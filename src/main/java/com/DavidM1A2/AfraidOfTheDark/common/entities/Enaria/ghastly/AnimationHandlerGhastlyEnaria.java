/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria.ghastly;

import java.util.HashMap;

import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.IMCAnimatedEntity;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.Channel;

public class AnimationHandlerGhastlyEnaria extends AnimationHandler
{
	/** Map with all the animations. */
	public static HashMap<String, Channel> animChannels = new HashMap<String, Channel>();
	static
	{
		animChannels.put("dance", new ChannelDance("dance", 30.0F, 300, Channel.LINEAR));
	}

	public AnimationHandlerGhastlyEnaria(IMCAnimatedEntity entity)
	{
		super(entity);
	}

	@Override
	public void activateAnimation(String name, float startingFrame)
	{
		super.activateAnimation(animChannels, name, startingFrame);
	}

	@Override
	public void stopAnimation(String name)
	{
		super.stopAnimation(animChannels, name);
	}
}