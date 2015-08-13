/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.threads.delayed;

import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.Teleporter;

public class DelayedTeleport extends DelayedUpdate<Integer>
{
	private final Class<? extends Teleporter> teleporter;

	public DelayedTeleport(EntityPlayer entityPlayer, Integer data, Class<? extends Teleporter> teleporter)
	{
		super(entityPlayer, data);
		this.teleporter = teleporter;
	}

	@Override
	protected void updatePlayer()
	{
		Utility.sendPlayerToDimension((EntityPlayerMP) entityPlayer, data, false, teleporter);
	}
}
