/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.handler;

import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBiomes;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.AOTDPlayerData;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class WorldEvents
{
	private int counter = 1;

	@SubscribeEvent
	public void onWorldTickEvent(TickEvent.ServerTickEvent event)
	{
		if (counter % 300 == 0)
		{
			counter = 1;
			updateInsanity();
		}
		else
		{
			counter = counter + 1;
		}
	}

	private void updateInsanity()
	{
		// Loop through
		for (final Object player : MinecraftServer.getServer().getConfigurationManager().playerEntityList)
		{
			final EntityPlayer entityPlayer = (EntityPlayer) player;
			if (AOTDPlayerData.get(entityPlayer).getHasStartedAOTD())
			{
				if (entityPlayer.worldObj.getBiomeGenForCoords(new BlockPos((int) entityPlayer.posX, 0, (int) entityPlayer.posZ)) == ModBiomes.erieForest)
				{
					final double amount = .01 + ((.09) * (new Random().nextDouble()));
					double newInsanity = AOTDPlayerData.get(entityPlayer).getPlayerInsanity() + amount;
					AOTDPlayerData.get(entityPlayer).setPlayerInsanity(newInsanity);
					AOTDPlayerData.get(entityPlayer).syncPlayerInsanity();
				}
				else
				{
					final double amount = .01 + ((.02) * (new Random().nextDouble()));
					double newInsanity = AOTDPlayerData.get(entityPlayer).getPlayerInsanity() - amount;
					AOTDPlayerData.get(entityPlayer).setPlayerInsanity(newInsanity);
					AOTDPlayerData.get(entityPlayer).syncPlayerInsanity();
				}
			}
		}
	}
}
