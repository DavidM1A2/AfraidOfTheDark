package com.DavidM1A2.AfraidOfTheDark.threads;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModBiomes;
import com.DavidM1A2.AfraidOfTheDark.playerData.Insanity;
import com.DavidM1A2.AfraidOfTheDark.utility.LogHelper;

// Over time the player becomes more sane. If the player is in the middle of nowhere, decrease insanity, else if he is in an erie forest, increase insanity

public class RandomInsanityUpdate extends Thread
{
	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				this.sleep((long) ((new Random()).nextDouble() * 100000));

				LogHelper.info("Updating");

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
