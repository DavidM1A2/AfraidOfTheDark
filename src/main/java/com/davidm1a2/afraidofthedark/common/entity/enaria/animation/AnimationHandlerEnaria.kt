package com.davidm1a2.afraidofthedark.common.entity.enaria.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedEntity
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel

/**
 * Animation handler class for the enaria entity
 *
 * @param entity The entity that this animator belongs to
 */
class AnimationHandlerEnaria(entity: IMCAnimatedEntity) : AnimationHandler(entity) {
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
            "walk" to ChannelWalk("walk", 59.0f, 59, Channel.LINEAR),
            "armthrow" to ChannelArmthrow("armthrow", 61.0f, 61, Channel.LINEAR),
            "autoattack" to ChannelAutoattack("autoattack", 70.0f, 51, Channel.LINEAR),
            "spell" to ChannelSpell("spell", 90.0f, 121, Channel.LINEAR)
        )
    }
}