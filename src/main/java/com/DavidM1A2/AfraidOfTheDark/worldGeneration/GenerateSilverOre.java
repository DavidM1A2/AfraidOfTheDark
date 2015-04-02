package com.DavidM1A2.AfraidOfTheDark.worldGeneration;

import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModBlocks;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class GenerateSilverOre implements IWorldGenerator
{
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		switch (world.provider.dimensionId)
		{
			case -1:
			{
				generateNether(world, random, chunkX * 16, chunkZ * 16);
				break;
			}
			case 0:
			{
				generateSurface(world, random, chunkX * 16, chunkZ * 16);
			}
		}
	}
	
	private void generateSurface(World world, Random random, int chunkX, int chunkZ) 
	{
		// The 5 is the amount of spawns per chunk
		for (int i = 0; i < 2; i++)
		{
			// 
			int randomPositionX = chunkX + random.nextInt(16);
			// 64 = 
			int randomPositionY = random.nextInt(64);
			// 
			int randomPositionZ = chunkZ + random.nextInt(16);
			
			// up to 10 per vein
			(new WorldGenMinable(ModBlocks.silverOre, 20)).generate(world, random, randomPositionX, randomPositionY, randomPositionZ);
		}
	}

	private void generateNether(World world, Random random, int chunkX, int chunkZ)
	{
		
	}
}
