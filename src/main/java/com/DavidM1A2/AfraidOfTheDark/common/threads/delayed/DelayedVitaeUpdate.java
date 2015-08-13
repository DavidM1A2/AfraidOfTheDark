/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.threads.delayed;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateVitae;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class DelayedVitaeUpdate extends DelayedUpdate<Integer>
{
	public DelayedVitaeUpdate(EntityPlayer entityPlayer, Integer data)
	{
		super(entityPlayer, data);
	}

	@Override
	protected void updatePlayer()
	{
		AfraidOfTheDark.getSimpleNetworkWrapper().sendTo(new UpdateVitae(data, entityPlayer.getEntityId()), (EntityPlayerMP) this.entityPlayer);
	}
}
