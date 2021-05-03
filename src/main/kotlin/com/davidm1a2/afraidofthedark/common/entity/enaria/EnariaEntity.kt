package com.davidm1a2.afraidofthedark.common.entity.enaria

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.*
import com.davidm1a2.afraidofthedark.common.entity.enaria.animation.ArmthrowChannel
import com.davidm1a2.afraidofthedark.common.entity.enaria.animation.AutoattackChannel
import com.davidm1a2.afraidofthedark.common.entity.enaria.animation.SpellChannel
import com.davidm1a2.afraidofthedark.common.entity.enaria.animation.WalkChannel
import com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton.EnchantedSkeletonEntity
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.SplinterDroneEntity
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.SplinterDroneProjectileEntity
import com.davidm1a2.afraidofthedark.common.entity.werewolf.WerewolfEntity
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.PlayEnariasFightMusicPacket
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.MobEntity
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.goal.LookAtGoal
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal
import net.minecraft.entity.ai.goal.SwimGoal
import net.minecraft.entity.item.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.IPacket
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.DamageSource
import net.minecraft.util.EntityDamageSource
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.BossInfo
import net.minecraft.world.ServerBossInfo
import net.minecraft.world.World
import net.minecraftforge.fml.network.NetworkHooks
import kotlin.math.min

/**
 * The enaria entity that is the overworld boss
 *
 * @constructor Required single arg constructor just sets the entity size and world
 * @param world The world enaria is in
 * @property animHandler Enaria's animation handler
 * @property bossInfo Enaria's boss info that allows the displaying of the boss HP bar
 * @property enariaAttackHelper Enaria's attack object use to manage her attacks
 */
class EnariaEntity(entityType: EntityType<out EnariaEntity>, world: World) : MobEntity(entityType, world), IMCAnimatedModel {
    private val animHandler = AnimationHandler(
        WalkChannel("walk", 59.0f, 59, ChannelMode.LINEAR),
        ArmthrowChannel("armthrow", 61.0f, 61, ChannelMode.LINEAR),
        AutoattackChannel("autoattack", 70.0f, 51, ChannelMode.LINEAR),
        SpellChannel("spell", 90.0f, 121, ChannelMode.LINEAR)
    )
    private val bossInfo = ServerBossInfo(
        StringTextComponent("placeholder"),
        BossInfo.Color.PURPLE,
        BossInfo.Overlay.PROGRESS
    ).setDarkenSky(true) as ServerBossInfo

    val enariaAttackHelper: EnariaAttackHelper

    init {
        // Name of the entity, will be bold and red
        this.customName = StringTextComponent("Enaria")
        bossInfo.name = this.displayName
        experienceValue = 300
        enariaAttackHelper = EnariaAttackHelper(this, rand)
    }

    /**
     * Overloaded constructor that sets the world and the region enaria is allowed to be in
     *
     * @param world The world to spawn enaria in
     * @param allowedRegion The region enaria is allowed to be inside of
     */
    constructor(world: World, allowedRegion: AxisAlignedBB) : this(ModEntities.ENARIA, world) {
        this.dataManager[IS_VALID] = true
        this.dataManager[ALLOWED_REGION] = allowedRegion
    }

    /**
     * Initialize dataManager
     */
    override fun registerData() {
        super.registerData()
        this.dataManager.register(IS_VALID, false)
        this.dataManager.register(LAST_HIT, 0)
        this.dataManager.register(ALLOWED_REGION, AxisAlignedBB(BlockPos.ZERO))
    }

    /**
     * Initializes enaria's AI
     */
    override fun registerGoals() {
        // First ensure we're not drowning and try to swim
        goalSelector.addGoal(1, SwimGoal(this))
        // Attack the target if we are not swimming
        goalSelector.addGoal(2, EnariaAttackGoal(this))
        // Follow the target if we can't attack
        goalSelector.addGoal(3, FollowPlayerGoal(this, 8.0, 128.0, 32.0))
        // Watch the nearest player if we can't follow them
        goalSelector.addGoal(4, LookAtGoal(this, PlayerEntity::class.java, 20.0f))
        // Find the nearest attackable player and attack
        targetSelector.addGoal(0, NearestAttackableTargetGoal(this, PlayerEntity::class.java, true))
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
     * Called to update the entity every tick
     */
    override fun tick() {
        super.tick()
        // Animations only update client side
        if (world.isRemote) {
            animHandler.update()
        }
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
            // If enaria was spawned without the right tags she's invalid so kill her
            if (ticksExisted == 1) {
                if (!this.dataManager.get(IS_VALID)) {
                    remove()
                }
            }
        }
    }

