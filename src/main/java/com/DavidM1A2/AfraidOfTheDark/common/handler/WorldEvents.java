/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.handler;

import com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria.ghastly.EntityGhastlyEnaria;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDDimensions;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDWorldData;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class WorldEvents
{
	private int counter = 1;

	// When a "Ghastly Enaria" is unloaded kill her, and then respawn her next to the closest player if there is a closest player
	@SubscribeEvent
	public void onChunkUnloadEvent(ChunkEvent.Unload event)
	{
		if (event.world.provider.getDimensionId() == AOTDDimensions.Nightmare.getWorldID())
			if (!event.world.isRemote)
			{
				EntityGhastlyEnaria newEnaria = null;
				for (ClassInheritanceMultiMap<Entity> entityMap : event.getChunk().getEntityLists())
				{
					for (Entity entity : entityMap)
					{
						if (entity instanceof EntityGhastlyEnaria)
						{
							EntityPlayer entityPlayer = event.world.getClosestPlayer(event.getChunk().xPosition * 16, 100, event.getChunk().zPosition * 16, AOTDDimensions.getBlocksBetweenIslands() / 2);
							entity.onKillCommand();
							if (entityPlayer != null && !entityPlayer.isDead)
							{
								int offsetX = entityPlayer.getRNG().nextBoolean() ? Utility.randInt(-50, -25) : Utility.randInt(25, 50);
								int offsetZ = entityPlayer.getRNG().nextBoolean() ? Utility.randInt(-50, -25) : Utility.randInt(25, 50);
								int posX = entityPlayer.getPosition().getX() + offsetX;
								int posZ = entityPlayer.getPosition().getZ() + offsetZ;
								newEnaria = new EntityGhastlyEnaria(event.world);
								newEnaria.setBenign(!entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.Enaria));
								newEnaria.setPosition(posX, entityPlayer.posY, posZ);
							}
						}
					}
				}
				if (newEnaria != null)
					event.world.spawnEntityInWorld(newEnaria);
			}
	}

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
