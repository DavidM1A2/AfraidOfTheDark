/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.threads.delayed;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateResearch;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class DelayedResearchUpdate extends DelayedUpdate<NBTTagCompound>
{
	public DelayedResearchUpdate(final EntityPlayer entityPlayer, final NBTTagCompound data)
	{
		super(entityPlayer, data);
	}

	@Override
	protected void updatePlayer()
	{
		AfraidOfTheDark.getPacketHandler().sendTo(new UpdateResearch(this.data), (EntityPlayerMP) this.entityPlayer);
	}
}
