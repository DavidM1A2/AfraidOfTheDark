package com.DavidM1A2.AfraidOfTheDark.common.entities.SplinterDrone;

import java.util.HashMap;

import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.IMCAnimatedEntity;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.Channel;

public class AnimationHandlerSplinterDroneProjectile extends AnimationHandler
{
	/** Map with all the animations. */
	public static HashMap<String, Channel> animChannels = new HashMap<String, Channel>();

	static
	{
		animChannels.put("Sping", new ChannelSping("Sping", 100.0F, 100, Channel.LINEAR));
	}

	public AnimationHandlerSplinterDroneProjectile(IMCAnimatedEntity entity)
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