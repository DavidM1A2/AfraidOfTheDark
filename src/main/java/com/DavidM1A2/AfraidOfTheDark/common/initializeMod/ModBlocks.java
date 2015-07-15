/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.initializeMod;

import com.DavidM1A2.AfraidOfTheDark.common.block.BlockAstralSilverOre;
import com.DavidM1A2.AfraidOfTheDark.common.block.BlockDarkForest;
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
import com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity.TileEntityDarkForest;
import com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity.TileEntitySpring;
import com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity.TileEntityVoidChest;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemGravewoodLeaves;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemGravewoodSlab;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Refrence;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
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
	}

	public static void initializeRenderers(final Side side)
	{
		if (side == Side.CLIENT)
		{
			final RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.spring), 0, new ModelResourceLocation(Refrence.MOD_ID + ":spring", "inventory"));
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.darkForest), 0, new ModelResourceLocation(Refrence.MOD_ID + ":darkForest", "inventory"));
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.gravewood), 0, new ModelResourceLocation(Refrence.MOD_ID + ":gravewood", "inventory"));
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.gravewoodLeaves), 0, new ModelResourceLocation(Refrence.MOD_ID + ":gravewoodLeaves", "inventory"));
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.gravewoodPlanks), 0, new ModelResourceLocation(Refrence.MOD_ID + ":gravewoodPlanks", "inventory"));
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.gravewoodStairs), 0, new ModelResourceLocation(Refrence.MOD_ID + ":gravewoodStairs", "inventory"));
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.gravewoodHalfSlab), 0, new ModelResourceLocation(Refrence.MOD_ID + ":gravewoodHalfSlab", "inventory"));
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.gravewoodDoubleSlab), 0, new ModelResourceLocation(Refrence.MOD_ID + ":gravewoodDoubleSlab", "inventory"));
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.sunstoneOre), 0, new ModelResourceLocation(Refrence.MOD_ID + ":sunstoneOre", "inventory"));
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.astralSilverOre), 0, new ModelResourceLocation(Refrence.MOD_ID + ":astralSilverOre", "inventory"));
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.meteor), 0, new ModelResourceLocation(Refrence.MOD_ID + ":meteor", "inventory"));
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.starMetalOre), 0, new ModelResourceLocation(Refrence.MOD_ID + ":starMetalOre", "inventory"));
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.igneousBlock), 0, new ModelResourceLocation(Refrence.MOD_ID + ":igneousBlock", "inventory"));
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.gravewoodSapling), 0, new ModelResourceLocation(Refrence.MOD_ID + ":gravewoodSapling", "inventory"));
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.vitaeDisenchanter), 0, new ModelResourceLocation(Refrence.MOD_ID + ":vitaeDisenchanter", "inventory"));
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.voidChest), 0, new ModelResourceLocation(Refrence.MOD_ID + ":voidChest", "inventory"));
		}
	}
}
