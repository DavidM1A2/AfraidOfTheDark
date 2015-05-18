/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
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
	public final static String RESEARCH_DATA = "unlockedResearches";
	private NBTTagCompound researches;

	public static final void register(EntityPlayer player)
	{
		player.registerExtendedProperties(LoadResearchData.RESEARCH_DATA, new LoadResearchData());
	}

	// Each player has a byte array representing their completed research
	// save it here
	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		researches = new NBTTagCompound();
		for (int i = 0; i < myResearch.getResearches().size(); i++)
		{
			researches.setBoolean(RESEARCH_DATA + myResearch.getResearches().get(i).getType().toString(), myResearch.getResearches().get(i).isResearched());
		}
		compound.setTag(RESEARCH_DATA, researches);
	}

	// Loading research
	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		researches = (NBTTagCompound) compound.getTag(RESEARCH_DATA);
		for (int i = 0; i < researches.getKeySet().size(); i++)
		{
			if (researches.getBoolean(RESEARCH_DATA + myResearch.getResearches().get(i).getType().toString()))
			{
				myResearch.unlockResearch(myResearch.getResearches().get(i).getType());
			}
		}
	}

	// Init for new players that don't have this IExtendedPropertyYet
	@Override
	public void init(Entity entity, World world)
	{
	}

	public static Research getResearch(EntityPlayer entityPlayer)
	{
		return ((LoadResearchData) entityPlayer.getExtendedProperties(RESEARCH_DATA)).myResearch;
	}

	public static final void set(EntityPlayer entityPlayer, NBTTagCompound compound)
	{
		entityPlayer.getEntityData().setTag(RESEARCH_DATA, compound);
		Research playerResearch = getResearch(entityPlayer);
		for (int i = 0; i < playerResearch.getResearches().size(); i++)
		{
			if (compound.getBoolean(RESEARCH_DATA + playerResearch.getResearches().get(i).getType().toString()))
			{
				playerResearch.unlockResearch(playerResearch.getResearches().get(i).getType());
			}
		}
	}

	/**
	 * Returns ExtendedPlayer properties for player This method is for convenience only; it will make your code look nicer
	 */
	public static final NBTTagCompound get(EntityPlayer entityPlayer)
	{
		return entityPlayer.getEntityData().getCompoundTag(RESEARCH_DATA);
	}

}
