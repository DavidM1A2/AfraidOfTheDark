/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.threads;

import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBiomes;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.Insanity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

// Over time the player becomes more sane. If the player is in the middle of nowhere, decrease insanity, else if he is in an erie forest, increase
// insanity
public class RandomInsanityUpdate extends Thread
{
	private boolean running = true;

	@Override
	public void run()
	{
		try
		{
			// This thread runs while the player is connected
			while (this.running)
			{
				// Sleep a random amount of time
				Thread.sleep((long) ((new Random()).nextDouble() * 10000));

				// Loop through
				for (final Object player : MinecraftServer.getServer().getConfigurationManager().playerEntityList)
				{
					final EntityPlayer entityPlayer = (EntityPlayer) player;
					if (entityPlayer.worldObj.getBiomeGenForCoords(new BlockPos((int) entityPlayer.posX, 0, (int) entityPlayer.posZ)) == ModBiomes.erieForest)
					{
						final double amount = .01 + ((.09) * (new Random().nextDouble()));
						Insanity.addInsanity(amount, entityPlayer);
					}
					else
					{
						final double amount = .01 + ((.02) * (new Random().nextDouble()));
						Insanity.addInsanity(-amount, entityPlayer);
					}
				}
			}
		}
		catch (final InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public void terminate()
	{
		this.running = false;
	}
}
