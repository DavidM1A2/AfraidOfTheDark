/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.biomes;

import java.util.Random;

import net.minecraft.block.BlockDirt;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;

import com.DavidM1A2.AfraidOfTheDark.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.worldGeneration.CryptModel;

public class BiomeErieForest extends BiomeGenBase
{
	/*
	 * This will be the erie biome creation class
	 */
	public BiomeErieForest(int biomeID)
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

	public WorldGenAbstractTree genBigTreeChance(Random p_150567_1_)
	{
		return (WorldGenAbstractTree) (new WorldGenGravewoodTrees(false));
	}

	// Called every chunk gen
	public void func_180624_a(World world, Random random, BlockPos blockPosition)
	{
		if (random.nextDouble() < 0.01)
		{
			BlockPos newPos = new BlockPos(blockPosition.getX(), 255, blockPosition.getZ());
			for (int i = 255; i > 0; i--)
			{
				if (world.getBlockState(newPos).getBlock() instanceof BlockDirt)
				{
					newPos = new BlockPos(newPos.getX(), newPos.getY() - 17, newPos.getZ());
					LogHelper.info("Chose: " + newPos.getX() + ", " + newPos.getY() + ", " + newPos.getZ());
					new CryptModel().generate(world, random, newPos.getX(), newPos.getY(), newPos.getZ());
					break;
				}
				else
				{
					newPos = new BlockPos(newPos.getX(), newPos.getY() - 1, newPos.getZ());
				}
			}
		}

		super.func_180624_a(world, random, blockPosition);
	}
}
