/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.refrence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.DavidM1A2.AfraidOfTheDark.common.entities.DeeeSyft.EntityDeeeSyft;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria.EntityEnaria;
import com.DavidM1A2.AfraidOfTheDark.common.entities.EnchantedSkeleton.EntityEnchantedSkeleton;
import com.DavidM1A2.AfraidOfTheDark.common.entities.SplinterDrone.EntitySplinterDrone;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Werewolf.EntityWerewolf;
import com.DavidM1A2.AfraidOfTheDark.common.handler.ConfigurationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.sizeof.RamUsageEstimator;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.Schematic;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.SchematicBlockReplacer;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.SchematicLoader;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.CryptChestLoot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.DarkForestChestLoot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.LootTable;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.LootTableEntry;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.VoidChestLoot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.WitchHutLoot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.gnomishCity.GnomishCityLoot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.gnomishCity.GnomishCityRareLoot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.Astronomy2Part1;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.Astronomy2Part2;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.Astronomy2Part3;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.Astronomy2Part4;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.FoodLoot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.InsanityResearchLoot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.PotionLoot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.RandomBlockLoot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.ValueableLoot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.Vitae1Part1Loot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.Vitae1Part2Loot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.Vitae1Part3Loot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.Vitae1Part4Loot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.Vitae1Part5Loot;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland.WeaponryLoot;

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
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.EnumHelper;

public final class Constants
{
	public static final boolean isDebug = false;

	public static Map<Class, Integer> entityVitaeResistance = new HashMap<Class, Integer>();
	public static Map<String, Integer> toolMaterialRepairCosts = new HashMap<String, Integer>();

	public static LootTable cryptLootTable = new LootTable(new LootTableEntry(new CryptChestLoot(), null, 1));
	public static LootTable darkForestLootTable = new LootTable(new LootTableEntry(new DarkForestChestLoot(), null, 6));
	public static LootTable witchHutLootTable = new LootTable(new LootTableEntry(new WitchHutLoot(), null, 1));
	public static LootTable nightmareIslandLootTable = new LootTable(new LootTableEntry[]
	{ new LootTableEntry(new FoodLoot(), Items.cooked_beef, 5 + MathHelper.floor_double(Math.random() * 5)), new LootTableEntry(new InsanityResearchLoot(), Items.feather, 20), new LootTableEntry(new PotionLoot(), Items.sugar, 5), new LootTableEntry(new RandomBlockLoot(), null, 12),
			new LootTableEntry(new ValueableLoot(), Items.diamond, 5), new LootTableEntry(new WeaponryLoot(), Items.blaze_rod, 4), new LootTableEntry(new Vitae1Part1Loot(), Items.emerald, 20), new LootTableEntry(new Vitae1Part2Loot(), Items.iron_ingot, 20), new LootTableEntry(new Vitae1Part3Loot(),
					Items.gold_ingot, 20), new LootTableEntry(new Vitae1Part4Loot(), Items.netherbrick, 20), new LootTableEntry(new Vitae1Part5Loot(), Items.coal, 20), new LootTableEntry(new Astronomy2Part1(), Items.gold_nugget, 20), new LootTableEntry(new Astronomy2Part2(), Items.glowstone_dust,
							20), new LootTableEntry(new Astronomy2Part3(), Items.nether_wart, 20), new LootTableEntry(new Astronomy2Part4(), Items.rabbit_hide, 20), });
	public static LootTable voidChestTable = new LootTable(new LootTableEntry(new VoidChestLoot(), null, 3));
	public static LootTable gnomishCityLootTable = new LootTable(new LootTableEntry(new GnomishCityLoot(), null, 1, 4), new LootTableEntry(new GnomishCityRareLoot(), Items.diamond, 1, 4));

	public static final ScheduledExecutorService TIMER_FOR_DELAYS = Executors.newSingleThreadScheduledExecutor();

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
		entityVitaeResistance.put(EntityWerewolf.class, 120);
		entityVitaeResistance.put(EntityDeeeSyft.class, 150);
		entityVitaeResistance.put(EntityEnchantedSkeleton.class, 100);
		entityVitaeResistance.put(EntitySplinterDrone.class, 100);
		entityVitaeResistance.put(EntityEnaria.class, Integer.MAX_VALUE);

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

