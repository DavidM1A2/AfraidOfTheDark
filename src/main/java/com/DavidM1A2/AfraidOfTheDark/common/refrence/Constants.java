package com.DavidM1A2.AfraidOfTheDark.common.refrence;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.EnumHelper;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.Schematic;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.SchematicBlockReplacer;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.SchematicLoader;

public final class Constants
{
	public static final class Packets
	{
		// Packet ids
		public static final int PACKET_ID_CROSSBOW = 1;
		public static final int PACKET_ID_INSANITY_UPDATE = 2;
		public static final int PACKET_ID_HAS_STARTED_AOTD_UPDATE = 3;
		public static final int PACKET_ID_RESEARCH_UPDATE = 4;
		public static final int PACKET_ID_CREATE_METEOR = 5;
		public static final int PACKET_ID_VITAE_UPDATE = 6;
		public static final int PACKET_ID_ROTATE_PLAYER_UPDATE = 7;
	}

	public static final class AOTDToolMaterials
	{
		public static final ToolMaterial astralSilver = EnumHelper.addToolMaterial("silverTool", 2, 250, 1, 3.0F, 20);
		public static final ToolMaterial igneousTool = EnumHelper.addToolMaterial("igneousTool", 3, 600, 1, 5, 15);
		public static final ToolMaterial starMetalTool = EnumHelper.addToolMaterial("starMetalTool", 3, 600, 1, 4, 15);
	}

	public static final class AOTDArmorMaterials
	{
		public static final ArmorMaterial igneous = EnumHelper.addArmorMaterial("igneous", "texture", 100, new int[]
		{ 3, 8, 6, 3 }, 20);
		public static final ArmorMaterial starMetal = EnumHelper.addArmorMaterial("starMetal", "texture", 100, new int[]
		{ 3, 8, 6, 3 }, 20);
	}

	public static final class AOTDDamageSources
	{
		// Silver weapon damage type and silver tool material
		public static final DamageSource silverWeapon = new DamageSource("silverWeapon");
	}

	public static final class AOTDSchematics
	{
		public static final Schematic treeSmall;
		public static final Schematic treeBranchyType1;
		public static final Schematic treeBranchyType2;
		public static final Schematic treeLargeCircle;
		public static final Schematic treeLargeDonut;
		public static final Schematic bedHouse;
		public static final Schematic propBush1;
		public static final Schematic propFallenOverLog;
		public static final Schematic propFence1;
		public static final Schematic propFence2;
		public static final Schematic propFountain;
		public static final Schematic propLog;
		public static final Schematic propPumpkin1;
		public static final Schematic propPumpkin2;
		public static final Schematic propStump;

		static
		{
			treeSmall = SchematicLoader.load("TreeSmall.schematic");
			treeBranchyType1 = SchematicLoader.load("TreeBranchyType1.schematic");
			treeBranchyType2 = SchematicLoader.load("TreeBranchyType2.schematic");
			treeLargeCircle = SchematicLoader.load("TreeLargeCircle.schematic");
			treeLargeDonut = SchematicLoader.load("TreeLargeDonut.schematic");
			bedHouse = SchematicLoader.load("BedHouse.schematic");
			propBush1 = SchematicLoader.load("PropBush1.schematic");
			propFallenOverLog = SchematicLoader.load("PropFallenOverLog.schematic");
			propFence1 = SchematicLoader.load("PropFence1.schematic");
			propFence2 = SchematicLoader.load("PropFence2.schematic");
			propFountain = SchematicLoader.load("PropFountain.schematic");
			propLog = SchematicLoader.load("PropLog.schematic");
			propPumpkin1 = SchematicLoader.load("PropPumpkin1.schematic");
			propPumpkin2 = SchematicLoader.load("PropPumpkin2.schematic");
			propStump = SchematicLoader.load("PropStump.schematic");
			SchematicBlockReplacer.replaceBlocks(treeSmall, Blocks.log, ModBlocks.gravewood, Blocks.leaves, ModBlocks.gravewoodLeaves);
			SchematicBlockReplacer.replaceBlocks(treeBranchyType1, Blocks.log, ModBlocks.gravewood, Blocks.leaves, ModBlocks.gravewoodLeaves);
			SchematicBlockReplacer.replaceBlocks(treeBranchyType2, Blocks.log, ModBlocks.gravewood, Blocks.leaves, ModBlocks.gravewoodLeaves);
			SchematicBlockReplacer.replaceBlocks(treeLargeCircle, Blocks.log, ModBlocks.gravewood, Blocks.leaves, ModBlocks.gravewoodLeaves);
			SchematicBlockReplacer.replaceBlocks(treeLargeDonut, Blocks.log, ModBlocks.gravewood, Blocks.leaves, ModBlocks.gravewoodLeaves);
			SchematicBlockReplacer.replaceBlocks(bedHouse, Blocks.lapis_block, ModBlocks.darkForest, Blocks.gold_block, Blocks.flower_pot, Blocks.iron_block, Blocks.cobblestone_wall, Blocks.gold_ore, Blocks.dark_oak_stairs, Blocks.bedrock, Blocks.spruce_stairs);
		}
	}

	// The creative tab
	public static final CreativeTabs AFRAID_OF_THE_DARK = new CreativeTabs(Refrence.MOD_ID.toLowerCase())
	{
		// Icon of the tab is the journal
		@Override
		public Item getTabIconItem()
		{
			return ModItems.journal;
		}
	};
}
