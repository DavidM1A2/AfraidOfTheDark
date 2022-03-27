package com.davidm1a2.afraidofthedark.common.entity.frostPhoenix

import com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.animation.AttackChannel
import com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.animation.FlyChannel
import com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.animation.IdleFlapChannel
import com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.animation.IdleLookChannel
import com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.animation.LandChannel
import com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.animation.LaunchChannel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
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

    init {
        xpReward = 30
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

    companion object {
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