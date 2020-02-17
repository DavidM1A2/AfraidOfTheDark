package com.davidm1a2.afraidofthedark.common.entity.werewolf.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedEntity
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel

/**
 * Animation handler class for the werewolf
 *
 * @param entity The entity that this animator belongs to
 */
class AnimationHandlerWerewolf(entity: IMCAnimatedEntity) : AnimationHandler(entity)
{
    /**
     * Begins playing a specific animation given a name and starting frame
     *
     * @param name          The animation to play
     * @param startingFrame The frame to begin playing at
     */
    override fun activateAnimation(name: String, startingFrame: Float)
    {
        super.activateAnimation(ANIMATION_TO_CHANNEL, name, startingFrame)
    }

    /**
     * Stops playing a given animation
     *
     * @param name The animation to stop playing
     */
    override fun stopAnimation(name: String)
    {
        super.stopAnimation(ANIMATION_TO_CHANNEL, name)
    }

    companion object
    {
        // Map of animation name to channel
        var ANIMATION_TO_CHANNEL = mapOf(
                "Bite" to ChannelBite("Bite", 50.0f, 21, Channel.LINEAR),
                "Run" to ChannelRun("Run", 60.0f, 32, Channel.LINEAR)
        )
    }
}