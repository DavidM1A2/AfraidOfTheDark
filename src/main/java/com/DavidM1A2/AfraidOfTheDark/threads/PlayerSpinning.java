/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.threads;

import net.minecraft.entity.player.EntityPlayerMP;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.packets.RotatePlayer;

public class PlayerSpinning extends Thread
{
	private final EntityPlayerMP entityPlayerMP;
	private static final int DEGREES_PER_ROTATION = 30;
	private static final int WAIT_BETWEEN_ROTATIONS_IN_MILLIS = 20;

	public PlayerSpinning(final EntityPlayerMP entityPlayerMP)
	{
		this.entityPlayerMP = entityPlayerMP;
	}

	@Override
	public void run()
	{
		for (int i = 0; i < 360 / DEGREES_PER_ROTATION; i++)
		{
			try
			{
				Thread.sleep(WAIT_BETWEEN_ROTATIONS_IN_MILLIS);
			}
			catch (InterruptedException e)
			{
			}

			AfraidOfTheDark.getSimpleNetworkWrapper().sendTo(new RotatePlayer(DEGREES_PER_ROTATION), entityPlayerMP);
		}
	}
}