	public static final class AOTDToolMaterials
	{
		public static final ToolMaterial astralSilver = EnumHelper.addToolMaterial("silverTool", 2, 250, 1, 3.0F, 20);
		public static final ToolMaterial igneousTool = EnumHelper.addToolMaterial("igneousTool", 3, 600, 1, 5, 15);
		public static final ToolMaterial starMetalTool = EnumHelper.addToolMaterial("starMetalTool", 3, 600, 1, 4, 15);
		public static final ToolMaterial bladeOfExhumation = EnumHelper.addToolMaterial("bladeOfExhumation", 0, 150, 0, 0, 0);
	}

	public static final class AOTDArmorMaterials
	{
		public static final ArmorMaterial igneous = EnumHelper.addArmorMaterial("igneous", "texture", 100, new int[]
		{ 3, 8, 6, 3 }, 20);
		public static final ArmorMaterial starMetal = EnumHelper.addArmorMaterial("starMetal", "texture", 100, new int[]
		{ 3, 8, 6, 3 }, 20);
	}

	public static final class NightmareWorld
	{
		public static final String NIGHTMARE_WORLD_NAME = "nightmare";
		public static final int NIGHTMARE_WORLD_ID = 67;
		public static final int BLOCKS_BETWEEN_ISLANDS = 992;
	}

	public static final class VoidChestWorld
	{
		public static final String VOID_CHEST_WORLD_NAME = "voidChest";
		public static final int VOID_CHEST_WORLD_ID = 68;
		public static final int BLOCKS_BETWEEN_ISLANDS = 992;
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

		public static final Schematic witchHut;

		public static final Schematic voidChestPortal;
		public static final Schematic voidChest;

		public static final Schematic tunnelEW;
		public static final Schematic tunnelNS;
		public static final Schematic roomStairUp;
		public static final Schematic roomStairDown;
		public static final List<Schematic> rooms;
		public static final Schematic stairwell;
		public static final Schematic enariaLair;

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

			crypt = SchematicLoader.load("Crypt.schematic");
			spring = SchematicLoader.load("Spring.schematic");

			nightmareIsland = SchematicLoader.load("NightmareIsland.schematic");

			witchHut = SchematicLoader.load("WitchHut.schematic");

			voidChestPortal = SchematicLoader.load("VoidChestPortal.schematic");

			voidChest = SchematicLoader.load("VoidChest.schematic");

