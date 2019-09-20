package com.davidm1a2.afraidofthedark.common.entity.werewolf.animation;

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedEntity;
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler;
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * Animation handler class for the werewolf
 */
public class AnimationHandlerWerewolf extends AnimationHandler
{
    // Map of animation name to channel
    public static Map<String, Channel> animChannels = new HashMap<String, Channel>();

    static
    {
        // Add the 2 animations a werewolf can use
        animChannels.put("Bite", new ChannelBite("Bite", 50.0F, 21, Channel.LINEAR));
        animChannels.put("Run", new ChannelRun("Run", 60.0F, 32, Channel.LINEAR));
    }

    /**
     * Constructor just calls super
     *
     * @param entity The entity that this animator belongs to
     */
    public AnimationHandlerWerewolf(IMCAnimatedEntity entity)
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
        super.activateAnimation(animChannels, name, startingFrame);
    }

    /**
     * Stops playing a given animation
     *
     * @param name The animation to stop playing
     */
    @Override
    public void stopAnimation(String name)
    {
        super.stopAnimation(animChannels, name);
    }
}
