package com.DavidM1A2.AfraidOfTheDark.threads;

import net.minecraft.entity.player.EntityPlayer;

public class TemporaryInvincibility extends Thread
{
	private final int INVINCIBILITY_TIME;
	private final EntityPlayer ENTITY_PLAYER;

	public TemporaryInvincibility(int invincibilityTime, EntityPlayer entityPlayer)
	{
		this.INVINCIBILITY_TIME = invincibilityTime;
		this.ENTITY_PLAYER = entityPlayer;
		this.ENTITY_PLAYER.capabilities.disableDamage = true;
		this.start();
	}

	@Override
	public void run()
	{
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
