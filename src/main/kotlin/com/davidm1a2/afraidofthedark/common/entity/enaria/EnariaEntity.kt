package com.davidm1a2.afraidofthedark.common.entity.enaria

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
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
import com.davidm1a2.afraidofthedark.common.utility.damagesource.AstralSilverDamageSource
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.core.BlockPos
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.MobEntity
import net.minecraft.entity.ai.attributes.AttributeModifierMap
import net.minecraft.entity.ai.attributes.Attributes
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
import net.minecraft.world.World
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.level.Level
import net.minecraft.world.server.ServerBossInfo
import net.minecraftforge.fml.network.NetworkHooks
import kotlin.math.min

class EnariaEntity(entityType: EntityType<out EnariaEntity>, world: Level) : Mob(entityType, world), IMCAnimatedModel {
    private val animHandler = AnimationHandler(WALK_CHANNEL, ARMTHROW_CHANNEL, AUTOATTACK_CHANNEL, SPELL_CHANNEL)
    private val bossInfo = ServerBossInfo(
        StringTextComponent("placeholder"),
        BossInfo.Color.PURPLE,
        BossInfo.Overlay.PROGRESS
    ).setDarkenScreen(true) as ServerBossInfo

    // Fight data will only be stored server side
    private lateinit var fight: EnariaFight
    private var lastHit = 0
    private var spawnerTilePos = BlockPos.ZERO
    var canMove = true

    init {
        // Name of the entity, will be bold and red
        this.customName = StringTextComponent("Enaria")
        bossInfo.name = this.displayName
        xpReward = 300
        // Can't despawn
        setPersistenceRequired()
    }

