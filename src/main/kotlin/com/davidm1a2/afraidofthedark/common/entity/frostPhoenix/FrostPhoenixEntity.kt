package com.davidm1a2.afraidofthedark.common.entity.frostPhoenix

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModDataSerializers
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.animation.AttackChannel
import com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.animation.FlyChannel
import com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.animation.IdleFlapChannel
import com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.animation.IdleLookChannel
import com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.animation.LandChannel
import com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.animation.LaunchChannel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.tileEntity.FrostPhoenixSpawnerTileEntity
import net.minecraft.block.BlockState
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.entity.EntitySize
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.MobEntity
import net.minecraft.entity.MoverType
import net.minecraft.entity.Pose
import net.minecraft.entity.ai.attributes.AttributeModifierMap
import net.minecraft.entity.ai.attributes.Attributes
import net.minecraft.entity.ai.goal.LookAtGoal
import net.minecraft.entity.ai.goal.LookRandomlyGoal
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.IPacket
import net.minecraft.network.datasync.DataParameter
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.DamageSource
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.World
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.level.Level
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.fml.network.NetworkHooks
import kotlin.math.sin

class FrostPhoenixEntity(entityType: EntityType<out FrostPhoenixEntity>, world: Level) : Mob(entityType, world), IMCAnimatedModel {
    private val animHandler = AnimationHandler(
        IDLE_FLAP_CHANNEL,
        LAUNCH_CHANNEL,
        FLY_CHANNEL,
        IDLE_LOOK_CHANNEL,
        ATTACK_CHANNEL,
        LAND_CHANNEL,
        STORM_CHANNEL
    )

    private var performedInitialAnimationSync = false
    internal val combatManager = FrostPhoenixCombatManager(this)

    var spawnerPos: BlockPos
        private set(value) = entityData.set(SPAWNER_POS, value)
        get() = entityData.get(SPAWNER_POS)

    var stance: FrostPhoenixStance
        internal set(value) = entityData.set(STANCE, value)
        get() = entityData.get(STANCE)

    init {
        xpReward = 30
        moveControl = FrostPhoenixMovementController(this)
        animHandler.animationFinishedCallback = {
            when (it) {
                FLY_CHANNEL -> when (stance) {
                    FrostPhoenixStance.FLYING -> animHandler.playAnimation(FLY_CHANNEL.name)
                    FrostPhoenixStance.LANDING -> animHandler.playAnimation(LAND_CHANNEL.name)
                    FrostPhoenixStance.STORMING -> animHandler.playAnimation(STORM_CHANNEL.name)
                    else -> {}
                }
                STORM_CHANNEL -> when (stance) {
                    FrostPhoenixStance.FLYING -> animHandler.playAnimation(FLY_CHANNEL.name)
                    FrostPhoenixStance.STORMING -> animHandler.playAnimation(STORM_CHANNEL.name)
                    else -> {}
                }
                LAUNCH_CHANNEL -> animHandler.playAnimation(FLY_CHANNEL.name)
            }
        }
        setPersistenceRequired()
    }

    constructor(world: Level, spawnerPos: BlockPos) : this(ModEntities.FROST_PHOENIX, world) {
        this.spawnerPos = spawnerPos
    }

    override fun defineSynchedData() {
        super.defineSynchedData()
        entityData.define(SPAWNER_POS, BlockPos.ZERO)
        entityData.define(STANCE, FrostPhoenixStance.STANDING)
    }

    override fun onSyncedDataUpdated(dataParameter: DataParameter<*>) {
        super.onSyncedDataUpdated(dataParameter)
        if (dataParameter == STANCE) {
            refreshDimensions()
            if (level.isClientSide) {
                // When we first learn what stance the phoenix should have, update the animation
                if (!performedInitialAnimationSync) {
                    // Make sure to start the correct animation once we load in for fly and land. These are normally controlled via a state machine, but the
                    // client might not be in sync yet with that state machine.
                    when (stance) {
                        FrostPhoenixStance.FLYING -> animHandler.playAnimation(FLY_CHANNEL.name)
                        FrostPhoenixStance.STORMING -> animHandler.playAnimation(STORM_CHANNEL.name)
                        FrostPhoenixStance.LANDING -> animHandler.playAnimation(LAND_CHANNEL.name)
                        else -> {}
                    }
                    performedInitialAnimationSync = true
                }

                // When the server decides the phoenix should take off, play the animation
                if (stance == FrostPhoenixStance.TAKING_OFF) {
                    animHandler.playAnimation(LAUNCH_CHANNEL.name)
                }
            }
        }
    }

