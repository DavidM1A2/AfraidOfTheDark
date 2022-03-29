package com.davidm1a2.afraidofthedark.common.entity.frostPhoenix

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
import net.minecraft.entity.EntityType
import net.minecraft.entity.FlyingEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.Pose
import net.minecraft.entity.ai.attributes.AttributeModifierMap
import net.minecraft.entity.ai.attributes.Attributes
import net.minecraft.entity.ai.goal.LookAtGoal
import net.minecraft.entity.ai.goal.LookRandomlyGoal
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.CompoundNBT
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

    private var spawnerPos: BlockPos
        set(value) = entityData.set(SPAWNER_POS, value)
        get() = entityData.get(SPAWNER_POS)

    init {
        xpReward = 30
    }

    constructor(world: World, spawnerPos: BlockPos) : this(ModEntities.FROST_PHOENIX, world) {
        this.spawnerPos = spawnerPos
    }

    override fun defineSynchedData() {
        super.defineSynchedData()
        entityData.define(SPAWNER_POS, BlockPos.ZERO)
    }

    /**
     * Creates the AI used by hostile or passive entities
     */
    override fun registerGoals() {
        // If the entity isn't wandering then try to watch whatever entity is nearby
        goalSelector.addGoal(0, LookAtGoal(this, PlayerEntity::class.java, FOLLOW_RANGE.toFloat()))
        // If the entity isn't walking, attacking, or watching anything look idle
        goalSelector.addGoal(1, LookRandomlyGoal(this))
    }

    override fun baseTick() {
        super.baseTick()

        if (level.isClientSide) {
            // animHandler.stopAnimation("Fly")
            if (!animHandler.isAnimationActive("Fly")) {
                animHandler.playAnimation("Fly")
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

    /**
     * @return The eye height of the frog which is used in path finding and looking around
     */
    override fun getEyeHeight(pose: Pose): Float {
        return 1.5f
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
    }

    override fun addAdditionalSaveData(compound: CompoundNBT) {
        super.addAdditionalSaveData(compound)
        compound.putInt("spawner_pos_x", this.spawnerPos.x)
        compound.putInt("spawner_pos_y", this.spawnerPos.y)
        compound.putInt("spawner_pos_z", this.spawnerPos.z)
    }

    companion object {
        private val SPAWNER_POS = EntityDataManager.defineId(FrostPhoenixEntity::class.java, DataSerializers.BLOCK_POS)

        // Constants defining phoenix parameters
        private const val MOVE_SPEED = 0.25
        private const val FOLLOW_RANGE = 64.0
        private const val MAX_HEALTH = 7.0
        private const val KNOCKBACK_RESISTANCE = 0.5

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
            return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, MAX_HEALTH)
                .add(Attributes.FOLLOW_RANGE, FOLLOW_RANGE)
                .add(Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_RESISTANCE)
                .add(Attributes.MOVEMENT_SPEED, MOVE_SPEED)
        }
    }
}