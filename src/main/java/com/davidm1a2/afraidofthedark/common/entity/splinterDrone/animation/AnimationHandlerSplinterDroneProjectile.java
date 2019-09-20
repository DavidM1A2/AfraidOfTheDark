package com.davidm1a2.afraidofthedark.common.entity.splinterDrone.animation;

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedEntity;
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler;
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * Animation handler class for the splinter drone projectile
 */
public class AnimationHandlerSplinterDroneProjectile extends AnimationHandler
{
    // Map of animation name to channel
    private static final Map<String, Channel> ANIMATION_TO_CHANNEL = new HashMap<>();

    static
    {
        // Add the 1 animation a splinter drone projectile can use
        ANIMATION_TO_CHANNEL.put("Sping", new ChannelSping("Sping", 100.0F, 100, Channel.LINEAR));
    }

    /**
     * Constructor just calls super
     *
     * @param entity The entity that this animator belongs to
     */
    public AnimationHandlerSplinterDroneProjectile(IMCAnimatedEntity entity)
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
