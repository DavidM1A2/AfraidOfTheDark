/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.playerData;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.packets.UpdateResearch;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;
import com.DavidM1A2.AfraidOfTheDark.refrence.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.utility.LogHelper;

//This property is saved on a player and keeps track of their current researches
public class LoadResearchData implements IExtendedEntityProperties
{
	private NBTTagCompound researches = new NBTTagCompound();;
	public final static String RESEARCH_DATA = "unlockedResearches";

	public static final void register(EntityPlayer player)
	{
		player.registerExtendedProperties(LoadResearchData.RESEARCH_DATA, new LoadResearchData());
	}

	@Override
	public void init(Entity entity, World world)
	{
	}

	// Each player has a byte array representing their completed research
	// save it here
	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		compound.setTag(RESEARCH_DATA, researches);
	}

	// Loading research
	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		researches = (NBTTagCompound) compound.getTag(RESEARCH_DATA);
	}

	/**
	 * Returns ExtendedPlayer properties for player This method is for convenience only; it will make your code look nicer
	 */
	public static final NBTTagCompound get(EntityPlayer entityPlayer)
	{
		return entityPlayer.getEntityData().getCompoundTag(RESEARCH_DATA);
	}

	public static final void set(EntityPlayer entityPlayer, NBTTagCompound compound)
	{
		entityPlayer.getEntityData().setTag(RESEARCH_DATA, compound);
	}

	public static final boolean isResearched(EntityPlayer entityPlayer, ResearchTypes type)
	{
		return entityPlayer.getEntityData().getCompoundTag(RESEARCH_DATA).getBoolean(RESEARCH_DATA + type.toString());
	}

	public static void unlockResearchSynced(EntityPlayer entityPlayer, ResearchTypes type, Side side)
	{
		NBTTagCompound current = get(entityPlayer);
		current.setBoolean(RESEARCH_DATA + type.toString(), true);
		LoadResearchData.set(entityPlayer, current);
		LogHelper.info("Updating research on " + FMLCommonHandler.instance().getSide().toString() + " side.");
		if (side == Side.CLIENT)
		{
			AfraidOfTheDark.getSimpleNetworkWrapper().sendToServer(new UpdateResearch(entityPlayer.getEntityData().getCompoundTag(RESEARCH_DATA)));
			Refrence.researchAchievedOverlay.displayResearch(type, new ItemStack(ModItems.journal, 1), false);
		}
		else
		{
			AfraidOfTheDark.getSimpleNetworkWrapper().sendTo(new UpdateResearch(entityPlayer.getEntityData().getCompoundTag(RESEARCH_DATA)), (EntityPlayerMP) entityPlayer);
		}
	}
}
