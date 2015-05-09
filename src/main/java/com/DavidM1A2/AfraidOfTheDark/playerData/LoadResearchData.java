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

import com.DavidM1A2.AfraidOfTheDark.research.Research;

public class LoadResearchData implements IExtendedEntityProperties
{
	private Research myResearch = new Research();
	private boolean[] unlockedResearches = new boolean[myResearch.getResearches().size()];
	private final static String RESEARCH_DATA = "unlockedResearches:";
	private final EntityPlayer entityPlayer;

	public LoadResearchData(EntityPlayer entityPlayer)
	{
		this.entityPlayer = entityPlayer;
	}

	public static final void register(EntityPlayer player)
	{
		player.registerExtendedProperties(LoadResearchData.RESEARCH_DATA, new LoadResearchData(player));
	}

	/**
	 * Returns ExtendedPlayer properties for player This method is for convenience only; it will make your code look nicer
	 */
	public static final LoadResearchData get(EntityPlayer player)
	{
		return (LoadResearchData) player.getExtendedProperties(RESEARCH_DATA);
	}

	// Each player has a byte array representing their completed research
	// save it here
	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		NBTTagCompound researches = new NBTTagCompound();
		for (int i = 0; i < myResearch.getResearches().size(); i++)
		{
			researches.setBoolean(RESEARCH_DATA + myResearch.getResearches().get(i).getNodeID(), unlockedResearches[i]);
		}
		compound.setTag(RESEARCH_DATA, researches);
	}

	// Loading research
	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		NBTTagCompound researches = (NBTTagCompound) compound.getTag(RESEARCH_DATA);
		for (int i = 0; i < researches.getKeySet().size(); i++)
		{
			if (researches.getBoolean(RESEARCH_DATA + myResearch.getResearches().get(i).getNodeID()))
			{
				this.unlockedResearches[i] = true;
				myResearch.unlockResearch(i);
			}
			else
			{
				this.unlockedResearches[i] = false;
			}
		}
	}

	// Init for new players that don't have this IExtendedPropertyYet
	@Override
	public void init(Entity entity, World world)
	{
	}

	public void setResearch(int index, boolean isResearched)
	{
		this.unlockedResearches[index] = isResearched;
		if (isResearched)
		{
			myResearch.unlockResearch(index);
		}
	}

	public Research getResearch()
	{
		return this.myResearch;
	}
}
