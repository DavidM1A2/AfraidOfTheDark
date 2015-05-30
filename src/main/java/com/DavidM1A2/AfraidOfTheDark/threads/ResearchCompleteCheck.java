/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.threads;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import com.DavidM1A2.AfraidOfTheDark.playerData.HasStartedAOTD;

public class ResearchCompleteCheck extends Thread
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
				Thread.sleep(1000);

				final List<EntityPlayerMP> allPlayers = MinecraftServer.getServer().getConfigurationManager().playerEntityList;

				for (final EntityPlayerMP player : allPlayers)
				{
					if (HasStartedAOTD.get(player))
					{
						// stuff
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
