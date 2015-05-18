/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.threads;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

import com.DavidM1A2.AfraidOfTheDark.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.research.Research;

public class UpdateReserachAfterDelay extends Thread
{
	private final EntityPlayer playerRefrence;
	private final NBTTagCompound refrence;

	public UpdateReserachAfterDelay(EntityPlayer playerRefrence, NBTTagCompound refrence)
	{
		this.playerRefrence = playerRefrence;
		this.refrence = refrence;
	}

	// After 500ms, update the players research. This allows for the recreation of the EntityPlayer object before applying changes
	@Override
	public void run()
	{
		try
		{
			Thread.sleep(500);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		LoadResearchData.set(playerRefrence, refrence);
		Research research = LoadResearchData.getResearch(playerRefrence);
		for (int i = 0; i < Research.getResearchAmount(); i++)
		{
			if (refrence.getBoolean(LoadResearchData.RESEARCH_DATA + research.getResearches().get(i).getType().toString()))
			{
				Research.unlockResearchSynced(playerRefrence, research.getResearches().get(i).getType(), Side.SERVER);
			}
		}

	}
}