    /**
     * Called when enaria gets attacked
     *
     * @param source The damage source that hit enaria
     * @param amount The amount of damage inflicted
     * @return True to allow the damage, false otherwise
     */
    override fun attackEntityFrom(source: DamageSource, amount: Float): Boolean {
        // Server side processing only
        if (!world.isRemote) {
            // Compute the time between this hit and the last hit she received
            val timeBetweenHits = System.currentTimeMillis() - this.dataManager.get(LAST_HIT)
            // Update the last hit time
            this.dataManager.set(LAST_HIT, timeBetweenHits.toInt())

            // Make amount mutable
            @Suppress("NAME_SHADOWING")
            var amount = amount

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
                    if (!damageSource.getResearch().isResearched(ModResearches.ENARIA.preRequisite!!)) {
                        damageSource.sendMessage(TranslationTextComponent("message.afraidofthedark.enaria.dont_understand"))
                        // Can't damage enaria without research
                        return false
                    }
                }

                // If the damage source is silver damage inflict heavy damage
                if (source.damageType.equals(ModDamageSources.SILVER_DAMAGE, ignoreCase = true)) {
                    // If its been more than a second since the last attack do full damage, otherwise scale the damage
                    val amountModifier = min(1.0f, timeBetweenHits / 1000.0f)

                    // If the amount of time is less than 250ms then the player is spam clicking and enaria should teleport 1/40 times
                    if (rand.nextInt(40) == 0) {
                        enariaAttackHelper.randomTeleport()
                    }
                    return super.attackEntityFrom(source, amount * amountModifier)
                } else {
                    if (rand.nextInt(60) == 0) {
                        enariaAttackHelper.randomTeleport()
                    }
                }
            } else if (source == DamageSource.FALL) {
                if (rand.nextBoolean()) {
                    enariaAttackHelper.randomTeleport()
                }
            } else {
                if (rand.nextInt(100) == 0) {
                    enariaAttackHelper.randomTeleport()
                }
            }
        }

        // Finally if nothing succeeds do 1 damage. Only do this if she has more than 1hp, this ensures she doesn't
        // die to a non-player source
        return if (this.health >= 2.0) {
            super.attackEntityFrom(DamageSource.GENERIC, 1f)
        } else {
            false
        }
    }

    /**
     * When enaria dies by a player unlock the enaria research
     *
     * @param cause The damage source that killed enaria
     */
    override fun onDeath(cause: DamageSource) {
        super.onDeath(cause)
        // Server side processing only
        if (!world.isRemote) {
            // If enaria died to a player damage source continue processing...
            if (cause is EntityDamageSource) {
                // If enaria died to a player continue processing...
                if (cause.getTrueSource() is PlayerEntity) {
                    // Grab all entities around enaria and if they can research "ENARIA" unlock the research for them
                    for (entityPlayer in world.getEntitiesWithinAABB(
                        PlayerEntity::class.java,
                        this.boundingBox.grow(RESEARCH_UNLOCK_RANGE)
                    )) {
                        val playerResearch = entityPlayer.getResearch()
                        // If we can research enaria unlock it
                        if (playerResearch.canResearch(ModResearches.ENARIA)) {
                            playerResearch.setResearch(ModResearches.ENARIA, true)
                            playerResearch.sync(entityPlayer, true)
                        }
                    }

                    // Kill all the entities enaria may have spawned
                    for (entity in world.getEntitiesWithinAABB(
                        Entity::class.java,
                        getAllowedRegion()
                    ) {
                        it is WerewolfEntity || it is SplinterDroneProjectileEntity || it is SplinterDroneEntity || it is EnchantedSkeletonEntity
                    }) {
                        entity.onKillCommand()
                    }

                    // Kill all the enchanted skeleton bones
                    for (itemEntity in world.getEntitiesWithinAABB(
                        ItemEntity::class.java,
                        getAllowedRegion()
                    )) {
                        if (itemEntity.item.item == ModItems.ENCHANTED_SKELETON_BONE) {
                            itemEntity.onKillCommand()
                        }
                    }
                }
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
     * Enaria can't ride anything
     *
     * @param entityIn The entity to test
     * @return False, duh
     */
    override fun canBeRidden(entityIn: Entity): Boolean {
        return false
    }

    /**
     * @return False, enaria spawn behavior is managed by the tile entity
     */
    override fun canDespawn(distanceToClosestPlayer: Double): Boolean {
        return false
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
        // Tell the player to play the enaria combat music
        AfraidOfTheDark.packetHandler.sendTo(PlayEnariasFightMusicPacket(this, true), player)
        bossInfo.addPlayer(player)
    }

    /**
     * Called whenever a player leaves range of enaria to hide the boss bar
     *
     * @param player The player to remove enaria's boss bar from
     */
    override fun removeTrackingPlayer(player: ServerPlayerEntity) {
        super.removeTrackingPlayer(player)
        // Tell the player to stop playing the enaria combat music
        AfraidOfTheDark.packetHandler.sendTo(PlayEnariasFightMusicPacket(this, false), player)
        bossInfo.removePlayer(player)
    }

    /**
     * @return False, enaria is a boss mob
     */
    override fun isNonBoss(): Boolean {
        return false
    }

    fun getAllowedRegion(): AxisAlignedBB {
        return dataManager[ALLOWED_REGION]
    }

    /**
     * Ghastly Enaria can't be pushed by water
     *
     * @return false
     */
    override fun isPushedByWater(): Boolean {
        return false
    }

    override fun createSpawnPacket(): IPacket<*> {
        return NetworkHooks.getEntitySpawningPacket(this)
    }

    override fun readAdditional(compound: CompoundNBT) {
        super.readAdditional(compound)
        this.dataManager[IS_VALID] = compound.getBoolean("is_valid")
        this.dataManager[LAST_HIT] = compound.getInt("last_hit")
        this.dataManager[ALLOWED_REGION] = AxisAlignedBB(
            compound.getDouble("allowed_region_min_x"),
            compound.getDouble("allowed_region_min_y"),
            compound.getDouble("allowed_region_min_z"),
            compound.getDouble("allowed_region_max_x"),
            compound.getDouble("allowed_region_max_y"),
            compound.getDouble("allowed_region_max_z")
        )
    }

    override fun writeAdditional(compound: CompoundNBT) {
        super.writeAdditional(compound)
        compound.putBoolean("is_valid", this.dataManager[IS_VALID])
        compound.putInt("last_hit", this.dataManager[LAST_HIT])
        compound.putDouble("allowed_region_min_x", this.dataManager[ALLOWED_REGION].minX)
        compound.putDouble("allowed_region_min_y", this.dataManager[ALLOWED_REGION].minY)
        compound.putDouble("allowed_region_min_z", this.dataManager[ALLOWED_REGION].minZ)
        compound.putDouble("allowed_region_max_x", this.dataManager[ALLOWED_REGION].maxX)
        compound.putDouble("allowed_region_max_y", this.dataManager[ALLOWED_REGION].maxY)
        compound.putDouble("allowed_region_max_z", this.dataManager[ALLOWED_REGION].maxZ)
    }

    companion object {
        private val IS_VALID = EntityDataManager.createKey(EnariaEntity::class.java, DataSerializers.BOOLEAN)
        private val LAST_HIT = EntityDataManager.createKey(EnariaEntity::class.java, DataSerializers.VARINT)
        private val ALLOWED_REGION = EntityDataManager.createKey(EnariaEntity::class.java, ModDataSerializers.AABB)

        // Constants for enaria's stats
        private const val MOVE_SPEED = 0.6
        private const val FOLLOW_RANGE = 64.0
        private const val MAX_HEALTH = 400.0
        private const val ATTACK_DAMAGE = 12.0
        private const val KNOCKBACK_RESISTANCE = 0.5
        private const val RESEARCH_UNLOCK_RANGE = 100.0

        // The maximum amount of damage done in a single shot
        private const val MAX_DAMAGE_IN_1_HIT = 10
    }
}