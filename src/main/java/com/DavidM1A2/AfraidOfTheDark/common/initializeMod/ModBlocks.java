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
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModBlocks
{
	// Register blocks 
	public static final BlockGravewood gravewood = new BlockGravewood();
	public static final BlockGravewoodPlanks gravewoodPlanks = new BlockGravewoodPlanks();
	public static final BlockGravewoodStairs gravewoodStairs = new BlockGravewoodStairs();
	public static final BlockGravewoodHalfSlab gravewoodHalfSlab = new BlockGravewoodHalfSlab();
	public static final BlockGravewoodDoubleSlab gravewoodDoubleSlab = new BlockGravewoodDoubleSlab();
	public static final BlockGravewoodSapling gravewoodSapling = new BlockGravewoodSapling();
	public static final BlockGravewoodLeaves gravewoodLeaves = new BlockGravewoodLeaves();

	public static final BlockMangrove mangrove = new BlockMangrove();
	public static final BlockMangrovePlanks mangrovePlanks = new BlockMangrovePlanks();
	public static final BlockMangroveStairs mangroveStairs = new BlockMangroveStairs();
	public static final BlockMangroveHalfSlab mangroveHalfSlab = new BlockMangroveHalfSlab();
	public static final BlockMangroveDoubleSlab mangroveDoubleSlab = new BlockMangroveDoubleSlab();
	public static final BlockMangroveSapling mangroveSapling = new BlockMangroveSapling();
	public static final BlockMangroveLeaves mangroveLeaves = new BlockMangroveLeaves();

	public static final BlockSunstoneOre sunstoneOre = new BlockSunstoneOre();
	public static final BlockAstralSilverOre astralSilverOre = new BlockAstralSilverOre();
	public static final BlockMeteor meteor = new BlockMeteor();
	public static final BlockStarMetalOre starMetalOre = new BlockStarMetalOre();
	public static final BlockIgneous igneousBlock = new BlockIgneous();
	public static final BlockSpring spring = new BlockSpring();
	public static final BlockDarkForest darkForest = new BlockDarkForest();
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
		GameRegistry.register(ModBlocks.spring);
		GameRegistry.register(new ItemBlock(ModBlocks.spring).setRegistryName("spring"));
		GameRegistry.registerTileEntity(TileEntitySpring.class, "tileEntitySpring");
		GameRegistry.register(ModBlocks.darkForest);
		GameRegistry.registerTileEntity(TileEntityDarkForest.class, "tileEntityDarkForest");

		GameRegistry.register(ModBlocks.gravewood);
		GameRegistry.register(ModBlocks.gravewoodPlanks);
		Blocks.FIRE.setFireInfo(ModBlocks.gravewoodPlanks, 5, 5);
		GameRegistry.register(ModBlocks.gravewoodStairs);
		Blocks.FIRE.setFireInfo(ModBlocks.gravewoodStairs, 5, 5);
		GameRegistry.register(new ItemGravewoodSlab(ModBlocks.gravewoodHalfSlab, ModBlocks.gravewoodHalfSlab, ModBlocks.gravewoodDoubleSlab, false).setRegistryName("gravewoodHalfSlab"));
		Blocks.FIRE.setFireInfo(ModBlocks.gravewoodHalfSlab, 5, 5);
		GameRegistry.register(new ItemGravewoodSlab(ModBlocks.gravewoodDoubleSlab, ModBlocks.gravewoodHalfSlab, ModBlocks.gravewoodDoubleSlab, true).setRegistryName("gravewoodDoubleSlab"));
		Blocks.FIRE.setFireInfo(ModBlocks.gravewoodDoubleSlab, 5, 5);
		GameRegistry.register(ModBlocks.gravewoodSapling);
		GameRegistry.register(new ItemGravewoodLeaves(ModBlocks.gravewoodLeaves).setRegistryName("gravewoodLeaves"));

		GameRegistry.register(ModBlocks.mangrove);
		Blocks.FIRE.setFireInfo(ModBlocks.mangrove, 5, 3);
		GameRegistry.register(ModBlocks.mangrovePlanks);
		Blocks.FIRE.setFireInfo(ModBlocks.mangrovePlanks, 5, 5);
		GameRegistry.register(ModBlocks.mangroveStairs);
		Blocks.FIRE.setFireInfo(ModBlocks.mangroveStairs, 5, 5);
		GameRegistry.register(new ItemMangroveSlab(ModBlocks.mangroveHalfSlab, ModBlocks.mangroveHalfSlab, ModBlocks.mangroveDoubleSlab, false).setRegistryName("mangroveHalfSlab"));
		Blocks.FIRE.setFireInfo(ModBlocks.mangroveHalfSlab, 5, 5);
		GameRegistry.register(new ItemMangroveSlab(ModBlocks.mangroveDoubleSlab, ModBlocks.mangroveHalfSlab, ModBlocks.mangroveDoubleSlab, true).setRegistryName("mangroveDoubleSlab"));
		Blocks.FIRE.setFireInfo(ModBlocks.mangroveDoubleSlab, 5, 5);
		GameRegistry.register(ModBlocks.mangroveSapling);
		Blocks.FIRE.setFireInfo(ModBlocks.mangroveSapling, 5, 5);
		GameRegistry.register(new ItemMangroveLeaves(ModBlocks.mangroveLeaves).setRegistryName("mangroveLeaves"));
		Blocks.FIRE.setFireInfo(ModBlocks.mangroveLeaves, 5, 5);

		GameRegistry.register(ModBlocks.sunstoneOre);
		GameRegistry.register(ModBlocks.astralSilverOre);
		GameRegistry.register(ModBlocks.meteor);
		GameRegistry.register(ModBlocks.starMetalOre);
		GameRegistry.register(ModBlocks.igneousBlock);
		GameRegistry.register(ModBlocks.vitaeDisenchanter);
		GameRegistry.register(ModBlocks.voidChest);
		GameRegistry.registerTileEntity(TileEntityVoidChest.class, "tileEntityVoidChest");
		GameRegistry.register(ModBlocks.voidChestPortal);
		GameRegistry.register(ModBlocks.eldritchObsidian);
		GameRegistry.register(ModBlocks.amorphousEldritchMetal);
		GameRegistry.register(ModBlocks.eldritchStone);
		GameRegistry.register(ModBlocks.aOTDBarrier);
		GameRegistry.register(ModBlocks.gnomishMetalPlate);
		GameRegistry.register(ModBlocks.gnomishMetalStrut);
		GameRegistry.register(ModBlocks.glowStalk);
		GameRegistry.register(ModBlocks.enariaSpawner);
		GameRegistry.registerTileEntity(TileEntityEnariaSpawner.class, "tileEntityEnariaSpawner");
		GameRegistry.registerTileEntity(TileEntityGhastlyEnariaSpawner.class, "tileEntityGhastlyEnariaSpawner");
		GameRegistry.register(ModBlocks.enariasAltar);
	}
}
