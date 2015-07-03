/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.handler;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.DavidM1A2.AfraidOfTheDark.common.entities.Werewolf.EntityWerewolf;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.HasStartedAOTD;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;

public class WorldEvents
{
	private boolean readyToSpawnWerewolves = true;

	// At midnight, spawn werewolves
	@SubscribeEvent
	public void midnightWerewolves(final WorldEvent event)
	{
		final World currentWorld = event.world;
		// Only in the overworld
		if (currentWorld.provider.getDimensionId() == 0)
		{
			// Full moon only
			if (currentWorld.getCurrentMoonPhaseFactor() == 1.0F)
			{
				// Current world time is total world time - (24,000 ticks * number of days passed)
				final long daysPassed = currentWorld.getWorldTime() / 24000L;
				final long worldTime = currentWorld.getWorldInfo().getWorldTime() - (24000L * daysPassed);
				// If it is 12oClock
				if ((worldTime > 16500) && (worldTime < 16700))
				{
					// If we are ready to spawn werewolves
					if (this.readyToSpawnWerewolves)
					{
						// Set werewolf attributes and update each already spawned werewolf
						EntityWerewolf.setMoveSpeedAndAgroRange(.51, 80.0D, 120.0D);
						for (final Object entity : currentWorld.loadedEntityList)
						{
							if (entity instanceof EntityWerewolf)
							{
								final EntityWerewolf myWolf = (EntityWerewolf) entity;
								myWolf.setWanderer();
								myWolf.setMyWatchClosest();
							}
						}

						// Tell each player that has started AOTD that it's "the night"
						final List<EntityPlayer> players = currentWorld.playerEntities;
						for (final EntityPlayer entityPlayer : players)
						{
							if (HasStartedAOTD.get(entityPlayer))
							{
								if (currentWorld.isRemote)
								{
									entityPlayer.addChatMessage(new ChatComponentText("§4§oSomething §4§ofeels §4§odifferent §4§otonight..."));
								}
							}
						}
						this.readyToSpawnWerewolves = false;
					}
				}
				// If the world time is between 23500 and 23700 (dawn) reset werewolf attributes and update each werewolf
				else if ((worldTime > 23500) && (worldTime < 23700))
				{
					if (!this.readyToSpawnWerewolves)
					{
						EntityWerewolf.setMoveSpeedAndAgroRange(.38, 16.0D, 32.0D);
						for (final Object entity : currentWorld.loadedEntityList)
						{
							if (entity instanceof EntityWerewolf)
							{
								final EntityWerewolf myWolf = (EntityWerewolf) entity;
								myWolf.setWanderer();
								myWolf.setMyWatchClosest();
							}
						}

						// Tell each player that the event is over
						final List<EntityPlayer> players = currentWorld.playerEntities;
						for (final EntityPlayer entityPlayer : players)
						{
							if (HasStartedAOTD.get(entityPlayer))
							{
								if (currentWorld.isRemote)
								{
									entityPlayer.addChatMessage(new ChatComponentText("§2I §2feel §2better §2some §2how."));
								}
							}
						}
						this.readyToSpawnWerewolves = true;
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void constantNightmareWorldRain(final WorldEvent event)
	{
		World world = event.world;
		if (world.isRemote)
		{
			if (world.provider.getDimensionId() == Constants.NightmareWorld.NIGHTMARE_WORLD_ID)
			{
				if (!world.isRaining())
				{
					world.setRainStrength(1.0F);
					world.setThunderStrength(1.0F);
				}
			}
		}
	}
}
