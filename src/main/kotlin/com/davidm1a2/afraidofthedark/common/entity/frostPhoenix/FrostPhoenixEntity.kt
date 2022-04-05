package com.davidm1a2.afraidofthedark.common.entity.frostPhoenix

import com.davidm1a2.afraidofthedark.common.constants.ModDataSerializers
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
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
import net.minecraft.entity.EntitySize
import net.minecraft.entity.EntityType
import net.minecraft.entity.FlyingEntity
import net.minecraft.entity.Pose
import net.minecraft.entity.ai.attributes.AttributeModifierMap
import net.minecraft.entity.ai.attributes.Attributes
import net.minecraft.entity.ai.goal.LookAtGoal
import net.minecraft.entity.ai.goal.LookRandomlyGoal
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.datasync.DataParameter
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.DamageSource
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class FrostPhoenixEntity(entityType: EntityType<out FrostPhoenixEntity>, world: World) : FlyingEntity(entityType, world), IMCAnimatedModel {
    private val animHandler = AnimationHandler(
        IDLE_FLAP_CHANNEL,
        LAUNCH_CHANNEL,
        FLY_CHANNEL,
        IDLE_LOOK_CHANNEL,
        ATTACK_CHANNEL,
        LAND_CHANNEL
    )

    var spawnerPos: BlockPos
        private set(value) = entityData.set(SPAWNER_POS, value)
        get() = entityData.get(SPAWNER_POS)

    var stance: FrostPhoenixStance
        internal set(value) = entityData.set(STANCE, value)
        get() = entityData.get(STANCE)

    init {
        xpReward = 30
        moveControl = FrostPhoenixMovementController(this)
        spawnerPos = blockPosition()
    }

    constructor(world: World, spawnerPos: BlockPos) : this(ModEntities.FROST_PHOENIX, world) {
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
            if (animHandler.getActiveAnimations().contains(FLY_CHANNEL)) {
                animHandler.stopAnimation(FLY_CHANNEL.name)
            }
            if (level.isClientSide) {
                when (stance) {
                    FrostPhoenixStance.FLYING -> animHandler.playAnimation(FLY_CHANNEL.name)
                    FrostPhoenixStance.LANDING -> animHandler.playAnimation(LAND_CHANNEL.name)
                    FrostPhoenixStance.TAKING_OFF -> animHandler.playAnimation(LAUNCH_CHANNEL.name)
                    else -> {}
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
        goalSelector.addGoal(0, FrostPhoenixTakeOffGoal(this))
        goalSelector.addGoal(1, FrostPhoenixFlyGoal(this))
        goalSelector.addGoal(2, FrostPhoenixLandGoal(this))
        // If the entity isn't wandering then try to watch whatever entity is nearby
        goalSelector.addGoal(3, LookAtGoal(this, PlayerEntity::class.java, FOLLOW_RANGE.toFloat()))
        // If the entity isn't walking, attacking, or watching anything look idle
        goalSelector.addGoal(4, LookRandomlyGoal(this))
    }

    override fun baseTick() {
        super.baseTick()

        if (level.isClientSide) {
            if (stance == FrostPhoenixStance.STANDING) {
                if (random.nextInt(40) == 0) {
                    val animationName = if (random.nextBoolean()) {
                        IDLE_LOOK_CHANNEL.name
                    } else {
                        IDLE_FLAP_CHANNEL.name
                    }
                    if (!animHandler.isAnimationActive(animationName)) {
                        animHandler.playAnimation(animationName)
                    }
                }
            }
        }
    }

    override fun die(damageSource: DamageSource) {
        super.die(damageSource)
        if (!level.isClientSide) {
            val tileEntity = level.getBlockEntity(spawnerPos)
            if (tileEntity is FrostPhoenixSpawnerTileEntity) {
                tileEntity.reportPhoenixDeath()
            }
            // Else the tileEntity was broken
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

    override fun readAdditionalSaveData(compound: CompoundNBT) {
        super.readAdditionalSaveData(compound)
        this.spawnerPos = BlockPos(
            compound.getInt("spawner_pos_x"),
            compound.getInt("spawner_pos_y"),
            compound.getInt("spawner_pos_z")
        )
        this.stance = FrostPhoenixStance.valueOf(compound.getString("stance"))
    }

    override fun addAdditionalSaveData(compound: CompoundNBT) {
        super.addAdditionalSaveData(compound)
        compound.putInt("spawner_pos_x", this.spawnerPos.x)
        compound.putInt("spawner_pos_y", this.spawnerPos.y)
        compound.putInt("spawner_pos_z", this.spawnerPos.z)
        compound.putString("stance", this.stance.name)
    }

    companion object {
        private val SPAWNER_POS = EntityDataManager.defineId(FrostPhoenixEntity::class.java, DataSerializers.BLOCK_POS)
        private val STANCE = EntityDataManager.defineId(FrostPhoenixEntity::class.java, ModDataSerializers.FROST_PHOENIX_STANCE)

        // Constants defining phoenix parameters
        private const val MOVE_SPEED = 0.15
        private const val FOLLOW_RANGE = 64.0
        private const val MAX_HEALTH = 7.0
        private const val KNOCKBACK_RESISTANCE = 1.0

        private val STANDING_DIMENSIONS = EntitySize.scalable(1.6f, 5.0f)
        private val FLYING_DIMENSIONS = EntitySize.scalable(4.0f, 2.0f)

        private val IDLE_FLAP_CHANNEL = IdleFlapChannel("IdleFlap", 24.0F, 21, ChannelMode.LINEAR)
        private val LAUNCH_CHANNEL = LaunchChannel("Launch", 24.0F, 21, ChannelMode.LINEAR)
        private val FLY_CHANNEL = FlyChannel("Fly", 30.0F, 21, ChannelMode.LOOP)
        private val IDLE_LOOK_CHANNEL = IdleLookChannel("IdleLook", 24.0F, 41, ChannelMode.LINEAR)
        private val ATTACK_CHANNEL = AttackChannel("Attack", 24.0F, 11, ChannelMode.LINEAR)
        private val LAND_CHANNEL = LandChannel("Land", 24.0F, 21, ChannelMode.LINEAR)

        /**
         * Gives the phoenix its entity attributes like movespeed
         */
        fun buildAttributeModifiers(): AttributeModifierMap.MutableAttribute {
            return createLivingAttributes()
                .add(Attributes.MAX_HEALTH, MAX_HEALTH)
                .add(Attributes.FOLLOW_RANGE, FOLLOW_RANGE)
                .add(Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_RESISTANCE)
                .add(Attributes.MOVEMENT_SPEED, MOVE_SPEED)
        }
    }
}