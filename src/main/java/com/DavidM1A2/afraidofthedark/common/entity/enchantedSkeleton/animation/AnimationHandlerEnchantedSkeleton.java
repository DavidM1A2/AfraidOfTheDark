/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.afraidofthedark.common.entity.enchantedSkeleton.animation;


import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedEntity;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.animation.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * Animation handler class for the enchanted skeleton
 */
public class AnimationHandlerEnchantedSkeleton extends AnimationHandler
{
	// Map of animation name to channel
	private static final Map<String, Channel> ANIMATION_TO_CHANNEL = new HashMap<>();

	static
	{
		// Add the 4 animations a skeleton can use
		ANIMATION_TO_CHANNEL.put("Walk", new ChannelWalk("Walk", 20.0F, 40, Channel.LINEAR));
		ANIMATION_TO_CHANNEL.put("Attack", new ChannelAttack("Attack", 30.0F, 20, Channel.LINEAR));
		ANIMATION_TO_CHANNEL.put("Spawn", new ChannelSpawn("Spawn", 20.0F, 40, Channel.LINEAR));
		ANIMATION_TO_CHANNEL.put("Idle", new ChannelIdle("Idle", 10.0F, 20, Channel.LOOP));
	}

	/**
	 * Constructor just calls super
	 *
	 * @param entity The entity that this animator belongs to
	 */
	public AnimationHandlerEnchantedSkeleton(IMCAnimatedEntity entity)
	{
		super(entity);
	}

	/**
	 * Begins playing a specific animation given a name and starting frame
	 *
	 * @param name The animation to play
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