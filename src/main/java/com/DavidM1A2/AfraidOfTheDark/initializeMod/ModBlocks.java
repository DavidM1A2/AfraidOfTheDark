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
import com.DavidM1A2.AfraidOfTheDark.block.BlockGravewoodLeaves;
import com.DavidM1A2.AfraidOfTheDark.block.BlockSilverOre;
import com.DavidM1A2.AfraidOfTheDark.block.BlockTileEntityDarkness;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

@GameRegistry.ObjectHolder(Refrence.MOD_ID)
public class ModBlocks
{
	// Register blocks
	public static final BlockSilverOre silverOre = new BlockSilverOre();
	public static final BlockDarkness darkness = new BlockDarkness(Material.ground);
	public static final BlockGravewoodLeaves gravewoodLeaves = new BlockGravewoodLeaves();
	public static final BlockGravewood gravewood = new BlockGravewood();

	public static void initialize()
	{
		// Register the items, allow gravewood to burn, and register tileEntities
		GameRegistry.registerBlock(silverOre, "silverOre");
		GameRegistry.registerBlock(darkness, "darkness");
		GameRegistry.registerTileEntity(BlockTileEntityDarkness.class, "teDarkness");
		GameRegistry.registerBlock(gravewoodLeaves, "gravewoodLeaves");
		Blocks.fire.func_180686_a(gravewoodLeaves, 5, 5);
		GameRegistry.registerBlock(gravewood, "gravewood");
		Blocks.fire.func_180686_a(gravewood, 5, 5);
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
		}
	}
}
