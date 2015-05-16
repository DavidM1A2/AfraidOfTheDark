package com.DavidM1A2.AfraidOfTheDark.threads;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
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

				List<EntityPlayer> allPlayers = MinecraftServer.getServer().getConfigurationManager().playerEntityList;

				for (EntityPlayer player : allPlayers)
				{
					if (HasStartedAOTD.get(player))
					{
						if (false)
						{
							if (LoadResearchData.get(player).isPreviousResearched(ResearchTypes.WerewolfExamination))
							{
								if (!LoadResearchData.get(player).isUnlocked(ResearchTypes.WerewolfExamination))
								{
									LoadResearchData.get(player).unlockResearch(ResearchTypes.WerewolfExamination);
									LoadResearchData.setSingleResearch(player, 0, true);
									AfraidOfTheDark.getSimpleNetworkWrapper().sendToServer(new UpdateResearch(1, true));
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
