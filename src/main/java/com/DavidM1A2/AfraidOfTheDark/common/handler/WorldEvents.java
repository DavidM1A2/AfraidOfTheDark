/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.handler;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDWorldData;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class WorldEvents
{
	private int counter = 1;

	@SubscribeEvent
	public void onWorldTickEvent(TickEvent.ServerTickEvent event)
	{
		if (counter % 400 == 0)
		{
			counter = 1;
			updateInsanity();
		}
		else
		{
			counter = counter + 1;
		}
	}

	@SubscribeEvent
	public void onWorldLoadEvent(WorldEvent.Load event)
	{
		if (!event.world.isRemote)
		{
			AOTDWorldData.register(event.world);
		}
	}

	private void updateInsanity()
	{
		// Loop through
		for (final Object player : MinecraftServer.getServer().getConfigurationManager().playerEntityList)
		{
			final EntityPlayer entityPlayer = (EntityPlayer) player;
			// if (AOTDPlayerData.get(entityPlayer).getHasStartedAOTD())
			// {
			// if (entityPlayer.worldObj.getBiomeGenForCoords(new BlockPos((int)
			// entityPlayer.posX, 0, (int) entityPlayer.posZ)) ==
			// ModBiomes.erieForest)
			// {
			// final double amount = .01 + ((.09) * (new
			// Random().nextDouble()));
			// double newInsanity =
			// AOTDPlayerData.get(entityPlayer).getPlayerInsanity() + amount;
			// AOTDPlayerData.get(entityPlayer).setPlayerInsanity(newInsanity);
			// AOTDPlayerData.get(entityPlayer).syncPlayerInsanity();
			// }
			// else
			// {
			// final double amount = .01 + ((.02) * (new
			// Random().nextDouble()));
			// double newInsanity =
			// AOTDPlayerData.get(entityPlayer).getPlayerInsanity() - amount;
			// AOTDPlayerData.get(entityPlayer).setPlayerInsanity(newInsanity);
			// AOTDPlayerData.get(entityPlayer).syncPlayerInsanity();
			// }
			// }
			entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).setPlayerInsanity(0);
			entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).syncPlayerInsanity();
		}
	}
}
