/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.playerData;

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
import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateResearch;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

//This property is saved on a player and keeps track of their current researches
public class Research implements IExtendedEntityProperties
{
	private NBTTagCompound researches = new NBTTagCompound();;
	public final static String RESEARCH_DATA = "unlockedResearches";

	public static final void register(final EntityPlayer player)
	{
		player.registerExtendedProperties(Research.RESEARCH_DATA, new Research());
	}

	@Override
	public void init(final Entity entity, final World world)
	{
	}

	// Each player has a byte array representing their completed research
	// save it here
	@Override
	public void saveNBTData(final NBTTagCompound compound)
	{
		compound.setTag(Research.RESEARCH_DATA, this.researches);
	}

	// Loading research
	@Override
	public void loadNBTData(final NBTTagCompound compound)
	{
		this.researches = (NBTTagCompound) compound.getTag(Research.RESEARCH_DATA);
	}

	/**
	 * Returns ExtendedPlayer properties for player This method is for convenience only; it will make your code look nicer
	 */
	public static final NBTTagCompound get(final EntityPlayer entityPlayer)
	{
		return entityPlayer.getEntityData().getCompoundTag(Research.RESEARCH_DATA);
	}

	public static final void set(final EntityPlayer entityPlayer, final NBTTagCompound compound)
	{
		entityPlayer.getEntityData().setTag(Research.RESEARCH_DATA, compound);
	}

	public static final boolean isResearched(final EntityPlayer entityPlayer, final ResearchTypes type)
	{
		if (type == null)
		{
			return true;
		}
		return entityPlayer.getEntityData().getCompoundTag(Research.RESEARCH_DATA).getBoolean(Research.RESEARCH_DATA + type.toString());
	}

	public static void unlockResearchSynced(final EntityPlayer entityPlayer, final ResearchTypes type, final Side side, boolean firstTimeResearched)
	{
		final NBTTagCompound current = Research.get(entityPlayer);
		current.setBoolean(Research.RESEARCH_DATA + type.toString(), true);
		Research.set(entityPlayer, current);
		LogHelper.info("Updating research on " + FMLCommonHandler.instance().getSide().toString() + " side.");
		if (side == Side.CLIENT)
		{
			AfraidOfTheDark.getSimpleNetworkWrapper().sendToServer(new UpdateResearch(entityPlayer.getEntityData().getCompoundTag(Research.RESEARCH_DATA), firstTimeResearched));
			if (firstTimeResearched)
			{
				ClientData.researchAchievedOverlay.displayResearch(type, new ItemStack(ModItems.journal, 1), false);
			}
		}
		else
		{
			AfraidOfTheDark.getSimpleNetworkWrapper().sendTo(new UpdateResearch(entityPlayer.getEntityData().getCompoundTag(Research.RESEARCH_DATA), firstTimeResearched), (EntityPlayerMP) entityPlayer);
		}
	}

	public static boolean canResearch(final EntityPlayer entityPlayer, final ResearchTypes type)
	{
		return HasStartedAOTD.get(entityPlayer) && !isResearched(entityPlayer, type) && isResearched(entityPlayer, type.getPrevious());
	}
}
