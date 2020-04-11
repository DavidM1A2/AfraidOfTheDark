package com.davidm1a2.afraidofthedark.common.entity.spell.projectile.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel

/**
 * Animation handler class for the spell projectile
 *
 * @param model The entity that this animator belongs to
 */
class AnimationHandlerSpellProjectile(model: IMCAnimatedModel) : AnimationHandler(model) {
    /**
     * Begins playing a specific animation given a name and starting frame
     *
     * @param name          The animation to play
     * @param startingFrame The frame to begin playing at
     */
    override fun activateAnimation(name: String, startingFrame: Float) {
        super.activateAnimation(ANIMATION_TO_CHANNEL, name, startingFrame)
    }

    /**
     * Stops playing a given animation
     *
     * @param name The animation to stop playing
     */
    override fun stopAnimation(name: String) {
        super.stopAnimation(ANIMATION_TO_CHANNEL, name)
    }

    companion object {
        // Map of animation name to channel
        private val ANIMATION_TO_CHANNEL = mapOf(
            "Idle" to ChannelSpellProjectileIdle("Idle", 100.0f, 60, Channel.LOOP)
        )
    }
}