/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.biomes;

import java.util.Random;

import net.minecraft.block.BlockDirt;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;

import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.CryptModel;

public class BiomeErieForest extends BiomeGenBase
{
	/*
	 * This will be the erie biome creation class
	 */
	public BiomeErieForest(final int biomeID)
	{
		// This sets the features of the erie biome
		super(biomeID);
		this.biomeName = "Erie Forest";
		this.color = 0x000099;
		this.enableRain = true;
		this.setFillerBlockMetadata(5159473);
		this.flowers.clear();
		this.setHeight(new BiomeGenBase.Height(0.125F, 0.05F));
		this.spawnableCreatureList.clear();
		this.waterColorMultiplier = 0x000099;
		this.theBiomeDecorator.treesPerChunk = 15;
		this.worldGeneratorBigTree = new WorldGenBigTree(false);
		this.topBlock = Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL);
	}

	@Override
	public WorldGenAbstractTree genBigTreeChance(final Random p_150567_1_)
	{
		return (new WorldGenGravewoodTrees(false));
	}

	// Called every chunk gen
	@Override
	public void func_180624_a(final World world, final Random random, BlockPos blockPosition)
	{
		if (random.nextDouble() < 0.003)
		{
			int y;
			try
			{
				y = Utility.getPlaceToSpawnAverage(world, blockPosition.getX() + 12, blockPosition.getZ() + 12, 8, 8);
			}
			catch (Exception e)
			{
				y = 0;
			}

			blockPosition = new BlockPos(blockPosition.getX(), y - 17, blockPosition.getZ());
			LogHelper.info("Chose: " + blockPosition.getX() + ", " + blockPosition.getY() + ", " + blockPosition.getZ());
			new CryptModel(world, random, blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
		}

		super.func_180624_a(world, random, blockPosition);
	}
}
