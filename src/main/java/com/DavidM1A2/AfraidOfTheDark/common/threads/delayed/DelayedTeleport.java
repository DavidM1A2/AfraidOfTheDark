/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.threads.delayed;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

public class DelayedTeleport extends DelayedUpdate<Integer>
{
	public DelayedTeleport(EntityPlayer entityPlayer, Integer data)
	{
		super(entityPlayer, data);
	}

	@Override
	protected void updatePlayer()
	{
		Utility.sendPlayerToDimension((EntityPlayerMP) entityPlayer, data, false);
	}
}
