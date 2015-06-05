package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration;

import java.util.Random;

import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockGrass;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenPlains;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

import com.DavidM1A2.AfraidOfTheDark.common.biomes.BiomeErieForest;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.UnsupportedLocationException;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

public class GenerateSprings implements IWorldGenerator
{
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		switch (world.provider.getDimensionId())
		{
			case 0:
			{
				generateSurface(world, random, chunkX * 16, chunkZ * 16);
			}
		}
	}

	private void generateSurface(World world, Random random, int chunkX, int chunkZ)
	{
		if (world.getBiomeGenForCoords(new BlockPos(chunkX, 50, chunkZ)) instanceof BiomeGenPlains || world.getBiomeGenForCoords(new BlockPos(chunkX * 16, 50, chunkZ * 16)) instanceof BiomeErieForest)
		{
			if (random.nextDouble() < 0.003)
			{
				for (int i = 255; i > 0; i--)
				{
					if (!(world.getBlockState(new BlockPos(chunkX, i, chunkZ)).getBlock() instanceof BlockAir) && world.getBlockState(new BlockPos(chunkX, i, chunkZ)).getBlock() instanceof BlockGrass)
					{
						int y;
						try
						{
							y = Utility.getPlaceToSpawn(world, chunkX, chunkZ, 28, 28);
						}
						catch (UnsupportedLocationException e)
						{
							y = 0;
						}
						if (y != 0)
						{
							LogHelper.info("Chose the location: x = " + chunkX + ", y = " + y + ", z = " + chunkZ);
							new Spring(world, random, chunkX, y - 2, chunkZ);
							break;
						}
					}
				}
			}
		}
	}
}
