/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.threads;

import net.minecraft.entity.player.EntityPlayer;

import com.DavidM1A2.AfraidOfTheDark.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.research.Research;

public class UpdateReserachAfterDelay extends Thread
{
	private final EntityPlayer playerRefrence;
	private final Research refrence;

	public UpdateReserachAfterDelay(EntityPlayer playerRefrence, Research refrence)
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
	}
}
