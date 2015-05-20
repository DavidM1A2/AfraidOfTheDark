package com.DavidM1A2.AfraidOfTheDark.threads;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.packets.UpdateAOTDStatus;

public class DelayedAOTDUpdate extends DelayedUpdate<Boolean>
{
	public DelayedAOTDUpdate(EntityPlayer entityPlayer, boolean data)
	{
		super(entityPlayer, data);
	}

	@Override
	protected void updatePlayer()
	{
		AfraidOfTheDark.getSimpleNetworkWrapper().sendTo(new UpdateAOTDStatus(data), (EntityPlayerMP) entityPlayer);
	}
}
