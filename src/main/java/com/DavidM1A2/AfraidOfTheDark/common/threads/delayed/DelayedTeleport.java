package com.DavidM1A2.AfraidOfTheDark.common.threads.delayed;

import net.minecraft.entity.player.EntityPlayer;

public class DelayedTeleport extends DelayedUpdate<Integer>
{
	public DelayedTeleport(EntityPlayer entityPlayer, Integer data)
	{
		super(entityPlayer, data);
	}

	@Override
	protected void updatePlayer()
	{
		entityPlayer.travelToDimension(data);
	}
}
