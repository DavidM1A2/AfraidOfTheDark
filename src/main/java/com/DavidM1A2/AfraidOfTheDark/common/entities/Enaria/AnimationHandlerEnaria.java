/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria;

import java.util.HashMap;

import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.IMCAnimatedEntity;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.Channel;

public class AnimationHandlerEnaria extends AnimationHandler
{
	/** Map with all the animations. */
	public static HashMap<String, Channel> animChannels = new HashMap<String, Channel>();

	static
	{
		animChannels.put("walk", new ChannelWalk("walk", 59.0F, 59, Channel.LINEAR));
		animChannels.put("armthrow", new ChannelArmthrow("armthrow", 61.0F, 61, Channel.LINEAR));
		animChannels.put("autoattack", new ChannelAutoattack("autoattack", 70.0F, 51, Channel.LINEAR));
		animChannels.put("spell", new ChannelSpell("spell", 90.0F, 121, Channel.LINEAR));
	}

	public AnimationHandlerEnaria(IMCAnimatedEntity entity)
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