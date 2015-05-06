/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.threads;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModBiomes;
import com.DavidM1A2.AfraidOfTheDark.playerData.Insanity;

// Over time the player becomes more sane. If the player is in the middle of nowhere, decrease insanity, else if he is in an erie forest, increase insanity
public class RandomInsanityUpdate extends Thread
{
	@Override
	public void run()
	{
		try
		{
			// This thread runs while the player is connected
			while (true)
			{
				// Sleep a random amount of time
				this.sleep((long) ((new Random()).nextDouble() * 10000));

				// Loop through
				for (Object player : MinecraftServer.getServer().getConfigurationManager().playerEntityList)
				{
					EntityPlayer entityPlayer = (EntityPlayer) player;
					if (entityPlayer.worldObj.getBiomeGenForCoords((int) entityPlayer.posX, (int) entityPlayer.posZ) == ModBiomes.erieForest)
					{
						double amount = .01 + (.09) * (new Random().nextDouble());
						Insanity.increaseInsanity(amount, entityPlayer);
					}
					else
					{
						double amount = .01 + (.02) * (new Random().nextDouble());
						Insanity.decreaseInsanity(amount, entityPlayer);
					}
				}
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
