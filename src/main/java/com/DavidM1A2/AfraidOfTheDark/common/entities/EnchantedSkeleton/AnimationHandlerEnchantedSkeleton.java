package com.DavidM1A2.AfraidOfTheDark.common.entities.EnchantedSkeleton;

import java.util.HashMap;

import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.IMCAnimatedEntity;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.Channel;

public class AnimationHandlerEnchantedSkeleton extends AnimationHandler
{
	/** Map with all the animations. */
	public static HashMap<String, Channel> animChannels = new HashMap<String, Channel>();

	static
	{
		animChannels.put("Walk", new ChannelWalk("Walk", 20.0F, 40, Channel.LINEAR));
		animChannels.put("Attack", new ChannelAttack("Attack", 30.0F, 20, Channel.LINEAR));
		animChannels.put("Spawn", new ChannelSpawn("Spawn", 20.0F, 40, Channel.LINEAR));
		animChannels.put("Idle", new ChannelIdle("Idle", 10.0F, 20, Channel.LOOP));
	}

	public AnimationHandlerEnchantedSkeleton(IMCAnimatedEntity entity)
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

	@Override
	public void fireAnimationEventClientSide(Channel anim, float prevFrame, float frame)
	{
	}

	@Override
	public void fireAnimationEventServerSide(Channel anim, float prevFrame, float frame)
	{
	}
}