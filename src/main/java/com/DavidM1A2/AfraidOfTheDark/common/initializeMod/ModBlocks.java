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
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockGlowStalk;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockGnomishMetalPlate;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockGnomishMetalStrut;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockGravewood;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockGravewoodDoubleSlab;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockGravewoodHalfSlab;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockGravewoodLeaves;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockGravewoodPlanks;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockGravewoodSapling;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockGravewoodStairs;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockIgneous;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockMeteor;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockSpring;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockStarMetalOre;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockSunstoneOre;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockVitaeDisenchanter;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockVoidChest;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockVoidChestPortal;
import com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity.TileEntityDarkForest;
import com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity.TileEntityEnariaSpawner;
import com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity.TileEntitySpring;
import com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity.TileEntityVoidChest;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemGravewoodLeaves;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemGravewoodSlab;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Refrence;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@GameRegistry.ObjectHolder(Refrence.MOD_ID)
public class ModBlocks
{
	// Register blocks 
	public static final BlockGravewoodLeaves gravewoodLeaves = new BlockGravewoodLeaves();
	public static final BlockGravewood gravewood = new BlockGravewood();
	public static final BlockGravewoodPlanks gravewoodPlanks = new BlockGravewoodPlanks();
	public static final BlockGravewoodStairs gravewoodStairs = new BlockGravewoodStairs(ModBlocks.gravewoodPlanks.getDefaultState());
	public static final BlockGravewoodHalfSlab gravewoodHalfSlab = new BlockGravewoodHalfSlab(Material.wood);
	public static final BlockGravewoodDoubleSlab gravewoodDoubleSlab = new BlockGravewoodDoubleSlab(Material.wood);
	public static final BlockSunstoneOre sunstoneOre = new BlockSunstoneOre(Material.rock);
	public static final BlockAstralSilverOre astralSilverOre = new BlockAstralSilverOre(Material.rock);
	public static final BlockMeteor meteor = new BlockMeteor(Material.rock);
	public static final BlockStarMetalOre starMetalOre = new BlockStarMetalOre(Material.rock);
	public static final BlockIgneous igneousBlock = new BlockIgneous();
	public static final BlockSpring spring = new BlockSpring(Material.rock);
	public static final BlockDarkForest darkForest = new BlockDarkForest(Material.rock);
	public static final BlockGravewoodSapling gravewoodSapling = new BlockGravewoodSapling();
	public static final BlockVitaeDisenchanter vitaeDisenchanter = new BlockVitaeDisenchanter();
	public static final BlockVoidChest voidChest = new BlockVoidChest();
	public static final BlockVoidChestPortal voidChestPortal = new BlockVoidChestPortal();
	public static final BlockEldritchObsidian eldritchObsidian = new BlockEldritchObsidian(Material.rock);
	public static final BlockAmorphousEldritchMetal amorphousEldritchMetal = new BlockAmorphousEldritchMetal(Material.portal);
	public static final BlockEldritchStone eldritchStone = new BlockEldritchStone(Material.rock);
	public static final BlockAOTDBarrier aOTDBarrier = new BlockAOTDBarrier();
	public static final BlockGnomishMetalPlate gnomishMetalPlate = new BlockGnomishMetalPlate();
	public static final BlockGnomishMetalStrut gnomishMetalStrut = new BlockGnomishMetalStrut();
	public static final BlockGlowStalk glowStalk = new BlockGlowStalk();
	public static final BlockEnariaSpawner enariaSpawner = new BlockEnariaSpawner(Material.rock);

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
		Blocks.fire.func_180686_a(ModBlocks.gravewoodPlanks, 5, 5);
		GameRegistry.registerBlock(ModBlocks.gravewoodStairs, "gravewoodStairs");
		Blocks.fire.func_180686_a(ModBlocks.gravewoodStairs, 5, 5);
		GameRegistry.registerBlock(ModBlocks.gravewoodHalfSlab, ItemGravewoodSlab.class, "gravewoodHalfSlab", ModBlocks.gravewoodHalfSlab, ModBlocks.gravewoodDoubleSlab, false);
		Blocks.fire.func_180686_a(ModBlocks.gravewoodHalfSlab, 5, 5);
		GameRegistry.registerBlock(ModBlocks.gravewoodDoubleSlab, ItemGravewoodSlab.class, "gravewoodDoubleSlab", ModBlocks.gravewoodHalfSlab, ModBlocks.gravewoodDoubleSlab, true);
		Blocks.fire.func_180686_a(ModBlocks.gravewoodDoubleSlab, 5, 5);
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
	}

	public static void initializeRenderers(final Side side)
	{
		if (side == Side.CLIENT)
		{
			final ItemModelMesher itemModelMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
			itemModelMesher.register(Item.getItemFromBlock(ModBlocks.spring), 0, new ModelResourceLocation(Refrence.MOD_ID + ":spring", "inventory"));
			itemModelMesher.register(Item.getItemFromBlock(ModBlocks.darkForest), 0, new ModelResourceLocation(Refrence.MOD_ID + ":darkForest", "inventory"));
			itemModelMesher.register(Item.getItemFromBlock(ModBlocks.gravewood), 0, new ModelResourceLocation(Refrence.MOD_ID + ":gravewood", "inventory"));
			itemModelMesher.register(Item.getItemFromBlock(ModBlocks.gravewoodLeaves), 0, new ModelResourceLocation(Refrence.MOD_ID + ":gravewoodLeaves", "inventory"));
			itemModelMesher.register(Item.getItemFromBlock(ModBlocks.gravewoodPlanks), 0, new ModelResourceLocation(Refrence.MOD_ID + ":gravewoodPlanks", "inventory"));
			itemModelMesher.register(Item.getItemFromBlock(ModBlocks.gravewoodStairs), 0, new ModelResourceLocation(Refrence.MOD_ID + ":gravewoodStairs", "inventory"));
			itemModelMesher.register(Item.getItemFromBlock(ModBlocks.gravewoodHalfSlab), 0, new ModelResourceLocation(Refrence.MOD_ID + ":gravewoodHalfSlab", "inventory"));
			itemModelMesher.register(Item.getItemFromBlock(ModBlocks.gravewoodDoubleSlab), 0, new ModelResourceLocation(Refrence.MOD_ID + ":gravewoodDoubleSlab", "inventory"));
			itemModelMesher.register(Item.getItemFromBlock(ModBlocks.sunstoneOre), 0, new ModelResourceLocation(Refrence.MOD_ID + ":sunstoneOre", "inventory"));
			itemModelMesher.register(Item.getItemFromBlock(ModBlocks.astralSilverOre), 0, new ModelResourceLocation(Refrence.MOD_ID + ":astralSilverOre", "inventory"));
			itemModelMesher.register(Item.getItemFromBlock(ModBlocks.meteor), 0, new ModelResourceLocation(Refrence.MOD_ID + ":meteor", "inventory"));
			itemModelMesher.register(Item.getItemFromBlock(ModBlocks.starMetalOre), 0, new ModelResourceLocation(Refrence.MOD_ID + ":starMetalOre", "inventory"));
			itemModelMesher.register(Item.getItemFromBlock(ModBlocks.igneousBlock), 0, new ModelResourceLocation(Refrence.MOD_ID + ":igneousBlock", "inventory"));
			itemModelMesher.register(Item.getItemFromBlock(ModBlocks.gravewoodSapling), 0, new ModelResourceLocation(Refrence.MOD_ID + ":gravewoodSapling", "inventory"));
			itemModelMesher.register(Item.getItemFromBlock(ModBlocks.vitaeDisenchanter), 0, new ModelResourceLocation(Refrence.MOD_ID + ":vitaeDisenchanter", "inventory"));
			itemModelMesher.register(Item.getItemFromBlock(ModBlocks.voidChest), 0, new ModelResourceLocation(Refrence.MOD_ID + ":voidChest", "inventory"));
			itemModelMesher.register(Item.getItemFromBlock(ModBlocks.voidChestPortal), 0, new ModelResourceLocation(Refrence.MOD_ID + ":voidChestPortal", "inventory"));
			itemModelMesher.register(Item.getItemFromBlock(ModBlocks.eldritchObsidian), 0, new ModelResourceLocation(Refrence.MOD_ID + ":eldritchObsidian", "inventory"));
			itemModelMesher.register(Item.getItemFromBlock(ModBlocks.amorphousEldritchMetal), 0, new ModelResourceLocation(Refrence.MOD_ID + ":amorphousEldritchMetal", "inventory"));
			itemModelMesher.register(Item.getItemFromBlock(ModBlocks.eldritchStone), 0, new ModelResourceLocation(Refrence.MOD_ID + ":eldritchStone", "inventory"));
			itemModelMesher.register(Item.getItemFromBlock(ModBlocks.aOTDBarrier), 0, new ModelResourceLocation(Refrence.MOD_ID + ":aOTDBarrier", "inventory"));
			itemModelMesher.register(Item.getItemFromBlock(ModBlocks.gnomishMetalPlate), 0, new ModelResourceLocation(Refrence.MOD_ID + ":gnomishMetalPlate", "inventory"));
			itemModelMesher.register(Item.getItemFromBlock(ModBlocks.gnomishMetalStrut), 0, new ModelResourceLocation(Refrence.MOD_ID + ":gnomishMetalStrut", "inventory"));
			itemModelMesher.register(Item.getItemFromBlock(ModBlocks.glowStalk), 0, new ModelResourceLocation(Refrence.MOD_ID + ":glowStalk", "inventory"));
			itemModelMesher.register(Item.getItemFromBlock(ModBlocks.enariaSpawner), 0, new ModelResourceLocation(Refrence.MOD_ID + ":enariaSpawner", "inventory"));
		}
	}
}
