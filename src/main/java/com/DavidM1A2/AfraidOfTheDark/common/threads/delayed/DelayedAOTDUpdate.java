/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.threads.delayed;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateAOTDStatus;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class DelayedAOTDUpdate extends DelayedUpdate<Boolean>
{
	public DelayedAOTDUpdate(final int delayInMillis, final EntityPlayer entityPlayer, final boolean data)
	{
		super(delayInMillis, entityPlayer, data);
	}

	@Override
	protected void updatePlayer()
	{
		AfraidOfTheDark.getSimpleNetworkWrapper().sendTo(new UpdateAOTDStatus(this.data), (EntityPlayerMP) this.entityPlayer);
	}
}
