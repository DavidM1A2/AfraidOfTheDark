/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedEntity
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel

/**
 * Animation handler class for the enchanted skeleton
 *
 * @param entity The entity that this animator belongs to
 */
class AnimationHandlerEnchantedSkeleton(entity: IMCAnimatedEntity) : AnimationHandler(entity)
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
        private val ANIMATION_TO_CHANNEL = mapOf(
            "Walk" to ChannelWalk("Walk", 20.0f, 40, Channel.LINEAR),
            "Attack" to ChannelAttack("Attack", 30.0f, 20, Channel.LINEAR),
            "Spawn" to ChannelSpawn("Spawn", 20.0f, 40, Channel.LINEAR),
            "Idle" to ChannelIdle("Idle", 10.0f, 20, Channel.LOOP)
        )
    }
}