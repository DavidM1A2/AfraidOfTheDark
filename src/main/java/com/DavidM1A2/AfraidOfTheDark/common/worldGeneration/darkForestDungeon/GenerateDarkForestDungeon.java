package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.darkForestDungeon;

import java.util.Random;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenForest;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

import com.DavidM1A2.AfraidOfTheDark.common.biomes.BiomeErieForest;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.UnsupportedLocationException;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

public class GenerateDarkForestDungeon implements IWorldGenerator
{
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		switch (world.provider.getDimensionId())
		{
			case 0:
			{
				this.generateSurface(world, random, chunkX * 16, chunkZ * 16);
			}
		}
	}

	private void generateSurface(World world, Random random, int chunkX, int chunkZ)
	{
		if (world.getBiomeGenForCoords(new BlockPos(chunkX, 50, chunkZ)) instanceof BiomeGenForest || world.getBiomeGenForCoords(new BlockPos(chunkX, 50, chunkZ)) instanceof BiomeErieForest)
		{
			if (random.nextDouble() < 0.003)
			{
				try
				{
					this.generateBedHouse(world, random, chunkX, chunkZ);
					LogHelper.info("Generating dark forest at " + chunkX + ", " + chunkZ);
					for (int i = 0; i < 20; i++)
					{
						try
						{
							this.generateSurroundingObject(world, random, chunkX, chunkZ);
						}
						catch (UnsupportedLocationException e)
						{
						}
					}
					for (int i = 0; i < 20; i++)
					{
						try
						{
							this.generateSurroundingTree(world, random, chunkX, chunkZ);
						}
						catch (UnsupportedLocationException e)
						{
							LogHelper.info(e.getMessage());
						}
					}
				}
				catch (UnsupportedLocationException e)
				{
				}
			}
		}
	}

	private void generateSurroundingTree(World world, Random random, int chunkX, int chunkZ) throws UnsupportedLocationException
	{
		int placeX = 0;
		int placeZ = 0;

		switch (random.nextInt(4))
		{
			case 0:
				placeX = chunkX + randInt(random, -23, 10);
				placeZ = chunkZ + randInt(random, -30, -15);
				break;
			case 1:
				placeX = chunkX + randInt(random, 15, 30);
				placeZ = chunkZ + 23;
				placeZ = placeZ + randInt(random, -23, 15);
				break;
			case 2:
				placeX = chunkX - 23;
				placeZ = chunkZ + 23;
				placeX = placeX + randInt(random, -15, 23);
				placeZ = placeZ + randInt(random, 15, 30);
				break;
			case 3:
				placeX = chunkX - 23;
				placeX = chunkX + randInt(random, -30, -15);
				placeZ = chunkZ + randInt(random, -15, 23);
				break;
			default:
				break;
		}

		switch (random.nextInt(5))
		{
			case 0:
				new TreeSmall(world, random, placeX - 38 / 2, Utility.getPlaceToSpawn(world, placeX - 38 / 2, placeZ - 38 / 2, 5, 5) - 2, placeZ - 38 / 2);
				break;
			case 1:
				new TreeLargeCircle(world, random, placeX - 49 / 2, Utility.getPlaceToSpawn(world, placeX - 49 / 2, placeZ - 52 / 2, 5, 5) - 2, placeZ - 52 / 2);
				break;
			case 2:
				new TreeLargeDonut(world, random, placeX - 55 / 2, Utility.getPlaceToSpawn(world, placeX - 55 / 2, placeZ - 57 / 2, 5, 5) - 2, placeZ - 57 / 2);
				break;
			case 3:
				new TreeBranchyType1(world, random, placeX - 49 / 2, Utility.getPlaceToSpawn(world, placeX - 49 / 2, placeZ - 54 / 2, 5, 5) - 2, placeZ - 54 / 2);
				break;
			case 4:
				new TreeBranchyType2(world, random, placeX - 55 / 2, Utility.getPlaceToSpawn(world, placeX - 55 / 2, placeZ - 55 / 2, 5, 5) - 2, placeZ - 55 / 2);
				break;
			default:
				break;
		}
	}

	private void generateSurroundingObject(World world, Random random, int chunkX, int chunkZ) throws UnsupportedLocationException
	{
		int placeX = 0;
		int placeZ = 0;

		switch (random.nextInt(6))
		{
			case 0:
				placeX = chunkX - random.nextInt(10) - 5;
				placeZ = chunkZ - random.nextInt(10) - 5;
				break;
			case 1:
				placeX = chunkX + random.nextInt(10) + 5;
				placeZ = chunkZ - random.nextInt(10) - 5;
				break;
			case 2:
				placeX = chunkX - random.nextInt(10) - 5;
				placeZ = chunkZ + random.nextInt(10) + 5;
				break;
			case 3:
				placeX = chunkX + 23 + random.nextInt(10) + 5;
				placeZ = chunkZ + 23 + random.nextInt(10) + 5;
				break;
			case 4:
				placeX = chunkX + 23 - random.nextInt(10) - 5;
				placeZ = chunkZ + 23 + random.nextInt(10) + 5;
				break;
			case 5:
				placeX = chunkX + 23 + random.nextInt(10) + 5;
				placeZ = chunkZ + 23 - random.nextInt(10) - 5;
				break;
			default:
				break;
		}

		switch (random.nextInt(9))
		{
			case 0:
				new PropBush1(world, random, placeX, Utility.getPlaceToSpawn(world, placeX, placeZ, 4, 3), placeZ);
				break;
			case 1:
				new PropFallenOverLog(world, random, placeX, Utility.getPlaceToSpawn(world, placeX, placeZ, 17, 11), placeZ);
				break;
			case 2:
				new PropFence1(world, random, placeX, Utility.getPlaceToSpawn(world, placeX, placeZ, 1, 6), placeZ);
				break;
			case 3:
				new PropFence2(world, random, placeX, Utility.getPlaceToSpawn(world, placeX, placeZ, 3, 1), placeZ);
				break;
			case 4:
				new PropFountain(world, random, placeX, Utility.getPlaceToSpawn(world, placeX, placeZ, 5, 6), placeZ);
				break;
			case 5:
				new PropLog(world, random, placeX, Utility.getPlaceToSpawn(world, placeX, placeZ, 2, 4), placeZ);
				break;
			case 6:
				new PropPumpkin1(world, random, placeX, Utility.getPlaceToSpawn(world, placeX, placeZ, 4, 5), placeZ);
				break;
			case 7:
				new PropPumpkin2(world, random, placeX, Utility.getPlaceToSpawn(world, placeX, placeZ, 4, 5), placeZ);
				break;
			case 8:
				new PropStump(world, random, placeX, Utility.getPlaceToSpawn(world, placeX, placeZ, 3, 3), placeZ);
				break;
			default:
				break;
		}
	}

	private void generateBedHouse(World world, Random random, int chunkX, int chunkZ) throws UnsupportedLocationException
	{
		int y = Utility.getPlaceToSpawn(world, chunkX, chunkZ, 23, 23);

		new BedHouse(world, random, chunkX, y - 1, chunkZ);
	}

	private int randInt(Random random, int min, int max)
	{
		return random.nextInt((max - min) + 1) + min;
	}
}
