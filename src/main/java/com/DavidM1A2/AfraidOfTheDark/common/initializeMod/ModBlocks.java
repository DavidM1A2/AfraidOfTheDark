/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.initializeMod;

import com.DavidM1A2.AfraidOfTheDark.common.block.BlockAOTDBarrier;
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
import com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity.TileEntityDarkForest;
import com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity.TileEntityEnariaSpawner;
import com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity.TileEntityGhastlyEnariaSpawner;
import com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity.TileEntitySpring;
import com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity.TileEntityVoidChest;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemGravewoodLeaves;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemGravewoodSlab;
import com.DavidM1A2.AfraidOfTheDark.common.reference.Reference;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModBlocks
{
	// Register blocks 
	public static final BlockGravewoodLeaves gravewoodLeaves = new BlockGravewoodLeaves();
	public static final BlockGravewood gravewood = new BlockGravewood();
	public static final BlockGravewoodPlanks gravewoodPlanks = new BlockGravewoodPlanks();
	public static final BlockGravewoodStairs gravewoodStairs = new BlockGravewoodStairs();
	public static final BlockGravewoodHalfSlab gravewoodHalfSlab = new BlockGravewoodHalfSlab();
	public static final BlockGravewoodDoubleSlab gravewoodDoubleSlab = new BlockGravewoodDoubleSlab();
	public static final BlockMangrove mangrove = new BlockMangrove();
	public static final BlockSunstoneOre sunstoneOre = new BlockSunstoneOre();
	public static final BlockAstralSilverOre astralSilverOre = new BlockAstralSilverOre();
	public static final BlockMeteor meteor = new BlockMeteor();
	public static final BlockStarMetalOre starMetalOre = new BlockStarMetalOre();
	public static final BlockIgneous igneousBlock = new BlockIgneous();
	public static final BlockSpring spring = new BlockSpring();
	public static final BlockDarkForest darkForest = new BlockDarkForest();
	public static final BlockGravewoodSapling gravewoodSapling = new BlockGravewoodSapling();
	public static final BlockVitaeDisenchanter vitaeDisenchanter = new BlockVitaeDisenchanter();
	public static final BlockVoidChest voidChest = new BlockVoidChest();
	public static final BlockVoidChestPortal voidChestPortal = new BlockVoidChestPortal();
	public static final BlockEldritchObsidian eldritchObsidian = new BlockEldritchObsidian();
	public static final BlockAmorphousEldritchMetal amorphousEldritchMetal = new BlockAmorphousEldritchMetal();
	public static final BlockEldritchStone eldritchStone = new BlockEldritchStone();
	public static final BlockAOTDBarrier aOTDBarrier = new BlockAOTDBarrier();
	public static final BlockGnomishMetalPlate gnomishMetalPlate = new BlockGnomishMetalPlate();
	public static final BlockGnomishMetalStrut gnomishMetalStrut = new BlockGnomishMetalStrut();
	public static final BlockGlowStalk glowStalk = new BlockGlowStalk();
	public static final BlockEnariaSpawner enariaSpawner = new BlockEnariaSpawner();
	public static final BlockEnariasAltar enariasAltar = new BlockEnariasAltar();

	public static void initialize()
	{
		// Register the items, allow gravewood to burn, and register tileEntities
		GameRegistry.registerBlock(ModBlocks.spring, "spring");
		GameRegistry.registerTileEntity(TileEntitySpring.class, "tileEntitySpring");
		GameRegistry.registerBlock(ModBlocks.darkForest, "darkForest");
		GameRegistry.registerTileEntity(TileEntityDarkForest.class, "tileEntityDarkForest");
		GameRegistry.registerBlock(ModBlocks.gravewoodLeaves, ItemGravewoodLeaves.class, "gravewoodLeaves");
		GameRegistry.registerBlock(ModBlocks.gravewood, "gravewood");
		GameRegistry.registerBlock(ModBlocks.gravewoodPlanks, "gravewoodPlanks");
		Blocks.fire.setFireInfo(ModBlocks.gravewoodPlanks, 5, 5);
		GameRegistry.registerBlock(ModBlocks.gravewoodStairs, "gravewoodStairs");
		Blocks.fire.setFireInfo(ModBlocks.gravewoodStairs, 5, 5);
		GameRegistry.registerBlock(ModBlocks.gravewoodHalfSlab, ItemGravewoodSlab.class, "gravewoodHalfSlab", ModBlocks.gravewoodHalfSlab, ModBlocks.gravewoodDoubleSlab, false);
		Blocks.fire.setFireInfo(ModBlocks.gravewoodHalfSlab, 5, 5);
		GameRegistry.registerBlock(ModBlocks.gravewoodDoubleSlab, ItemGravewoodSlab.class, "gravewoodDoubleSlab", ModBlocks.gravewoodHalfSlab, ModBlocks.gravewoodDoubleSlab, true);
		Blocks.fire.setFireInfo(ModBlocks.gravewoodDoubleSlab, 5, 5);
		GameRegistry.registerBlock(ModBlocks.mangrove, "mangrove");
		GameRegistry.registerBlock(ModBlocks.sunstoneOre, "sunstoneOre");
		GameRegistry.registerBlock(ModBlocks.astralSilverOre, "astralSilverOre");
		GameRegistry.registerBlock(ModBlocks.meteor, "meteor");
		GameRegistry.registerBlock(ModBlocks.starMetalOre, "starMetalOre");
		GameRegistry.registerBlock(ModBlocks.igneousBlock, "igneousBlock");
		GameRegistry.registerBlock(ModBlocks.gravewoodSapling, "gravewoodSapling");
		GameRegistry.registerBlock(ModBlocks.vitaeDisenchanter, "vitaeDisenchanter");
		GameRegistry.registerBlock(ModBlocks.voidChest, "voidChest");
		GameRegistry.registerTileEntity(TileEntityVoidChest.class, "tileEntityVoidChest");
		GameRegistry.registerBlock(ModBlocks.voidChestPortal, "voidChestPortal");
		GameRegistry.registerBlock(ModBlocks.eldritchObsidian, "eldritchObsidian");
		GameRegistry.registerBlock(ModBlocks.amorphousEldritchMetal, "amorphousEldritchMetal");
		GameRegistry.registerBlock(ModBlocks.eldritchStone, "eldritchStone");
		GameRegistry.registerBlock(ModBlocks.aOTDBarrier, "aOTDBarrier");
		GameRegistry.registerBlock(ModBlocks.gnomishMetalPlate, "gnomishMetalPlate");
		GameRegistry.registerBlock(ModBlocks.gnomishMetalStrut, "gnomishMetalStrut");
		GameRegistry.registerBlock(ModBlocks.glowStalk, "glowStalk");
		GameRegistry.registerBlock(ModBlocks.enariaSpawner, "enariaSpawner");
		GameRegistry.registerTileEntity(TileEntityEnariaSpawner.class, "tileEntityEnariaSpawner");
		GameRegistry.registerTileEntity(TileEntityGhastlyEnariaSpawner.class, "tileEntityGhastlyEnariaSpawner");
		GameRegistry.registerBlock(ModBlocks.enariasAltar, "enariasAltar");
	}
}
