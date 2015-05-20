package com.DavidM1A2.AfraidOfTheDark.threads;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.packets.UpdateInsanity;

public class DelayedInsanityUpdate extends DelayedUpdate<Double>
{
	public DelayedInsanityUpdate(EntityPlayer entityPlayer, double data)
	{
		super(entityPlayer, data);
	}

	@Override
	protected void updatePlayer()
	{
		AfraidOfTheDark.getSimpleNetworkWrapper().sendTo(new UpdateInsanity(data), (EntityPlayerMP) entityPlayer);
	}
}