    override fun refreshDimensions() {
        val oldX = this.x
        val oldY = this.y
        val oldZ = this.z
        super.refreshDimensions()
        setPos(oldX, oldY, oldZ)
    }

    /**
     * Creates the AI used by hostile or passive entities
     */
    override fun registerGoals() {
        /*
        AI goes as follows from highest priority to lowest:
        1. Try to flap, heal, and make an ice storm if ready
        2. Try to attack the nearest target with an ice ball if ready
        3. Try to take off the ground
        4. Try to fly around
        5. Try to land
        6. Look around at nearby players
        7. Randomly look around
         */

        goalSelector.addGoal(0, FrostPhoenixFlapHealStormGoal(this))
        goalSelector.addGoal(1, FrostPhoenixProjectileAttackGoal(this))
        goalSelector.addGoal(2, FrostPhoenixTakeOffGoal(this))
        goalSelector.addGoal(3, FrostPhoenixFlyGoal(this))
        goalSelector.addGoal(4, FrostPhoenixLandGoal(this))
        goalSelector.addGoal(5, LookAtGoal(this, PlayerEntity::class.java, FOLLOW_RANGE.toFloat()))
        goalSelector.addGoal(6, LookRandomlyGoal(this))
        targetSelector.addGoal(0, FrostPhoenixHurtByPlayerTargetGoal(this))
    }

    override fun baseTick() {
        super.baseTick()

        if (level.isClientSide) {
            if (stance == FrostPhoenixStance.STANDING) {
                if (random.nextInt(60) == 0) {
                    if (animHandler.getActiveAnimations().isEmpty()) {
                        val animationName = if (random.nextBoolean()) {
                            IDLE_LOOK_CHANNEL.name
                        } else {
                            IDLE_FLAP_CHANNEL.name
                        }
                        animHandler.playAnimation(animationName)
                    }
                }
            }

            if (stance == FrostPhoenixStance.STORMING) {
                for (i in 0..STORM_PARTICLES_PER_TICK) {
                    Minecraft.getInstance().level!!.addParticle(
                        ModParticles.FROST_PHOENIX_STORM,
                        boundingBox.center.x,
                        boundingBox.center.y + random.nextGaussian() * 6.0 - 2.0,
                        boundingBox.center.z,
                        3.0 + (FrostPhoenixCombatManager.STORM_RADIUS_BLOCKS - 4.5) * random.nextDouble(),
                        0.0,
                        0.0,
                    )
                }
            }

            val player = Minecraft.getInstance().player!!
            if (stance == FrostPhoenixStance.FLYING || stance == FrostPhoenixStance.TAKING_OFF || stance == FrostPhoenixStance.LANDING) {
                if (tickCount % 14 == 0) {
                    AfraidOfTheDark.proxy.playSoundFixed(this, ModSounds.FROST_PHOENIX_FLY, SoundCategory.NEUTRAL, getVolumeByDistance(player, 45f), random.nextFloat() * 0.1f + 0.95f)
                }
            } else if (stance == FrostPhoenixStance.STORMING) {
                if (tickCount % 5 == 0) {
                    AfraidOfTheDark.proxy.playSoundFixed(this, ModSounds.FROST_PHOENIX_FLY, SoundCategory.NEUTRAL, getVolumeByDistance(player, 45f), random.nextFloat() * 0.1f + 1.0f)
                }
            }
        }
    }

