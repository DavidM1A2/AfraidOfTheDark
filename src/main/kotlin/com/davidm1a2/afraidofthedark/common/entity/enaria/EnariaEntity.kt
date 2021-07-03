package com.davidm1a2.afraidofthedark.common.entity.enaria

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.entity.enaria.animation.ArmthrowChannel
import com.davidm1a2.afraidofthedark.common.entity.enaria.animation.AutoattackChannel
import com.davidm1a2.afraidofthedark.common.entity.enaria.animation.SpellChannel
import com.davidm1a2.afraidofthedark.common.entity.enaria.animation.WalkChannel
import com.davidm1a2.afraidofthedark.common.entity.enaria.fight.EnariaFight
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.tileEntity.EnariaSpawnerTileEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.MobEntity
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.goal.LookAtGoal
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.NBTUtil
import net.minecraft.network.IPacket
import net.minecraft.util.DamageSource
import net.minecraft.util.EntityDamageSource
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.BossInfo
import net.minecraft.world.ServerBossInfo
import net.minecraft.world.World
import net.minecraftforge.fml.network.NetworkHooks
import kotlin.math.min

class EnariaEntity(entityType: EntityType<out EnariaEntity>, world: World) : MobEntity(entityType, world), IMCAnimatedModel {
    private val animHandler = AnimationHandler(
        WalkChannel("walk", 59.0f, 59, ChannelMode.LINEAR),
        ArmthrowChannel("armthrow", 61.0f, 61, ChannelMode.LINEAR),
        AutoattackChannel("autoattack", 70.0f, 51, ChannelMode.LINEAR),
        SpellChannel("spell", 50.0f, 121, ChannelMode.LINEAR)
    )
    private val bossInfo = ServerBossInfo(
        StringTextComponent("placeholder"),
        BossInfo.Color.PURPLE,
        BossInfo.Overlay.PROGRESS
    ).setDarkenSky(true) as ServerBossInfo

    // Fight data will only be stored server side
    private lateinit var fight: EnariaFight
    private var lastHit = 0
    private var spawnerTilePos = BlockPos.ZERO
    var canMove = true

    init {
        // Name of the entity, will be bold and red
        this.customName = StringTextComponent("Enaria")
        bossInfo.name = this.displayName
        experienceValue = 300
    }

    /**
     * Overloaded constructor that sets the world
     *
     * @param world The world to spawn enaria in
     */
    constructor(world: World, spawnerTilePos: BlockPos) : this(ModEntities.ENARIA, world) {
        if (!world.isRemote) {
            this.fight = EnariaFight(this, spawnerTilePos)
            this.spawnerTilePos = spawnerTilePos
        }
    }

    /**
     * Initializes enaria's AI
     */
    override fun registerGoals() {
        // Follow the target
        goalSelector.addGoal(1, FollowPlayerGoal(this, 8.0, 128.0, 64.0))
        // Watch the nearest player if we can't follow them
        goalSelector.addGoal(2, LookAtGoal(this, PlayerEntity::class.java, 100.0f))
    }

