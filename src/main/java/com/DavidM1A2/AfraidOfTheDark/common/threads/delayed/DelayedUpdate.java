/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.threads.delayed;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import net.minecraft.entity.player.EntityPlayer;

public abstract class DelayedUpdate<E> implements Runnable
{
	protected final EntityPlayer entityPlayer;
	protected final E data;

	public DelayedUpdate(final EntityPlayer entityPlayer, final E data)
	{
		this.entityPlayer = entityPlayer;
		this.data = data;
	}

	@Override
	public void run()
	{
		if (Constants.isDebug)
		{
			LogHelper.info(this.getClass().getSimpleName());
		}

		this.updatePlayer();
	}

	protected abstract void updatePlayer();
}