    private fun getVolumeByDistance(playerEntity: PlayerEntity, maxDistanceBlocks: Float): Float {
        val distance = playerEntity.distanceTo(this)
        if (distance >= maxDistanceBlocks) {
            return 0f
        }
        // w * sin(PI / distance * x + PI / 2) + w = w at x=0, and 0 at x=45
        return 0.5f * sin(Math.PI.toFloat() / maxDistanceBlocks * distance + Math.PI.toFloat() / 2f) + 0.5f
    }

    override fun die(damageSource: DamageSource) {
        super.die(damageSource)
        reportPhoenixGone()
    }

    override fun checkDespawn() {
        val wasAlive = isAlive
        super.checkDespawn()
        if (!isAlive && wasAlive) {
            reportPhoenixGone()
        }
    }

    private fun reportPhoenixGone() {
        if (!level.isClientSide) {
            val tileEntity = level.getBlockEntity(spawnerPos)
            if (tileEntity is FrostPhoenixSpawnerTileEntity) {
                tileEntity.reportPhoenixGone()
            }
            // Else the tileEntity was broken
        }
    }

    override fun onAddedToWorld() {
        super.onAddedToWorld()
        performedInitialAnimationSync = false
        // If we were created with a spawn egg then circle this position
        if (spawnerPos == BlockPos.ZERO) {
            spawnerPos = blockPosition()
        }
    }

    /**
     * @return The handler for all animations for this entity
     */
    override fun getAnimationHandler(): AnimationHandler {
        return animHandler
    }

    override fun getEyeHeight(pose: Pose): Float {
        return 1.5f
    }

    override fun getDimensions(pose: Pose): EntitySize {
        return if (stance == FrostPhoenixStance.STANDING || stance == FrostPhoenixStance.LANDING) {
            STANDING_DIMENSIONS
        } else {
            FLYING_DIMENSIONS
        }
    }

    /**
     * Don't play a step sound for the phoenix
     *
     * @param pos The position
     * @param state The block's state
     */
    override fun playStepSound(pos: BlockPos, state: BlockState) {
    }

    override fun getSoundSource(): SoundCategory {
        return SoundCategory.HOSTILE
    }

    override fun playAmbientSound() {
        if (level.isClientSide) {
            val player = Minecraft.getInstance().player!!
            AfraidOfTheDark.proxy.playSoundFixed(this, ModSounds.FROST_PHOENIX_AMBIENT, SoundCategory.NEUTRAL, getVolumeByDistance(player, 100f), random.nextFloat() * 0.1f + 0.8f)
        }
    }

    override fun getAmbientSoundInterval(): Int {
        return 20 * 20 // 20 seconds
    }

    override fun getDeathSound(): SoundEvent {
        return ModSounds.FROST_PHOENIX_DEATH
    }

    override fun getHurtSound(damageSource: DamageSource): SoundEvent {
        return ModSounds.FROST_PHOENIX_HURT
    }

    override fun causeFallDamage(distance: Float, damageMultiplier: Float): Boolean {
        return false
    }

    override fun checkFallDamage(distance: Double, onGround: Boolean, blockState: BlockState, location: BlockPos) {}

    override fun travel(location: Vector3d) {
        if (stance == FrostPhoenixStance.STANDING) {
            super.travel(location)
        } else {
            flyingTravel(location)
        }
    }

    /**
     * Special version of travel() that flying entities use. It's a copy of FlyingEntity's travel()
     *
     * @param location Location to go to
     */
    private fun flyingTravel(location: Vector3d) {
        if (this.isInWater) {
            moveRelative(0.02f, location)
            move(MoverType.SELF, deltaMovement)
            deltaMovement = deltaMovement.scale(0.8)
        } else if (this.isInLava) {
            moveRelative(0.02f, location)
            move(MoverType.SELF, deltaMovement)
            deltaMovement = deltaMovement.scale(0.5)
        } else {
            val ground = BlockPos(this.x, this.y - 1.0, this.z)
            var movementModifier = 0.91f
            if (onGround) {
                movementModifier = level.getBlockState(ground).getSlipperiness(level, ground, this) * 0.91f
            }
            val slipperinessModifier = 0.16277137f / (movementModifier * movementModifier * movementModifier)
            moveRelative(if (onGround) 0.1f * slipperinessModifier else 0.02f, location)
            move(MoverType.SELF, deltaMovement)
            deltaMovement = deltaMovement.scale(movementModifier.toDouble())
        }

        calculateEntityAnimation(this, false)
    }

