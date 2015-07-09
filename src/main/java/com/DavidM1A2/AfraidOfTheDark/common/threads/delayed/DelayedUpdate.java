/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.threads.delayed;

import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import net.minecraft.entity.player.EntityPlayer;

public abstract class DelayedUpdate<E> extends Thread
{
	protected final EntityPlayer entityPlayer;
	protected final E data;
	protected final int delayInMillis;

	public DelayedUpdate(final int delayInMillis, final EntityPlayer entityPlayer, final E data)
	{
		this.entityPlayer = entityPlayer;
		this.data = data;
		this.delayInMillis = delayInMillis;
	}

	@Override
	public void run()
	{
		try
		{
			Thread.sleep(delayInMillis);

			LogHelper.info(this.getClass().getSimpleName());
			this.updatePlayer();
		}
		catch (final InterruptedException e)
		{
		}
	}

	protected abstract void updatePlayer();
}
