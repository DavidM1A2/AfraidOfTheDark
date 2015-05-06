/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.playerData;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

public class LoadResearchData implements IExtendedEntityProperties
{
	// A byte array represents all of our unlocked researches
	private byte[] unlockedResearches;
	private final static String RESEARCH_DATA = "unlockedResearches";

	// Each player has a byte array representing their completed research
	// save it here
	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		unlockedResearches = Refrence.myResearch.unlockedResearches();
		for (int i = 0; i < unlockedResearches.length; i++)
		{
			compound.setByteArray(RESEARCH_DATA, unlockedResearches);
		}
	}

	// Loading research
	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		unlockedResearches = compound.getByteArray(RESEARCH_DATA);
		for (int i = 0; i < unlockedResearches.length; i++)
		{
			if (unlockedResearches[i] == 1)
			{
				Refrence.myResearch.unlockResearch(i);
			}
		}
	}

	// Init for new players that don't have this IExtendedPropertyYet
	@Override
	public void init(Entity entity, World world)
	{
		loadNBTData(entity.getEntityData());
	}

	// Getters and setters
	public static byte[] get(EntityPlayer myPlayer)
	{
		return myPlayer.getEntityData().getByteArray(RESEARCH_DATA);
	}

	public static void set(EntityPlayer myPlayer, byte[] unlockedResearch)
	{
		myPlayer.getEntityData().setByteArray(RESEARCH_DATA, unlockedResearch);
	}
}
