/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.threads;

import net.minecraft.entity.player.EntityPlayer;

public class TemporaryInvincibility implements Runnable
{
	private final int INVINCIBILITY_TIME;
	private final EntityPlayer ENTITY_PLAYER;

	public TemporaryInvincibility(int invincibilityTime, EntityPlayer entityPlayer)
	{
		this.INVINCIBILITY_TIME = invincibilityTime;
		this.ENTITY_PLAYER = entityPlayer;
	}

	@Override
	public void run()
	{
		this.ENTITY_PLAYER.capabilities.disableDamage = true;
		try
		{
			Thread.sleep(INVINCIBILITY_TIME);
		}
		catch (InterruptedException e)
		{
		}

		this.ENTITY_PLAYER.capabilities.disableDamage = false;
	}
}