    override fun readAdditionalSaveData(compound: CompoundNBT) {
        super.readAdditionalSaveData(compound)
        if (compound.contains("spawner_pos_x") && compound.contains("spawner_pos_y") && compound.contains("spawner_pos_z")) {
            this.spawnerPos = BlockPos(
                compound.getInt("spawner_pos_x"),
                compound.getInt("spawner_pos_y"),
                compound.getInt("spawner_pos_z")
            )
        }
        if (compound.contains("stance")) {
            this.stance = FrostPhoenixStance.valueOf(compound.getString("stance"))
        }
        if (compound.contains("combat_manager")) {
            this.combatManager.deserializeNBT(compound.getCompound("combat_manager"))
        }
        val world = level
        if (compound.hasUUID("target_uuid") && world is ServerWorld) {
            target = world.getEntity(compound.getUUID("target_uuid")) as? LivingEntity?
        }
    }

    override fun shouldDespawnInPeaceful(): Boolean {
        return true
    }

    override fun getAddEntityPacket(): IPacket<*> {
        return NetworkHooks.getEntitySpawningPacket(this)
    }

    override fun addAdditionalSaveData(compound: CompoundNBT) {
        super.addAdditionalSaveData(compound)
        compound.putInt("spawner_pos_x", this.spawnerPos.x)
        compound.putInt("spawner_pos_y", this.spawnerPos.y)
        compound.putInt("spawner_pos_z", this.spawnerPos.z)
        compound.putString("stance", this.stance.name)
        compound.put("combat_manager", this.combatManager.serializeNBT())
        target?.let { compound.putUUID("target_uuid", it.uuid) }
    }

    companion object {
        private val SPAWNER_POS = EntityDataManager.defineId(FrostPhoenixEntity::class.java, DataSerializers.BLOCK_POS)
        private val STANCE = EntityDataManager.defineId(FrostPhoenixEntity::class.java, ModDataSerializers.FROST_PHOENIX_STANCE)

        // Constants defining phoenix parameters
        private const val MOVE_SPEED = 0.15
        private const val FOLLOW_RANGE = 120.0
        private const val MAX_HEALTH = 240.0
        private const val KNOCKBACK_RESISTANCE = 1.0

        private val STANDING_DIMENSIONS = EntitySize.scalable(1.3f, 5.8f)
        private val FLYING_DIMENSIONS = EntitySize.scalable(3.8f, 1.9f)

        private val IDLE_FLAP_CHANNEL = IdleFlapChannel("IdleFlap", 24.0F, 21, ChannelMode.LINEAR)
        private val LAUNCH_CHANNEL = LaunchChannel("Launch", 24.0F, 21, ChannelMode.LINEAR)
        private val FLY_CHANNEL = FlyChannel("Fly", 30.0F, 21, ChannelMode.LINEAR)
        private val STORM_CHANNEL = FlyChannel("Storm", 90.0F, 21, ChannelMode.LINEAR)
        private val IDLE_LOOK_CHANNEL = IdleLookChannel("IdleLook", 24.0F, 41, ChannelMode.LINEAR)
        private val ATTACK_CHANNEL = AttackChannel("Attack", 24.0F, 11, ChannelMode.LINEAR)
        private val LAND_CHANNEL = LandChannel("Land", 24.0F, 21, ChannelMode.LINEAR)

        private const val STORM_PARTICLES_PER_TICK = 3

        /**
         * Gives the phoenix its entity attributes like movespeed
         */
        fun buildAttributeModifiers(): AttributeSupplier.Builder {
            return createLivingAttributes()
                .add(Attributes.MAX_HEALTH, MAX_HEALTH)
                .add(Attributes.FOLLOW_RANGE, FOLLOW_RANGE)
                .add(Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_RESISTANCE)
                .add(Attributes.MOVEMENT_SPEED, MOVE_SPEED)
        }
    }
}