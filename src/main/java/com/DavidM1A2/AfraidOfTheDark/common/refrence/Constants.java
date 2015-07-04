/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.refrence;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.EnumHelper;

import com.DavidM1A2.AfraidOfTheDark.common.entities.DeeeSyft.EntityDeeeSyft;
import com.DavidM1A2.AfraidOfTheDark.common.entities.POOPER123.EntityPOOPER123;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.Schematic;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.SchematicBlockReplacer;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.SchematicLoader;

public final class Constants
{
	public static Map<Class, Integer> entityVitaeResistance = new HashMap<Class, Integer>();
	public static Map<String, Integer> toolMaterialRepairCosts = new HashMap<String, Integer>();

	static
	{
		entityVitaeResistance.put(EntityPlayer.class, 100);
		entityVitaeResistance.put(EntityPlayerMP.class, 100);
		entityVitaeResistance.put(EntityBat.class, 5);
		entityVitaeResistance.put(EntityChicken.class, 10);
		entityVitaeResistance.put(EntityCow.class, 50);
		entityVitaeResistance.put(EntityMooshroom.class, 50);
		entityVitaeResistance.put(EntityPig.class, 40);
		entityVitaeResistance.put(EntityRabbit.class, 5);
		entityVitaeResistance.put(EntitySheep.class, 40);
		entityVitaeResistance.put(EntitySquid.class, 30);
		entityVitaeResistance.put(EntityVillager.class, 100);
		entityVitaeResistance.put(EntityCaveSpider.class, 90);
		entityVitaeResistance.put(EntityEnderman.class, 105);
		entityVitaeResistance.put(EntitySpider.class, 60);
		entityVitaeResistance.put(EntityPigZombie.class, 100);
		entityVitaeResistance.put(EntityBlaze.class, 100);
		entityVitaeResistance.put(EntityCreeper.class, 100);
		entityVitaeResistance.put(EntityGuardian.class, 150);
		entityVitaeResistance.put(EntityEndermite.class, 4);
		entityVitaeResistance.put(EntityGhast.class, 115);
		entityVitaeResistance.put(EntityMagmaCube.class, 60);
		entityVitaeResistance.put(EntitySilverfish.class, 25);
		entityVitaeResistance.put(EntitySkeleton.class, 100);
		entityVitaeResistance.put(EntitySlime.class, 85);
		entityVitaeResistance.put(EntityWitch.class, 100);
		entityVitaeResistance.put(EntityZombie.class, 100);
		entityVitaeResistance.put(EntityHorse.class, 50);
		entityVitaeResistance.put(EntityOcelot.class, 25);
		entityVitaeResistance.put(EntityWolf.class, 70);
		entityVitaeResistance.put(EntityIronGolem.class, 190);
		entityVitaeResistance.put(EntitySnowman.class, 15);
		entityVitaeResistance.put(EntityDragon.class, 300);
		entityVitaeResistance.put(EntityWither.class, 300);
		entityVitaeResistance.put(EntityPOOPER123.class, 120);
		entityVitaeResistance.put(EntityDeeeSyft.class, 150);

		toolMaterialRepairCosts.put("EMERALD", 4);
		toolMaterialRepairCosts.put("GOLD", 5);
		toolMaterialRepairCosts.put("IRON", 3);
		toolMaterialRepairCosts.put("STONE", 1);
		toolMaterialRepairCosts.put("WOOD", 1);

		toolMaterialRepairCosts.put("LEATHER", 1);
		toolMaterialRepairCosts.put("CHAINMAIL", 2);
		toolMaterialRepairCosts.put("DIAMOND", 4);

		toolMaterialRepairCosts.put("silverTool", 4);
		toolMaterialRepairCosts.put("igneousTool", 4);
		toolMaterialRepairCosts.put("starMetalTool", 4);
		toolMaterialRepairCosts.put("igneous", 4);
		toolMaterialRepairCosts.put("starMetal", 4);
	}

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
		public static final int PACKET_ID_FIRE_BOLT = 8;
		public static final int PACKET_ID_UPDATE_LANTERN_STATE = 9;
		public static final int PACKET_ID_UPDATE_SLEEPING_STATE = 10;
		public static final int PACKET_ID_PLAY_ANIMATION = 11;
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
		public static final DamageSource silverDamage = new DamageSource("silverDamage");
	}

	public static final class NightmareWorld
	{
		public static final String NIGHTMARE_WORLD_NAME = "";
		public static final int NIGHTMARE_WORLD_ID = 67;
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

		public static final Schematic crypt;
		public static final Schematic spring;

		public static final Schematic nightmareIsland;

		static
		{
			treeSmall = SchematicLoader.load("darkForest/TreeSmall.schematic");
			treeBranchyType1 = SchematicLoader.load("darkForest/TreeBranchyType1.schematic");
			treeBranchyType2 = SchematicLoader.load("darkForest/TreeBranchyType2.schematic");
			treeLargeCircle = SchematicLoader.load("darkForest/TreeLargeCircle.schematic");
			treeLargeDonut = SchematicLoader.load("darkForest/TreeLargeDonut.schematic");
			bedHouse = SchematicLoader.load("darkForest/BedHouse.schematic");
			propBush1 = SchematicLoader.load("darkForest/PropBush1.schematic");
			propFallenOverLog = SchematicLoader.load("darkForest/PropFallenOverLog.schematic");
			propFence1 = SchematicLoader.load("darkForest/PropFence1.schematic");
			propFence2 = SchematicLoader.load("darkForest/PropFence2.schematic");
			propFountain = SchematicLoader.load("darkForest/PropFountain.schematic");
			propLog = SchematicLoader.load("darkForest/PropLog.schematic");
			propPumpkin1 = SchematicLoader.load("darkForest/PropPumpkin1.schematic");
			propPumpkin2 = SchematicLoader.load("darkForest/PropPumpkin2.schematic");
			propStump = SchematicLoader.load("darkForest/PropStump.schematic");

			crypt = SchematicLoader.load("crypt.schematic");
			spring = SchematicLoader.load("spring.schematic");

			nightmareIsland = SchematicLoader.load("nightmareIsland.schematic");

			SchematicBlockReplacer.replaceBlocks(treeSmall, Blocks.log, ModBlocks.gravewood, Blocks.leaves, ModBlocks.gravewoodLeaves);
			SchematicBlockReplacer.replaceBlocks(treeBranchyType1, Blocks.log, ModBlocks.gravewood, Blocks.leaves, ModBlocks.gravewoodLeaves);
			SchematicBlockReplacer.replaceBlocks(treeBranchyType2, Blocks.log, ModBlocks.gravewood, Blocks.leaves, ModBlocks.gravewoodLeaves);
			SchematicBlockReplacer.replaceBlocks(treeLargeCircle, Blocks.log, ModBlocks.gravewood, Blocks.leaves, ModBlocks.gravewoodLeaves);
			SchematicBlockReplacer.replaceBlocks(treeLargeDonut, Blocks.log, ModBlocks.gravewood, Blocks.leaves, ModBlocks.gravewoodLeaves);
			SchematicBlockReplacer.replaceBlocks(bedHouse, Blocks.lapis_block, ModBlocks.darkForest, Blocks.gold_block, Blocks.flower_pot, Blocks.iron_block, Blocks.cobblestone_wall, Blocks.gold_ore, Blocks.dark_oak_stairs, Blocks.bedrock, Blocks.spruce_stairs);

			SchematicBlockReplacer.replaceBlocks(crypt, Blocks.gold_block, ModBlocks.gravewoodStairs, Blocks.gold_ore, ModBlocks.gravewoodPlanks);
			SchematicBlockReplacer.replaceBlocks(spring, Blocks.gold_ore, ModBlocks.gravewoodLeaves, Blocks.gold_block, ModBlocks.spring);

			SchematicBlockReplacer.fixKnownSchematicErrors(propBush1);
			SchematicBlockReplacer.fixKnownSchematicErrors(propFallenOverLog);
			SchematicBlockReplacer.fixKnownSchematicErrors(propFence1);
			SchematicBlockReplacer.fixKnownSchematicErrors(propFence2);
			SchematicBlockReplacer.fixKnownSchematicErrors(propFountain);
			SchematicBlockReplacer.fixKnownSchematicErrors(propLog);
			SchematicBlockReplacer.fixKnownSchematicErrors(propPumpkin1);
			SchematicBlockReplacer.fixKnownSchematicErrors(propPumpkin2);
			SchematicBlockReplacer.fixKnownSchematicErrors(propStump);

			SchematicBlockReplacer.replaceBlocks(nightmareIsland, ((short) -56), (short) Block.getIdFromBlock(ModBlocks.gravewoodLeaves), ((short) -55), (short) Block.getIdFromBlock(ModBlocks.gravewood));
			SchematicBlockReplacer.fixKnownSchematicErrors(nightmareIsland);
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
