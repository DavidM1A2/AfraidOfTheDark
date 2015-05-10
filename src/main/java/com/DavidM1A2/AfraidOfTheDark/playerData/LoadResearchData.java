/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.playerData;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.packets.UpdateResearch;
import com.DavidM1A2.AfraidOfTheDark.research.Research;

public class LoadResearchData implements IExtendedEntityProperties
{
	private Research myResearch = new Research();
	private final static String RESEARCH_DATA = "unlockedResearches";

	public static final void register(EntityPlayer player)
	{
		player.registerExtendedProperties(LoadResearchData.RESEARCH_DATA, new LoadResearchData());
	}

	// Each player has a byte array representing their completed research
	// save it here
	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		NBTTagCompound researches = new NBTTagCompound();
		for (int i = 0; i < myResearch.getResearches().size(); i++)
		{
			researches.setBoolean(RESEARCH_DATA + myResearch.getResearches().get(i).getNodeID(), myResearch.getResearches().get(i).isResearched());
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
				myResearch.unlockResearch(i);
			}
		}

	}

	// Init for new players that don't have this IExtendedPropertyYet
	@Override
	public void init(Entity entity, World world)
	{
	}

	public static void setSingleResearch(EntityPlayer entityPlayer, int index, boolean isResearched)
	{
		Research data = LoadResearchData.get(entityPlayer);
		if (isResearched)
		{
			data.unlockResearch(index);
			NBTTagCompound researches = entityPlayer.getEntityData().getCompoundTag(RESEARCH_DATA);
			researches.setBoolean(RESEARCH_DATA + data.getResearches().get(index).getNodeID(), data.getResearches().get(index).isResearched());
			entityPlayer.getEntityData().setTag(RESEARCH_DATA, researches);
		}
	}

	public static final void set(EntityPlayer entityPlayer, Research research)
	{
		((LoadResearchData) entityPlayer.getExtendedProperties(RESEARCH_DATA)).myResearch = research;
		NBTTagCompound researches = entityPlayer.getEntityData().getCompoundTag(RESEARCH_DATA);
		for (int i = 0; i < Research.getResearchAmount(); i++)
		{
			researches.setBoolean(RESEARCH_DATA + research.getResearches().get(i).getNodeID(), research.getResearches().get(i).isResearched());
			AfraidOfTheDark.getSimpleNetworkWrapper().sendTo(new UpdateResearch(i, research.getResearches().get(i).isResearched()), (EntityPlayerMP) entityPlayer);
		}
		entityPlayer.getEntityData().setTag(RESEARCH_DATA, researches);
	}

	/**
	 * Returns ExtendedPlayer properties for player This method is for convenience only; it will make your code look nicer
	 */
	public static final Research get(EntityPlayer entityPlayer)
	{
		return ((LoadResearchData) entityPlayer.getExtendedProperties(RESEARCH_DATA)).myResearch;
	}

}
