/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.initializeMod;

import com.DavidM1A2.AfraidOfTheDark.common.block.BlockAmorphousEldritchMetal;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockAstralSilverOre;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockDarkForest;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockEldritchObsidian;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockEldritchStone;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockEnariaSpawner;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockEnariasAltar;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockGlowStalk;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockGnomishMetalPlate;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockGnomishMetalStrut;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockIgneous;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockMeteor;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockSpring;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockStarMetalOre;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockSunstoneOre;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockVitaeDisenchanter;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockVoidChest;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockVoidChestPortal;
import com.DavidM1A2.AfraidOfTheDark.common.block.gravewood.BlockGravewood;
import com.DavidM1A2.AfraidOfTheDark.common.block.gravewood.BlockGravewoodDoubleSlab;
import com.DavidM1A2.AfraidOfTheDark.common.block.gravewood.BlockGravewoodHalfSlab;
import com.DavidM1A2.AfraidOfTheDark.common.block.gravewood.BlockGravewoodLeaves;
import com.DavidM1A2.AfraidOfTheDark.common.block.gravewood.BlockGravewoodPlanks;
import com.DavidM1A2.AfraidOfTheDark.common.block.gravewood.BlockGravewoodSapling;
import com.DavidM1A2.AfraidOfTheDark.common.block.gravewood.BlockGravewoodStairs;
import com.DavidM1A2.AfraidOfTheDark.common.block.mangrove.BlockMangrove;
import com.DavidM1A2.AfraidOfTheDark.common.block.mangrove.BlockMangroveDoubleSlab;
import com.DavidM1A2.AfraidOfTheDark.common.block.mangrove.BlockMangroveHalfSlab;
import com.DavidM1A2.AfraidOfTheDark.common.block.mangrove.BlockMangroveLeaves;
import com.DavidM1A2.AfraidOfTheDark.common.block.mangrove.BlockMangrovePlanks;
import com.DavidM1A2.AfraidOfTheDark.common.block.mangrove.BlockMangroveSapling;
import com.DavidM1A2.AfraidOfTheDark.common.block.mangrove.BlockMangroveStairs;
import com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity.TileEntityDarkForest;
import com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity.TileEntityEnariaSpawner;
import com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity.TileEntityGhastlyEnariaSpawner;
import com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity.TileEntitySpring;
import com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity.TileEntityVoidChest;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemGravewoodLeaves;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemGravewoodSlab;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemMangroveLeaves;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemMangroveSlab;
import com.DavidM1A2.AfraidOfTheDark.common.reference.Reference;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModBlocks
{
	// Register blocks 
	public static final BlockGravewood gravewood = new BlockGravewood();
	public static final Item gravewoodItem = new ItemBlock(gravewood).setRegistryName("gravewood");
	public static final BlockGravewoodPlanks gravewoodPlanks = new BlockGravewoodPlanks();
	public static final Item gravewoodPlanksItem = new ItemBlock(gravewoodPlanks).setRegistryName("gravewood_planks");
	public static final BlockGravewoodStairs gravewoodStairs = new BlockGravewoodStairs();
	public static final Item gravewoodStairsItem = new ItemBlock(gravewoodStairs).setRegistryName("gravewood_stairs");
	public static final BlockGravewoodHalfSlab gravewoodHalfSlab = new BlockGravewoodHalfSlab();
	public static final BlockGravewoodDoubleSlab gravewoodDoubleSlab = new BlockGravewoodDoubleSlab();
	public static final BlockGravewoodSapling gravewoodSapling = new BlockGravewoodSapling();
	public static final Item gravewoodSaplingItem = new ItemBlock(gravewoodSapling).setRegistryName("gravewood_sapling");
	public static final BlockGravewoodLeaves gravewoodLeaves = new BlockGravewoodLeaves();
	public static final Item gravewoodLeavesItem = new ItemBlock(gravewoodLeaves).setRegistryName("gravewood_leaves");

	public static final BlockMangrove mangrove = new BlockMangrove();
	public static final Item mangroveItem = new ItemBlock(mangrove).setRegistryName("mangrove");
	public static final BlockMangrovePlanks mangrovePlanks = new BlockMangrovePlanks();
	public static final Item mangrovePlanksItem = new ItemBlock(mangrovePlanks).setRegistryName("mangrove_planks");
	public static final BlockMangroveStairs mangroveStairs = new BlockMangroveStairs();
	public static final Item mangroveStairsItem = new ItemBlock(mangroveStairs).setRegistryName("mangrove_stairs");
	public static final BlockMangroveHalfSlab mangroveHalfSlab = new BlockMangroveHalfSlab();
	public static final Item mangroveHalfSlabItem = new ItemBlock(mangroveHalfSlab).setRegistryName("mangrove_half_slab");
	public static final BlockMangroveDoubleSlab mangroveDoubleSlab = new BlockMangroveDoubleSlab();
	public static final Item mangroveDoubleSlabItem = new ItemBlock(mangroveDoubleSlab).setRegistryName("mangrove_upper_slab");
	public static final BlockMangroveSapling mangroveSapling = new BlockMangroveSapling();
	public static final Item mangroveSaplingItem = new ItemBlock(mangroveSapling).setRegistryName("mangrove_sapling");
	public static final BlockMangroveLeaves mangroveLeaves = new BlockMangroveLeaves();
	public static final Item mangroveLeavesItem = new ItemBlock(mangroveLeaves).setRegistryName("mangrove_leaves");

	public static final BlockSunstoneOre sunstoneOre = new BlockSunstoneOre();
	public static final Item sunstoneOreItem = new ItemBlock(sunstoneOre).setRegistryName("sunstone_ore");
	public static final BlockAstralSilverOre astralSilverOre = new BlockAstralSilverOre();
	public static final Item astralSilverOreItem = new ItemBlock(astralSilverOre).setRegistryName("astral_silver_ore");
	public static final BlockMeteor meteor = new BlockMeteor();
	public static final Item meteorItem = new ItemBlock(meteor).setRegistryName("meteor");
	public static final BlockStarMetalOre starMetalOre = new BlockStarMetalOre();
	public static final Item starMetalOreItem = new ItemBlock(starMetalOre).setRegistryName("star_metal_ore");
	public static final BlockIgneous igneousBlock = new BlockIgneous();
	public static final Item igneousBlockItem = new ItemBlock(igneousBlock).setRegistryName("igneous_block");
	public static final BlockSpring spring = new BlockSpring();
	public static final Item springItem = new ItemBlock(spring).setRegistryName("spring");
	public static final BlockDarkForest darkForest = new BlockDarkForest();
	public static final Item darkForestItem = new ItemBlock(darkForest).setRegistryName("dark_forest");
	public static final BlockVitaeDisenchanter vitaeDisenchanter = new BlockVitaeDisenchanter();
	public static final Item vitaeDisenchanterItem = new ItemBlock(vitaeDisenchanter).setRegistryName("vitae_disenchanter");
	public static final BlockVoidChest voidChest = new BlockVoidChest();
	public static final Item voidChestItem = new ItemBlock(voidChest).setRegistryName("void_chest");
	public static final BlockVoidChestPortal voidChestPortal = new BlockVoidChestPortal();
	public static final Item voidChestPortalItem = new ItemBlock(voidChestPortal).setRegistryName("void_chest_portal");
	public static final BlockEldritchObsidian eldritchObsidian = new BlockEldritchObsidian();
	public static final Item eldritchObsidianItem = new ItemBlock(eldritchObsidian).setRegistryName("eldritch_obsidian");
	public static final BlockAmorphousEldritchMetal amorphousEldritchMetal = new BlockAmorphousEldritchMetal();
	public static final Item amorphousEldritchMetalItem = new ItemBlock(amorphousEldritchMetal).setRegistryName("amorphous_eldritch_metal");
	public static final BlockEldritchStone eldritchStone = new BlockEldritchStone();
	public static final Item eldritchStoneItem = new ItemBlock(eldritchStone).setRegistryName("eldritch_stone");
	public static final BlockGnomishMetalPlate gnomishMetalPlate = new BlockGnomishMetalPlate();
	public static final Item gnomishMetalPlateItem = new ItemBlock(gnomishMetalPlate).setRegistryName("gnomish_metal_plate");
	public static final BlockGnomishMetalStrut gnomishMetalStrut = new BlockGnomishMetalStrut();
	public static final Item gnomishMetalStrutItem = new ItemBlock(gnomishMetalStrut).setRegistryName("gnomish_metal_strut");
	public static final BlockGlowStalk glowStalk = new BlockGlowStalk();
	public static final Item glowStalkItem = new ItemBlock(glowStalk).setRegistryName("glow_stalk");
	public static final BlockEnariaSpawner enariaSpawner = new BlockEnariaSpawner();
	public static final Item enariaSpawnerItem = new ItemBlock(enariaSpawner).setRegistryName("enaria_spawner");
	public static final BlockEnariasAltar enariasAltar = new BlockEnariasAltar();
	public static final Item enariasAltarItem = new ItemBlock(enariasAltar).setRegistryName("enarias_altar");

	public static void initialize()
	{
		// Register the items, allow gravewood to burn, and register tileEntities
		GameRegistry.register(ModBlocks.spring);
		GameRegistry.register(ModBlocks.springItem);
		GameRegistry.registerTileEntity(TileEntitySpring.class, "tile_entity_spring");
		GameRegistry.register(ModBlocks.darkForest);
		GameRegistry.register(ModBlocks.darkForestItem);
		GameRegistry.registerTileEntity(TileEntityDarkForest.class, "tile_entity_dark_forest");

		GameRegistry.register(ModBlocks.gravewood);
		GameRegistry.register(ModBlocks.gravewoodItem);
		GameRegistry.register(ModBlocks.gravewoodPlanks);
		GameRegistry.register(ModBlocks.gravewoodPlanksItem);
		GameRegistry.register(ModBlocks.gravewoodStairs);
		GameRegistry.register(ModBlocks.gravewoodStairsItem);
		GameRegistry.register(ModBlocks.gravewoodHalfSlab);
		GameRegistry.register(ModBlocks.gravewoodDoubleSlab);
		GameRegistry.register(new ItemGravewoodSlab(ModBlocks.gravewoodHalfSlab, ModBlocks.gravewoodHalfSlab, ModBlocks.gravewoodDoubleSlab, false));
		GameRegistry.register(new ItemGravewoodSlab(ModBlocks.gravewoodDoubleSlab, ModBlocks.gravewoodHalfSlab, ModBlocks.gravewoodDoubleSlab, true));
		GameRegistry.register(ModBlocks.gravewoodSapling);
		GameRegistry.register(ModBlocks.gravewoodSaplingItem);
		GameRegistry.register(ModBlocks.gravewoodLeaves);
		GameRegistry.register(new ItemGravewoodLeaves(ModBlocks.gravewoodLeaves));

		Blocks.FIRE.setFireInfo(ModBlocks.gravewoodPlanks, 5, 5);
		Blocks.FIRE.setFireInfo(ModBlocks.gravewoodStairs, 5, 5);
		Blocks.FIRE.setFireInfo(ModBlocks.gravewoodHalfSlab, 5, 5);
		Blocks.FIRE.setFireInfo(ModBlocks.gravewoodSapling, 5, 5);
		Blocks.FIRE.setFireInfo(ModBlocks.gravewoodDoubleSlab, 5, 5);

		GameRegistry.register(ModBlocks.mangrove);
		GameRegistry.register(ModBlocks.mangroveItem);
		GameRegistry.register(ModBlocks.mangrovePlanks);
		GameRegistry.register(ModBlocks.mangrovePlanksItem);
		GameRegistry.register(ModBlocks.mangroveStairs);
		GameRegistry.register(ModBlocks.mangroveStairsItem);
		GameRegistry.register(ModBlocks.mangroveHalfSlab);
		GameRegistry.register(ModBlocks.mangroveDoubleSlab);
		GameRegistry.register(new ItemMangroveSlab(ModBlocks.mangroveHalfSlab, ModBlocks.mangroveHalfSlab, ModBlocks.mangroveDoubleSlab, false));
		GameRegistry.register(new ItemMangroveSlab(ModBlocks.mangroveDoubleSlab, ModBlocks.mangroveHalfSlab, ModBlocks.mangroveDoubleSlab, true));
		GameRegistry.register(ModBlocks.mangroveSapling);
		GameRegistry.register(ModBlocks.mangroveSaplingItem);
		GameRegistry.register(ModBlocks.mangroveLeaves);
		GameRegistry.register(new ItemMangroveLeaves(ModBlocks.mangroveLeaves));

		Blocks.FIRE.setFireInfo(ModBlocks.mangrove, 5, 3);
		Blocks.FIRE.setFireInfo(ModBlocks.mangrovePlanks, 5, 5);
		Blocks.FIRE.setFireInfo(ModBlocks.mangroveStairs, 5, 5);
		Blocks.FIRE.setFireInfo(ModBlocks.mangroveHalfSlab, 5, 5);
		Blocks.FIRE.setFireInfo(ModBlocks.mangroveDoubleSlab, 5, 5);
		Blocks.FIRE.setFireInfo(ModBlocks.mangroveSapling, 5, 5);
		Blocks.FIRE.setFireInfo(ModBlocks.mangroveLeaves, 5, 5);

		GameRegistry.register(ModBlocks.sunstoneOre);
		GameRegistry.register(ModBlocks.sunstoneOreItem);
		GameRegistry.register(ModBlocks.astralSilverOre);
		GameRegistry.register(ModBlocks.astralSilverOreItem);
		GameRegistry.register(ModBlocks.meteor);
		GameRegistry.register(ModBlocks.meteorItem);
		GameRegistry.register(ModBlocks.starMetalOre);
		GameRegistry.register(ModBlocks.starMetalOreItem);
		GameRegistry.register(ModBlocks.igneousBlock);
		GameRegistry.register(ModBlocks.igneousBlockItem);
		GameRegistry.register(ModBlocks.vitaeDisenchanter);
		GameRegistry.register(ModBlocks.vitaeDisenchanterItem);
		GameRegistry.register(ModBlocks.voidChest);
		GameRegistry.register(ModBlocks.voidChestItem);
		GameRegistry.registerTileEntity(TileEntityVoidChest.class, "tile_entity_void_chest");
		GameRegistry.register(ModBlocks.voidChestPortal);
		GameRegistry.register(ModBlocks.voidChestPortalItem);
		GameRegistry.register(ModBlocks.eldritchObsidian);
		GameRegistry.register(ModBlocks.eldritchObsidianItem);
		GameRegistry.register(ModBlocks.amorphousEldritchMetal);
		GameRegistry.register(ModBlocks.amorphousEldritchMetalItem);
		GameRegistry.register(ModBlocks.eldritchStone);
		GameRegistry.register(ModBlocks.eldritchStoneItem);
		GameRegistry.register(ModBlocks.gnomishMetalPlate);
		GameRegistry.register(ModBlocks.gnomishMetalPlateItem);
		GameRegistry.register(ModBlocks.gnomishMetalStrut);
		GameRegistry.register(ModBlocks.gnomishMetalStrutItem);
		GameRegistry.register(ModBlocks.glowStalk);
		GameRegistry.register(ModBlocks.glowStalkItem);
		GameRegistry.register(ModBlocks.enariaSpawner);
		GameRegistry.register(ModBlocks.enariaSpawnerItem);
		GameRegistry.registerTileEntity(TileEntityEnariaSpawner.class, "tile_entity_enaria_spawner");
		GameRegistry.registerTileEntity(TileEntityGhastlyEnariaSpawner.class, "tile_entity_ghastly_enaria_spawner");
		GameRegistry.register(ModBlocks.enariasAltar);
		GameRegistry.register(ModBlocks.enariasAltarItem);
	}
}
