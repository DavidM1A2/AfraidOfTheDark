package com.DavidM1A2.afraidofthedark.common.entity.spell.projectile.animation;

import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedEntity;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * Animation handler class for the spell projectile
 */
public class AnimationHandlerSpellProjectile extends AnimationHandler
{
    // Map of animation name to channel
    private static final Map<String, Channel> ANIMATION_TO_CHANNEL = new HashMap<>();

    static
    {
        // Add the 1 animation a spell projectile can use
        ANIMATION_TO_CHANNEL.put("Idle", new ChannelSpellProjectileIdle("Idle", 100.0F, 60, Channel.LOOP));
    }

    /**
     * Constructor just calls super
     *
     * @param entity The entity that this animator belongs to
     */
    public AnimationHandlerSpellProjectile(IMCAnimatedEntity entity)
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
