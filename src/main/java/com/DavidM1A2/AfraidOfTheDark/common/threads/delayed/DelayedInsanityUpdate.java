/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.threads.delayed;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateInsanity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class DelayedInsanityUpdate extends DelayedUpdate<Double>
{
	public DelayedInsanityUpdate(final EntityPlayer entityPlayer, final double data)
	{
		super(entityPlayer, data);
	}

	@Override
	protected void updatePlayer()
	{
		AfraidOfTheDark.getSimpleNetworkWrapper().sendTo(new UpdateInsanity(this.data), (EntityPlayerMP) this.entityPlayer);
	}
}
