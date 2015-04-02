package com.DavidM1A2.AfraidOfTheDark.handler;

/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

import com.DavidM1A2.AfraidOfTheDark.entities.WereWolf.EntityWereWolf;
import com.DavidM1A2.AfraidOfTheDark.playerData.HasStartedAOTD;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class WorldEvents
{
	private boolean readyToSpawnWerewolves = true;

	// At midnight, spawn werewolves
	@SubscribeEvent
	public void midnightWerewolves(WorldEvent event)
	{
		World currentWorld = event.world;
		// Only in the overworld
		if (currentWorld.provider.dimensionId == 0)
		{
			// Full moon only
			if (currentWorld.getCurrentMoonPhaseFactor() == 1.0F)
			{
				// Current world time is total world time - (24,000 ticks * number of days passed)
				long daysPassed = currentWorld.getWorldTime() / 24000L;
				long worldTime = currentWorld.getWorldInfo().getWorldTime() - (24000L * daysPassed);
				// If it is 12oClock
				if (worldTime > 16500 && worldTime < 16700)
				{
					// If we are ready to spawn werewolves
					if (readyToSpawnWerewolves)
					{
						EntityWereWolf.setMoveSpeedAndAgroRange(.51, 80.0D, 120.0D);
						for (Object entity : currentWorld.loadedEntityList)
						{
							if (entity instanceof EntityWereWolf)
							{
								EntityWereWolf myWolf = (EntityWereWolf) entity;
								myWolf.setWanderer();
								myWolf.setMyWatchClosest();
							}
						}

						List<EntityPlayer> players = currentWorld.playerEntities;
						for (EntityPlayer entityPlayer : players)
						{
							if (HasStartedAOTD.get(entityPlayer))
							{
								entityPlayer.addChatMessage(new ChatComponentText("§4§oSomething §4§ofeels §4§odifferent §4§otonight..."));
							}
						}
						readyToSpawnWerewolves = false;
					}
				}
				else if (worldTime > 23500 && worldTime < 23700)
				{
					if (!readyToSpawnWerewolves)
					{
						EntityWereWolf.setMoveSpeedAndAgroRange(.38, 16.0D, 32.0D);
						for (Object entity : currentWorld.loadedEntityList)
						{
							if (entity instanceof EntityWereWolf)
							{
								EntityWereWolf myWolf = (EntityWereWolf) entity;
								myWolf.setWanderer();
								myWolf.setMyWatchClosest();
							}
						}

						List<EntityPlayer> players = currentWorld.playerEntities;
						for (EntityPlayer entityPlayer : players)
						{
							if (HasStartedAOTD.get(entityPlayer))
							{
								entityPlayer.addChatMessage(new ChatComponentText("§2I §2feel §2better §2some §2how."));
							}
						}
						readyToSpawnWerewolves = true;
					}
				}
			}
		}
	}
}
