package com.DavidM1A2.AfraidOfTheDark.threads;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.entities.WereWolf.EntityWereWolf;
import com.DavidM1A2.AfraidOfTheDark.packets.UpdateResearch;
import com.DavidM1A2.AfraidOfTheDark.playerData.HasStartedAOTD;
import com.DavidM1A2.AfraidOfTheDark.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.research.ResearchTypes;

public class ResearchCompleteCheck extends Thread
{
	private boolean running = true;

	@Override
	public void run()
	{
		try
		{
			// This thread runs while the player is connected
			while (running)
			{
				Thread.sleep(1000);

				List<EntityPlayerMP> allPlayers = MinecraftServer.getServer().getConfigurationManager().playerEntityList;

				for (EntityPlayerMP player : allPlayers)
				{
					if (HasStartedAOTD.get(player))
					{
						if (LoadResearchData.get(player).isPreviousResearched(ResearchTypes.WerewolfExamination))
						{
							if (!LoadResearchData.get(player).isUnlocked(ResearchTypes.WerewolfExamination))
							{
								for (Object entity : player.worldObj.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().expand(15, 15, 15)))
								{
									if (entity instanceof EntityWereWolf)
									{
										LoadResearchData.get(player).unlockResearch(ResearchTypes.WerewolfExamination);
										LoadResearchData.setSingleResearch(player, 0, true);
										AfraidOfTheDark.getSimpleNetworkWrapper().sendTo(new UpdateResearch(1, true), player);
									}
								}
							}
						}
					}
				}
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public void terminate()
	{
		this.running = false;
	}
}
