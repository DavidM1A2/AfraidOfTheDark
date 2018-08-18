package com.DavidM1A2.afraidofthedark.common.biomes;

import com.DavidM1A2.afraidofthedark.common.biomes.extras.AOTDWorldGenBigTree;
import com.DavidM1A2.afraidofthedark.common.constants.ModBlocks;
import com.google.common.collect.Lists;
import com.sun.javafx.UnmodifiableArrayList;
import javafx.scene.paint.Color;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockLog;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenTrees;

import java.util.Random;

/**
 * Class representing the Erie Forest biome
 */
public class BiomeErieForest extends Biome
{
	/**
	 * Constructor initializes the biome's fields
	 */
	public BiomeErieForest()
	{
		// Set this biome's properties. It takes height, variation, water color, and a name
		super(new BiomeProperties("Erie Forest")
				.setWaterColor(0x000099)
				.setBaseHeight(0.125f)
				.setHeightVariation(0.05f));
		this.decorator.grassPerChunk = 10;
		// Set the biome's registry name to erieForest
		this.setRegistryName("erieForest");
		// Use stone as the underground filler block
		this.fillerBlock = Blocks.STONE.getDefaultState();
		// We will have no flowers in this biome
		this.flowers.clear();
		// We have lots of trees per chunk
		this.decorator.treesPerChunk = 10;
		this.decorator.flowersPerChunk = 0;
		this.decorator.mushroomsPerChunk = 0;
		// The top block of the biome is dirt
		this.topBlock = Blocks.GRASS.getDefaultState();
	}

	/**
	 * Use a brown grass color
	 *
	 * @param original The original grass color
	 * @return The new grass color
	 */
	@Override
	public int getModdedBiomeGrassColor(int original)
	{
		// hash code converts from color object to 32-bit integer, then get rid of the alpha parameter
		return Color.rgb(83, 56, 6).hashCode() >> 8;
	}

	/**
	 * Getter for the tree generator
	 *
	 * @param rand Random object to be used by the generator
	 * @return The biome tree generator
	 */
	@Override
	public WorldGenAbstractTree getRandomTreeFeature(Random rand)
	{
		// Every 3 trees is a big tree
		if (rand.nextInt(3) == 0)
			return new AOTDWorldGenBigTree(true, ModBlocks.GRAVEWOOD.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y), ModBlocks.GRAVEWOOD_LEAVES.getDefaultState())
					.setLeafIntegrity(0.9)
					.setTrunkSize(2);
		else
			return new WorldGenTrees(true, 6, ModBlocks.GRAVEWOOD.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y), ModBlocks.GRAVEWOOD_LEAVES.getDefaultState(), false);
	}
}
