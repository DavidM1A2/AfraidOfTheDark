/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.handler;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldEvents
{
	@SubscribeEvent
	public void constantNightmareWorldRain(final WorldEvent event)
	{
		World world = event.world;
		if (!world.isRemote)
		{
			WorldInfo worldInfo = world.getWorldInfo();
			if (world.provider.getDimensionId() == Constants.NightmareWorld.NIGHTMARE_WORLD_ID)
			{
				if (!worldInfo.isRaining())
				{
					MinecraftServer.getServer().getCommandManager().executeCommand(MinecraftServer.getServer(), "/weather rain");
				}
			}
		}
	}
}