			tunnelEW = SchematicLoader.load("gnomishCity/TunnelEW.schematic");
			tunnelNS = SchematicLoader.load("gnomishCity/TunnelNS.schematic");
			roomStairUp = SchematicLoader.load("gnomishCity/RoomStairUp.schematic");
			roomStairDown = SchematicLoader.load("gnomishCity/RoomStairDown.schematic");
			rooms = new ArrayList<Schematic>()
			{
				{
					add(SchematicBlockReplacer.replaceBlocks(SchematicLoader.load("gnomishCity/RoomCave.schematic"), (short) -36, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalStrut), (short) -37, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalPlate), (short) -35, (short) Block
							.getIdFromBlock(ModBlocks.glowStalk), (short) -41, (short) Block.getIdFromBlock(ModBlocks.eldritchObsidian)));
					add(SchematicBlockReplacer.replaceBlocks(SchematicLoader.load("gnomishCity/RoomFarm.schematic"), (short) -36, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalStrut), (short) -37, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalPlate), (short) -35, (short) Block
							.getIdFromBlock(ModBlocks.glowStalk), (short) -41, (short) Block.getIdFromBlock(ModBlocks.eldritchObsidian)));
					add(SchematicBlockReplacer.replaceBlocks(SchematicLoader.load("gnomishCity/RoomHotel.schematic"), (short) -36, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalStrut), (short) -37, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalPlate), (short) -35, (short) Block
							.getIdFromBlock(ModBlocks.glowStalk), (short) -41, (short) Block.getIdFromBlock(ModBlocks.eldritchObsidian)));
					add(SchematicBlockReplacer.replaceBlocks(SchematicLoader.load("gnomishCity/RoomMeetingHall.schematic"), (short) -36, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalStrut), (short) -37, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalPlate), (short) -35, (short) Block
							.getIdFromBlock(ModBlocks.glowStalk), (short) -41, (short) Block.getIdFromBlock(ModBlocks.eldritchObsidian)));
					add(SchematicBlockReplacer.replaceBlocks(SchematicLoader.load("gnomishCity/RoomMushroom.schematic"), (short) -36, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalStrut), (short) -37, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalPlate), (short) -35, (short) Block
							.getIdFromBlock(ModBlocks.glowStalk), (short) -41, (short) Block.getIdFromBlock(ModBlocks.eldritchObsidian)));
					add(SchematicBlockReplacer.replaceBlocks(SchematicLoader.load("gnomishCity/RoomRuin.schematic"), (short) -36, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalStrut), (short) -37, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalPlate), (short) -35, (short) Block
							.getIdFromBlock(ModBlocks.glowStalk), (short) -41, (short) Block.getIdFromBlock(ModBlocks.eldritchObsidian)));
					add(SchematicBlockReplacer.replaceBlocks(SchematicLoader.load("gnomishCity/RoomTanks.schematic"), (short) -36, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalStrut), (short) -37, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalPlate), (short) -35, (short) Block
							.getIdFromBlock(ModBlocks.glowStalk), (short) -41, (short) Block.getIdFromBlock(ModBlocks.eldritchObsidian)));
				}
			};
			stairwell = SchematicLoader.load("gnomishCity/Stairwell.schematic");
			enariaLair = SchematicLoader.load("gnomishCity/EnariaLair.schematic");

			SchematicBlockReplacer.replaceBlocks(treeSmall, Blocks.log, ModBlocks.gravewood, Blocks.leaves, ModBlocks.gravewoodLeaves);
			SchematicBlockReplacer.replaceBlocks(treeBranchyType1, Blocks.log, ModBlocks.gravewood, Blocks.leaves, ModBlocks.gravewoodLeaves);
			SchematicBlockReplacer.replaceBlocks(treeBranchyType2, Blocks.log, ModBlocks.gravewood, Blocks.leaves, ModBlocks.gravewoodLeaves);
			SchematicBlockReplacer.replaceBlocks(treeLargeCircle, Blocks.log, ModBlocks.gravewood, Blocks.leaves, ModBlocks.gravewoodLeaves);
			SchematicBlockReplacer.replaceBlocks(treeLargeDonut, Blocks.log, ModBlocks.gravewood, Blocks.leaves, ModBlocks.gravewoodLeaves);
			SchematicBlockReplacer.replaceBlocks(bedHouse, Blocks.lapis_block, ModBlocks.darkForest, Blocks.gold_block, Blocks.flower_pot, Blocks.iron_block, Blocks.cobblestone_wall, Blocks.gold_ore, Blocks.dark_oak_stairs, Blocks.bedrock, Blocks.spruce_stairs);

			SchematicBlockReplacer.replaceBlocks(crypt, Blocks.gold_block, ModBlocks.gravewoodStairs, Blocks.gold_ore, ModBlocks.gravewoodPlanks);
			SchematicBlockReplacer.replaceBlocks(spring, Blocks.gold_ore, ModBlocks.gravewoodLeaves, Blocks.gold_block, ModBlocks.spring);

			// SchematicBlockReplacer.replaceBlocks(nightmareIsland, ((short)
			// -56), (short) Block.getIdFromBlock(ModBlocks.gravewoodLeaves),
			// ((short) -55), (short)
			// Block.getIdFromBlock(ModBlocks.gravewood));

			// SchematicBlockReplacer.replaceBlocks(witchHut, ((short) -56),
			// (short) Block.getIdFromBlock(ModBlocks.gravewoodLeaves), ((short)
			// -55), (short) Block.getIdFromBlock(ModBlocks.gravewood), ((short)
			// -52), (short) Block.getIdFromBlock(ModBlocks.gravewoodHalfSlab),
			// ((short) -53), (short) Block
			// .getIdFromBlock(ModBlocks.gravewoodStairs), ((short) -54),
			// (short) Block.getIdFromBlock(ModBlocks.gravewoodPlanks));

			// SchematicBlockReplacer.replaceBlocks(voidChestPortal, (short)
			// -42, (short) Block.getIdFromBlock(ModBlocks.voidChestPortal),
			// (short) -90, (short) Block.getIdFromBlock(Blocks.barrier));
			SchematicBlockReplacer.replaceBlocks(voidChestPortal, Blocks.lapis_block, ModBlocks.eldritchStone);

			// SchematicBlockReplacer.replaceBlocks(voidChest, (short) -41,
			// (short) Block.getIdFromBlock(ModBlocks.eldritchObsidian), (short)
			// -40, (short)
			// Block.getIdFromBlock(ModBlocks.amorphousEldritchMetal), (short)
			// -39, (short) Block.getIdFromBlock(ModBlocks.eldritchStone),
			// (short) -42,
			// (short) Block.getIdFromBlock(ModBlocks.voidChestPortal));

			SchematicBlockReplacer.replaceBlocks(tunnelEW, (short) -36, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalStrut), (short) -37, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalPlate), (short) -35, (short) Block.getIdFromBlock(ModBlocks.glowStalk), (short) -41, (short) Block
					.getIdFromBlock(ModBlocks.eldritchObsidian));
			SchematicBlockReplacer.replaceBlocks(tunnelNS, (short) -36, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalStrut), (short) -37, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalPlate), (short) -35, (short) Block.getIdFromBlock(ModBlocks.glowStalk), (short) -41, (short) Block
					.getIdFromBlock(ModBlocks.eldritchObsidian));
			SchematicBlockReplacer.replaceBlocks(roomStairUp, (short) -36, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalStrut), (short) -37, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalPlate), (short) -35, (short) Block.getIdFromBlock(ModBlocks.glowStalk), (short) -41, (short) Block
					.getIdFromBlock(ModBlocks.eldritchObsidian));
			SchematicBlockReplacer.replaceBlocks(roomStairDown, (short) -36, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalStrut), (short) -37, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalPlate), (short) -35, (short) Block.getIdFromBlock(ModBlocks.glowStalk), (short) -41, (short) Block
					.getIdFromBlock(ModBlocks.eldritchObsidian));
			SchematicBlockReplacer.replaceBlocks(stairwell, (short) -36, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalStrut), (short) -37, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalPlate), (short) -35, (short) Block.getIdFromBlock(ModBlocks.glowStalk), (short) -41, (short) Block
					.getIdFromBlock(ModBlocks.eldritchObsidian));
			SchematicBlockReplacer.replaceBlocks(enariaLair, (short) -36, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalStrut), (short) -37, (short) Block.getIdFromBlock(ModBlocks.gnomishMetalPlate), (short) -35, (short) Block.getIdFromBlock(ModBlocks.glowStalk), (short) -41, (short) Block
					.getIdFromBlock(ModBlocks.eldritchObsidian), (short) -40, (short) Block.getIdFromBlock(ModBlocks.amorphousEldritchMetal), (short) -39, (short) Block.getIdFromBlock(ModBlocks.eldritchStone), (short) -48, (short) Block.getIdFromBlock(ModBlocks.meteor), (short) -47, (short) Block
							.getIdFromBlock(ModBlocks.starMetalOre), (short) -46, (short) Block.getIdFromBlock(ModBlocks.igneousBlock), (short) -55, (short) Block.getIdFromBlock(ModBlocks.gravewood));

			if (ConfigurationHandler.debugMessages)
			{
				LogHelper.info("Nightmare island is using this much ram: " + RamUsageEstimator.humanReadableUnits(RamUsageEstimator.sizeOfAll(nightmareIsland)));
			}
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
