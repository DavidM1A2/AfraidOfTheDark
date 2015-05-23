/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.initializeMod;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import com.DavidM1A2.AfraidOfTheDark.block.BlockDarkness;
import com.DavidM1A2.AfraidOfTheDark.block.BlockGravewood;
import com.DavidM1A2.AfraidOfTheDark.block.BlockGravewoodDoubleSlab;
import com.DavidM1A2.AfraidOfTheDark.block.BlockGravewoodHalfSlab;
import com.DavidM1A2.AfraidOfTheDark.block.BlockGravewoodLeaves;
import com.DavidM1A2.AfraidOfTheDark.block.BlockGravewoodPlanks;
import com.DavidM1A2.AfraidOfTheDark.block.BlockGravewoodStairs;
import com.DavidM1A2.AfraidOfTheDark.block.BlockIgneous;
import com.DavidM1A2.AfraidOfTheDark.block.BlockMeteor;
import com.DavidM1A2.AfraidOfTheDark.block.BlockMeteoricSilverOre;
import com.DavidM1A2.AfraidOfTheDark.block.BlockSilverOre;
import com.DavidM1A2.AfraidOfTheDark.block.BlockStarMetalOre;
import com.DavidM1A2.AfraidOfTheDark.block.BlockSunstoneOre;
import com.DavidM1A2.AfraidOfTheDark.block.BlockTileEntityDarkness;
import com.DavidM1A2.AfraidOfTheDark.item.ItemGravewoodLeaves;
import com.DavidM1A2.AfraidOfTheDark.item.ItemGravewoodSlab;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

@GameRegistry.ObjectHolder(Refrence.MOD_ID)
public class ModBlocks
{
	// Register blocks
	public static final BlockSilverOre silverOre = new BlockSilverOre();
	public static final BlockDarkness darkness = new BlockDarkness(Material.ground);
	public static final BlockGravewoodLeaves gravewoodLeaves = new BlockGravewoodLeaves();
	public static final BlockGravewood gravewood = new BlockGravewood();
	public static final BlockGravewoodPlanks gravewoodPlanks = new BlockGravewoodPlanks();
	public static final BlockGravewoodStairs gravewoodStairs = new BlockGravewoodStairs(gravewoodPlanks.getDefaultState());
	public static final BlockGravewoodHalfSlab gravewoodHalfSlab = new BlockGravewoodHalfSlab(Material.wood);
	public static final BlockGravewoodDoubleSlab gravewoodDoubleSlab = new BlockGravewoodDoubleSlab(Material.wood);
	public static final BlockSunstoneOre sunstone = new BlockSunstoneOre(Material.rock);
	public static final BlockMeteoricSilverOre meteoricSilver = new BlockMeteoricSilverOre(Material.rock);
	public static final BlockMeteor meteor = new BlockMeteor(Material.rock);
	public static final BlockStarMetalOre starMetal = new BlockStarMetalOre(Material.rock);
	public static final BlockIgneous igneousBlock = new BlockIgneous();

	public static void initialize()
	{
		// Register the items, allow gravewood to burn, and register tileEntities
		GameRegistry.registerBlock(silverOre, "silverOre");
		GameRegistry.registerBlock(darkness, "darkness");
		GameRegistry.registerTileEntity(BlockTileEntityDarkness.class, "teDarkness");
		GameRegistry.registerBlock(gravewoodLeaves, ItemGravewoodLeaves.class, "gravewoodLeaves");
		Blocks.fire.func_180686_a(gravewoodLeaves, 5, 5);
		GameRegistry.registerBlock(gravewood, "gravewood");
		Blocks.fire.func_180686_a(gravewood, 5, 5);
		GameRegistry.registerBlock(gravewoodPlanks, "gravewoodPlanks");
		Blocks.fire.func_180686_a(gravewoodPlanks, 5, 5);
		GameRegistry.registerBlock(gravewoodStairs, "gravewoodStairs");
		Blocks.fire.func_180686_a(gravewoodStairs, 5, 5);
		GameRegistry.registerBlock(gravewoodHalfSlab, ItemGravewoodSlab.class, "gravewoodHalfSlab", gravewoodHalfSlab, gravewoodDoubleSlab, false);
		Blocks.fire.func_180686_a(gravewoodHalfSlab, 5, 5);
		GameRegistry.registerBlock(gravewoodDoubleSlab, ItemGravewoodSlab.class, "gravewoodDoubleSlab", gravewoodHalfSlab, gravewoodDoubleSlab, true);
		Blocks.fire.func_180686_a(gravewoodDoubleSlab, 5, 5);
		GameRegistry.registerBlock(sunstone, "sunstone");
		GameRegistry.registerBlock(meteoricSilver, "meteoricSilver");
		GameRegistry.registerBlock(meteor, "meteor");
		GameRegistry.registerBlock(starMetal, "starMetal");
		GameRegistry.registerBlock(igneousBlock, "igneousBlock");
	}

	public static void initializeRenderers(Side side)
	{
		if (side == Side.CLIENT)
		{
			RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(silverOre), 0, new ModelResourceLocation(Refrence.MOD_ID + ":silverOre", "inventory"));
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(darkness), 0, new ModelResourceLocation(Refrence.MOD_ID + ":darkness", "inventory"));
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(gravewood), 0, new ModelResourceLocation(Refrence.MOD_ID + ":gravewood", "inventory"));
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(gravewoodLeaves), 0, new ModelResourceLocation(Refrence.MOD_ID + ":gravewoodLeaves", "inventory"));
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(gravewoodPlanks), 0, new ModelResourceLocation(Refrence.MOD_ID + ":gravewoodPlanks", "inventory"));
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(gravewoodStairs), 0, new ModelResourceLocation(Refrence.MOD_ID + ":gravewoodStairs", "inventory"));
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(gravewoodHalfSlab), 0, new ModelResourceLocation(Refrence.MOD_ID + ":gravewoodHalfSlab", "inventory"));
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(gravewoodDoubleSlab), 0, new ModelResourceLocation(Refrence.MOD_ID + ":gravewoodDoubleSlab", "inventory"));
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(sunstone), 0, new ModelResourceLocation(Refrence.MOD_ID + ":sunstone", "inventory"));
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(meteoricSilver), 0, new ModelResourceLocation(Refrence.MOD_ID + ":meteoricSilver", "inventory"));
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(meteor), 0, new ModelResourceLocation(Refrence.MOD_ID + ":meteor", "inventory"));
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(starMetal), 0, new ModelResourceLocation(Refrence.MOD_ID + ":starMetal", "inventory"));
			renderItem.getItemModelMesher().register(Item.getItemFromBlock(igneousBlock), 0, new ModelResourceLocation(Refrence.MOD_ID + ":igneousBlock"));
		}
	}
}
