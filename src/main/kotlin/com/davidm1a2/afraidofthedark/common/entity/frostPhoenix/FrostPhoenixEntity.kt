package com.davidm1a2.afraidofthedark.common.entity.frostPhoenix

import com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.animation.AttackChannel
import com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.animation.FlyChannel
import com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.animation.IdleChannel
import com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.animation.LandChannel
import com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.animation.LaunchChannel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import net.minecraft.block.BlockState
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.MobEntity
import net.minecraft.entity.Pose
import net.minecraft.entity.ai.attributes.AttributeModifierMap
import net.minecraft.entity.ai.attributes.Attributes
import net.minecraft.entity.ai.goal.LookAtGoal
import net.minecraft.entity.ai.goal.LookRandomlyGoal
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class FrostPhoenixEntity(entityType: EntityType<out FrostPhoenixEntity>, world: World) : MobEntity(entityType, world), IMCAnimatedModel {
    private val animHandler = AnimationHandler(
        IdleChannel("Idle", .1F, 21, ChannelMode.LINEAR),
        LaunchChannel("Launch", 24.0F, 21, ChannelMode.LINEAR),
        FlyChannel("Fly", 24.0F, 21, ChannelMode.LINEAR),
        LandChannel("Land", 24.0F, 41, ChannelMode.LINEAR),
        AttackChannel("Attack", 24.0F, 11, ChannelMode.LINEAR)
    )

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
            animHandler.stopAnimation("Idle")
            //if (!animHandler.isAnimationActive("Idle")) {
            //    animHandler.playAnimation("Idle")
            //}
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

    companion object {
        // Constants defining phoenix parameters
        private const val MOVE_SPEED = 0.25
        private const val FOLLOW_RANGE = 32.0
        private const val MAX_HEALTH = 7.0
        private const val KNOCKBACK_RESISTANCE = 0.5

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