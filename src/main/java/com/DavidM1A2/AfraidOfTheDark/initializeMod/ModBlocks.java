/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.initializeMod;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

import com.DavidM1A2.AfraidOfTheDark.block.BlockDarkness;
import com.DavidM1A2.AfraidOfTheDark.block.BlockGravewood;
import com.DavidM1A2.AfraidOfTheDark.block.BlockGravewoodLeaves;
import com.DavidM1A2.AfraidOfTheDark.block.BlockSilverOre;
import com.DavidM1A2.AfraidOfTheDark.block.BlockTileEntityDarkness;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

import cpw.mods.fml.common.registry.GameRegistry;

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
		Blocks.fire.setFireInfo(gravewoodLeaves, 5, 5);
		GameRegistry.registerBlock(gravewood, "gravewood");
		Blocks.fire.setFireInfo(gravewood, 5, 5);
	}
}
