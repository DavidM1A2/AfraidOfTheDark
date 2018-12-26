/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.biomes;

import java.util.Random;

import net.minecraft.block.BlockDirt;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomeErieForest extends Biome
{
	/*
	 * This will be the erie biome creation class
	 */
	public BiomeErieForest(final int biomeID)
	{
		// This sets the features of the erie biome
		super(new Biome.BiomeProperties("Erie Forest").setWaterColor(0x000099).setBaseHeight(0.125f).setHeightVariation(0.05f));
		this.fillerBlock = Blocks.STONE.getDefaultState();
		// this.theBiomeDecorator
		this.flowers.clear();
		this.spawnableCreatureList.clear();
		this.theBiomeDecorator.treesPerChunk = 15;
		this.topBlock = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL);
	}

	@Override
	public WorldGenAbstractTree genBigTreeChance(final Random p_150567_1_)
	{
		return (new WorldGenGravewoodTrees(false));
	}
}