    /**
     * Overloaded constructor that sets the world
     *
     * @param world The world to spawn enaria in
     */
    constructor(world: Level, spawnerTilePos: BlockPos) : this(ModEntities.ENARIA, world) {
        if (!world.isClientSide) {
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
     * Called every game tick for the entity
     */
    override fun baseTick() {
        super.baseTick()
        // Client side test if enaria is walking, if so play the animation
        if (level.isClientSide) {
            // Motion >= 0.5 = walking
            if (deltaMovement.x > 0.05 || deltaMovement.z > 0.05 || deltaMovement.x < -0.05 || deltaMovement.z < -0.05) {
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
        } else {
            // Update the boss info HP bar
            bossInfo.percent = this.health / this.maxHealth
            // Tick the fight if enaria is alive. There's an edge case where we tick the fight one too many times after she's dead leading to infinite music :P
            if (!dead) {
                fight.tick(tickCount)
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
    override fun hurt(source: DamageSource, rawAmount: Float): Boolean {
        // Server side processing only
        if (!level.isClientSide) {
            // Compute the time between this hit and the last hit she received
            val timeBetweenHits = System.currentTimeMillis() - this.lastHit
            // Update the last hit time
            this.lastHit = timeBetweenHits.toInt()

            // Make amount mutable
            var amount = rawAmount

            // Kill the entity if damage received is FLOAT.MAX_VALUE
            if (amount == Float.MAX_VALUE) {
                return super.hurt(source, amount)
            } else if (amount > MAX_DAMAGE_IN_1_HIT) {
                amount = MAX_DAMAGE_IN_1_HIT.toFloat()
            }

            // If an entity damaged the entity check if it was with silver damage
            if (source is EntityDamageSource) {
                // Grab the source of the damage
                val damageSource = source.entity
                if (damageSource is PlayerEntity) {
                    // If a player hit enaria check if they have the right research
                    if (!damageSource.getResearch().canResearch(ModResearches.ARCH_SORCERESS) && !damageSource.getResearch().isResearched(ModResearches.ARCH_SORCERESS)) {
                        damageSource.sendMessage(TranslationTextComponent("message.afraidofthedark.enaria.dont_understand"))
                        // Can't damage enaria without research
                        return false
                    }
                }

                // If the damage source is silver damage inflict heavy damage
                if (source is AstralSilverDamageSource) {
                    // If its been more than a second since the last attack do full damage, otherwise scale the damage
                    val amountModifier = min(1.0f, timeBetweenHits / 1000.0f)

                    return super.hurt(source, amount * amountModifier)
                }
            }
        }

        // Finally if nothing succeeds do 1 damage
        return super.hurt(DamageSource.GENERIC, 1f)
    }

    override fun die(cause: DamageSource) {
        super.die(cause)
        if (!level.isClientSide) {
            fight.end()
            val spawnerTileEntity = level.getBlockEntity(this.spawnerTilePos)
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
    override fun startSeenByPlayer(player: ServerPlayerEntity) {
        super.startSeenByPlayer(player)
        bossInfo.addPlayer(player)
    }

    /**
     * Called whenever a player leaves range of enaria to hide the boss bar
     *
     * @param player The player to remove enaria's boss bar from
     */
    override fun stopSeenByPlayer(player: ServerPlayerEntity) {
        super.stopSeenByPlayer(player)
        bossInfo.removePlayer(player)
    }

    override fun isPushedByFluid(): Boolean {
        return false
    }

    override fun getWaterSlowDown(): Float {
        return 0.0f
    }

    override fun canBreatheUnderwater(): Boolean {
        return true
    }

    override fun isPushable(): Boolean {
        return false
    }

    override fun canRide(entityIn: Entity): Boolean {
        return false
    }

    override fun shouldDespawnInPeaceful(): Boolean {
        // Can't despawn
        return false
    }

    override fun removeWhenFarAway(distanceToClosestPlayer: Double): Boolean {
        return false
    }

    override fun isInvulnerableTo(source: DamageSource): Boolean {
        if (source == DamageSource.FALL) {
            return true
        }
        return super.isInvulnerableTo(source)
    }

    override fun getAddEntityPacket(): IPacket<*> {
        return NetworkHooks.getEntitySpawningPacket(this)
    }

    override fun readAdditionalSaveData(compound: CompoundNBT) {
        super.readAdditionalSaveData(compound)
        this.lastHit = compound.getInt("last_hit")
        this.spawnerTilePos = NBTUtil.readBlockPos(compound.getCompound("spawner_tile_pos"))
        if (!level.isClientSide) {
            this.fight = EnariaFight(this, this.spawnerTilePos)
            this.fight.deserializeNBT(compound.getCompound("fight"))
        }
    }

    override fun addAdditionalSaveData(compound: CompoundNBT) {
        super.addAdditionalSaveData(compound)
        compound.putInt("last_hit", this.lastHit)
        compound.put("spawner_tile_pos", NBTUtil.writeBlockPos(this.spawnerTilePos))
        if (!level.isClientSide) {
            compound.put("fight", this.fight.serializeNBT())
        }
    }

    companion object {
        // Constants for enaria's stats
        private const val MOVE_SPEED = 0.6
        private const val FOLLOW_RANGE = 64.0
        private const val MAX_HEALTH = 1000.0
        private const val ATTACK_DAMAGE = 12.0
        private const val KNOCKBACK_RESISTANCE = 0.5

        // The maximum amount of damage done in a single shot
        private const val MAX_DAMAGE_IN_1_HIT = 10

        private val WALK_CHANNEL = WalkChannel("walk", 59.0f, 59, ChannelMode.LINEAR)
        private val ARMTHROW_CHANNEL = ArmthrowChannel("armthrow", 61.0f, 61, ChannelMode.LINEAR)
        private val AUTOATTACK_CHANNEL = AutoattackChannel("autoattack", 70.0f, 51, ChannelMode.LINEAR)
        private val SPELL_CHANNEL = SpellChannel("spell", 50.0f, 121, ChannelMode.LINEAR)

        /**
         * Gives enaria her entity attributes like damage and movespeed
         */
        fun buildAttributeModifiers(): AttributeSupplier.Builder {
            return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, MAX_HEALTH)
                .add(Attributes.FOLLOW_RANGE, FOLLOW_RANGE)
                .add(Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_RESISTANCE)
                .add(Attributes.MOVEMENT_SPEED, MOVE_SPEED)
                .add(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE)
        }
    }
}