    /**
     * Gives enaria her entity attributes like damage and movespeed
     */
    override fun registerAttributes() {
        super.registerAttributes()
        attributes.getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH)?.baseValue = MAX_HEALTH
        attributes.getAttributeInstance(SharedMonsterAttributes.FOLLOW_RANGE)?.baseValue = FOLLOW_RANGE
        attributes.getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE)?.baseValue = KNOCKBACK_RESISTANCE
        attributes.getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED)?.baseValue = MOVE_SPEED
        attributes.registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).baseValue = ATTACK_DAMAGE
    }

    /**
     * Called every game tick for the entity
     */
    override fun baseTick() {
        super.baseTick()
        // Client side test if enaria is walking, if so play the animation
        if (world.isRemote) {
            // Motion >= 0.5 = walking
            if (motion.x > 0.05 || motion.z > 0.05 || motion.x < -0.05 || motion.z < -0.05) {
                // Ensure no other animation is currently active
                if (!animHandler.isAnimationActive("spell") &&
                    !animHandler.isAnimationActive("autoattack") &&
                    !animHandler.isAnimationActive("armthrow") &&
                    !animHandler.isAnimationActive("walk")
                ) {
                    // Walk
                    animHandler.playAnimation("walk")
                }
            }
        }
    }

    /**
     * Called every tick the enaria entity is alive
     */
    override fun livingTick() {
        super.livingTick()
        // If we're on server side perform some checks
        if (!world.isRemote) {
            // Update the boss info HP bar
            bossInfo.percent = this.health / this.maxHealth
            // Tick the fight if enaria is alive. There's an edge case where we tick the fight one too many times after she's dead leading to infinite music :P
            if (!dead) {
                fight.tick(ticksExisted)
            }
        }
    }

    /**
     * Called when enaria gets attacked
     *
     * @param source The damage source that hit enaria
     * @param rawAmount The amount of damage inflicted
     * @return True to allow the damage, false otherwise
     */
    override fun attackEntityFrom(source: DamageSource, rawAmount: Float): Boolean {
        // Server side processing only
        if (!world.isRemote) {
            // Compute the time between this hit and the last hit she received
            val timeBetweenHits = System.currentTimeMillis() - this.lastHit
            // Update the last hit time
            this.lastHit = timeBetweenHits.toInt()

            // Make amount mutable
            var amount = rawAmount

            // Kill the entity if damage received is FLOAT.MAX_VALUE
            if (amount == Float.MAX_VALUE) {
                return super.attackEntityFrom(source, amount)
            } else if (amount > MAX_DAMAGE_IN_1_HIT) {
                amount = MAX_DAMAGE_IN_1_HIT.toFloat()
            }

            // If an entity damaged the entity check if it was with silver damage
            if (source is EntityDamageSource) {
                // Grab the source of the damage
                val damageSource = source.getTrueSource()
                if (damageSource is PlayerEntity) {
                    // If a player hit enaria check if they have the right research
                    if (!damageSource.getResearch().canResearch(ModResearches.ENARIA) && !damageSource.getResearch().isResearched(ModResearches.ENARIA)) {
                        damageSource.sendMessage(TranslationTextComponent("message.afraidofthedark.enaria.dont_understand"))
                        // Can't damage enaria without research
                        return false
                    }
                }

                // If the damage source is silver damage inflict heavy damage
                if (source.damageType.equals(ModDamageSources.SILVER_DAMAGE, ignoreCase = true)) {
                    // If its been more than a second since the last attack do full damage, otherwise scale the damage
                    val amountModifier = min(1.0f, timeBetweenHits / 1000.0f)

                    return super.attackEntityFrom(source, amount * amountModifier)
                }
            }
        }

        // Finally if nothing succeeds do 1 damage
        return super.attackEntityFrom(DamageSource.GENERIC, 1f)
    }

    override fun onDeath(cause: DamageSource) {
        super.onDeath(cause)
        if (!world.isRemote) {
            fight.end()
            val spawnerTileEntity = world.getTileEntity(this.spawnerTilePos)
            if (spawnerTileEntity is EnariaSpawnerTileEntity) {
                spawnerTileEntity.endFight()
            }
        }
    }

    /**
     * @return Make enaria's name tag red and bold in chat
     */
    override fun getDisplayName(): ITextComponent {
        return this.customName!!
    }

    /**
     * @return The animation handler used to animate enaria
     */
    override fun getAnimationHandler(): AnimationHandler {
        return animHandler
    }

    /**
     * Called whenever a player enters range of enaria to show the boss bar
     *
     * @param player The player to show enaria's boss bar to
     */
    override fun addTrackingPlayer(player: ServerPlayerEntity) {
        super.addTrackingPlayer(player)
        bossInfo.addPlayer(player)
    }

    /**
     * Called whenever a player leaves range of enaria to hide the boss bar
     *
     * @param player The player to remove enaria's boss bar from
     */
    override fun removeTrackingPlayer(player: ServerPlayerEntity) {
        super.removeTrackingPlayer(player)
        bossInfo.removePlayer(player)
    }

    override fun isNonBoss(): Boolean {
        return false
    }

    override fun isPushedByWater(): Boolean {
        return false
    }

    override fun getWaterSlowDown(): Float {
        return 0.0f
    }

    override fun canBreatheUnderwater(): Boolean {
        return true
    }

    override fun canBePushed(): Boolean {
        return false
    }

    override fun canBeRidden(entityIn: Entity): Boolean {
        return false
    }

    override fun canDespawn(distanceToClosestPlayer: Double): Boolean {
        return false
    }

    override fun isInvulnerableTo(source: DamageSource): Boolean {
        if (source == DamageSource.FALL) {
            return true
        }
        return super.isInvulnerableTo(source)
    }

    override fun createSpawnPacket(): IPacket<*> {
        return NetworkHooks.getEntitySpawningPacket(this)
    }

    override fun readAdditional(compound: CompoundNBT) {
        super.readAdditional(compound)
        this.lastHit = compound.getInt("last_hit")
        this.spawnerTilePos = NBTUtil.readBlockPos(compound.getCompound("spawner_tile_pos"))
        if (!world.isRemote) {
            this.fight = EnariaFight(this, this.spawnerTilePos)
            this.fight.deserializeNBT(compound.getCompound("fight"))
        }
    }

    override fun writeAdditional(compound: CompoundNBT) {
        super.writeAdditional(compound)
        compound.putInt("last_hit", this.lastHit)
        compound.put("spawner_tile_pos", NBTUtil.writeBlockPos(this.spawnerTilePos))
        if (!world.isRemote) {
            compound.put("fight", this.fight.serializeNBT())
        }
    }

    companion object {
        // Constants for enaria's stats
        private const val MOVE_SPEED = 0.6
        private const val FOLLOW_RANGE = 64.0
        private const val MAX_HEALTH = 700.0
        private const val ATTACK_DAMAGE = 12.0
        private const val KNOCKBACK_RESISTANCE = 0.5

        // The maximum amount of damage done in a single shot
        private const val MAX_DAMAGE_IN_1_HIT = 10
    }
}