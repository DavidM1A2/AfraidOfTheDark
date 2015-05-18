/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.research;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.packets.UpdateResearch;
import com.DavidM1A2.AfraidOfTheDark.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;
import com.DavidM1A2.AfraidOfTheDark.utility.LogHelper;

public class Research
{
	// Array list of various researches available to the user
	private List<ResearchNode> researches = new ArrayList<ResearchNode>()
	{
		{
			add(new ResearchNode(ResearchTypes.AnUnbreakableCovenant, null));
			add(new ResearchNode(ResearchTypes.WerewolfExamination, ResearchTypes.AnUnbreakableCovenant));
			add(new ResearchNode(ResearchTypes.Crossbow, null));
			add(new ResearchNode(ResearchTypes.Astronomy1, ResearchTypes.WerewolfExamination));
			add(new ResearchNode(ResearchTypes.AstralSilver, ResearchTypes.Astronomy1));
			add(new ResearchNode(ResearchTypes.SilverInfusion, ResearchTypes.AstralSilver));
			add(new ResearchNode(ResearchTypes.DarkForest, ResearchTypes.AstralSilver));
			add(new ResearchNode(ResearchTypes.Astronomy2, ResearchTypes.DarkForest));
		}
	};

	// Given a type we can unlock a research by setting the node
	public void unlockResearch(ResearchTypes type)
	{
		for (int i = 0; i < researches.size(); i++)
		{
			if (researches.get(i).getType() == type)
			{
				researches.get(i).unlockNode();
			}
		}
	}

	public boolean isUnlocked(ResearchTypes type)
	{
		for (int i = 0; i < researches.size(); i++)
		{
			if (researches.get(i).getType() == type)
			{
				return researches.get(i).isResearched();
			}
		}
		return false;
	}

	public List<ResearchNode> getResearches()
	{
		return this.researches;
	}

	public boolean isPreviousResearched(ResearchTypes type)
	{
		if (getResearch(type).getPrevious() == null)
		{
			return false;
		}
		return getResearch(getResearch(type).getPrevious()).isResearched();
	}

	public ResearchNode getResearch(ResearchTypes type)
	{
		for (int i = 0; i < researches.size(); i++)
		{
			if (researches.get(i).getType() == type)
			{
				return researches.get(i);
			}
		}
		return null;
	}

	public static int getResearchAmount()
	{
		return 8;
	}

	public String toString()
	{
		String toReturn = "\n";
		for (int i = 0; i < this.getResearches().size(); i++)
		{
			toReturn = toReturn + this.getResearches().get(i).toString() + "\n";
		}
		return toReturn;
	}

	public static void unlockResearchSynced(EntityPlayer entityPlayer, ResearchTypes type, Side side)
	{
		NBTTagCompound current = entityPlayer.getEntityData().getCompoundTag(LoadResearchData.RESEARCH_DATA);
		current.setBoolean(LoadResearchData.RESEARCH_DATA + type.toString(), true);
		LoadResearchData.set(entityPlayer, current);
		LogHelper.info("Updating research on " + FMLCommonHandler.instance().getSide().toString() + " side.");
		if (side == Side.CLIENT)
		{
			AfraidOfTheDark.getSimpleNetworkWrapper().sendToServer(new UpdateResearch(entityPlayer.getEntityData().getCompoundTag(LoadResearchData.RESEARCH_DATA)));
			Refrence.researchAchievedOverlay.displayResearch(type, new ItemStack(ModItems.journal, 1), false);
		}
		else
		{
			AfraidOfTheDark.getSimpleNetworkWrapper().sendTo(new UpdateResearch(entityPlayer.getEntityData().getCompoundTag(LoadResearchData.RESEARCH_DATA)), (EntityPlayerMP) entityPlayer);
		}
	}

	public static int getIndexFromResearch(ResearchTypes type)
	{
		Research temp = new Research();
		for (int i = 0; i < temp.getResearches().size(); i++)
		{
			if (temp.getResearches().get(i).getType() == type)
			{
				return i;
			}
		}
		return Integer.MIN_VALUE;
	}
}
