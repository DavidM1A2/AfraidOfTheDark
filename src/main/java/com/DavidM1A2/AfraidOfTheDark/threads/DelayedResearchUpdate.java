package com.DavidM1A2.AfraidOfTheDark.threads;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.packets.UpdateResearch;

public class DelayedResearchUpdate extends DelayedUpdate<NBTTagCompound>
{
	public DelayedResearchUpdate(EntityPlayer entityPlayer, NBTTagCompound data)
	{
		super(entityPlayer, data);
	}

	@Override
	protected void updatePlayer()
	{
		AfraidOfTheDark.getSimpleNetworkWrapper().sendTo(new UpdateResearch(data), (EntityPlayerMP) entityPlayer);
	}
}
