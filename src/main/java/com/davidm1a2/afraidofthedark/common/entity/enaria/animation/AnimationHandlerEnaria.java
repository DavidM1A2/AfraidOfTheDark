package com.davidm1a2.afraidofthedark.common.entity.enaria.animation;

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedEntity;
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler;
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * Animation handler class for the enaria entity
 */
public class AnimationHandlerEnaria extends AnimationHandler
{
    // Map of animation name to channel
    private static final Map<String, Channel> ANIMATION_TO_CHANNEL = new HashMap<>();

    static
    {
        // Add the 4 animations enaria can use
        ANIMATION_TO_CHANNEL.put("walk", new ChannelWalk("walk", 59.0F, 59, Channel.LINEAR));
        ANIMATION_TO_CHANNEL.put("armthrow", new ChannelArmthrow("armthrow", 61.0F, 61, Channel.LINEAR));
        ANIMATION_TO_CHANNEL.put("autoattack", new ChannelAutoattack("autoattack", 70.0F, 51, Channel.LINEAR));
        ANIMATION_TO_CHANNEL.put("spell", new ChannelSpell("spell", 90.0F, 121, Channel.LINEAR));
    }

    /**
     * Constructor just calls super
     *
     * @param entity The entity that this animator belongs to
     */
    public AnimationHandlerEnaria(IMCAnimatedEntity entity)
    {
        super(entity);
    }

    /**
     * Begins playing a specific animation given a name and starting frame
     *
     * @param name          The animation to play
     * @param startingFrame The frame to begin playing at
     */
    @Override
    public void activateAnimation(String name, float startingFrame)
    {
        super.activateAnimation(ANIMATION_TO_CHANNEL, name, startingFrame);
    }

    /**
     * Stops playing a given animation
     *
     * @param name The animation to stop playing
     */
    @Override
    public void stopAnimation(String name)
    {
        super.stopAnimation(ANIMATION_TO_CHANNEL, name);
    }
}
