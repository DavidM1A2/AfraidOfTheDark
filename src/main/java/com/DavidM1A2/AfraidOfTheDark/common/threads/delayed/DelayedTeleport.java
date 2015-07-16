/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.threads.delayed;

import com.DavidM1A2.AfraidOfTheDark.common.dimension.nightmare.NightmareTeleporter;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class DelayedTeleport extends DelayedUpdate<Integer>
{
	public DelayedTeleport(final int delayInMillis, EntityPlayer entityPlayer, Integer data)
	{
		super(delayInMillis, entityPlayer, data);
	}

	@Override
	protected void updatePlayer()
	{
		Utility.sendPlayerToDimension((EntityPlayerMP) entityPlayer, data, false, NightmareTeleporter.class);
	}
}
