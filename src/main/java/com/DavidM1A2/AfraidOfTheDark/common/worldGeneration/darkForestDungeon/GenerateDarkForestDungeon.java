package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.darkForestDungeon;

import java.util.Random;

import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.UnsupportedLocationException;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

public class GenerateDarkForestDungeon
{
	public GenerateDarkForestDungeon(Random random, int chunkX, int chunkZ, World world)
	{
		this.generateSurface(world, random, chunkX, chunkZ);
	}

	private void generateSurface(World world, Random random, int chunkX, int chunkZ)
	{
		try
		{
			Utility.getPlaceToSpawn(world, chunkX, chunkZ, 23, 23);
			LogHelper.info("Generating dark forest at " + chunkX + ", " + chunkZ);

			for (int i = 0; i < 25; i++)
			{
				try
				{
					this.generateSurroundingObject(world, random, chunkX, chunkZ);
				}
				catch (UnsupportedLocationException e)
				{
				}
			}
			try
			{
				this.generateBedHouse(world, random, chunkX, chunkZ);
			}
			catch (UnsupportedLocationException e)
			{
			}
			for (int i = 0; i < 30; i++)
			{
				try
				{
					this.generateSurroundingTree(world, random, chunkX, chunkZ);
				}
				catch (UnsupportedLocationException e)
				{
				}
			}
		}
		catch (UnsupportedLocationException e)
		{
		}
	}

	private void generateSurroundingTree(World world, Random random, int chunkX, int chunkZ) throws UnsupportedLocationException
	{
		int placeX = 0;
		int placeZ = 0;

		switch (random.nextInt(4))
		{
			case 0:
				placeX = chunkX;
				placeZ = chunkZ;

				placeX = placeX + randInt(random, 46, -23);
				placeZ = placeZ + randInt(random, -30, -7);
				break;
			case 1:
				placeX = chunkX;
				placeZ = chunkZ + 23;

				placeX = placeX + randInt(random, -7, -30);
				placeZ = placeZ + randInt(random, 23, -46);
				break;
			case 2:
				placeX = chunkX + 23;
				placeZ = chunkZ + 23;

				placeX = placeX + randInt(random, 23, -46);
				placeZ = placeZ + randInt(random, 7, 30);
				break;
			case 3:
				placeX = chunkX + 23;
				placeZ = chunkZ;

				placeX = placeX + randInt(random, -23, 46);
				placeZ = placeZ + randInt(random, -7, -30);
				break;
			default:
				break;
		}

		switch (random.nextInt(5))
		{
			case 0:
				new TreeSmall(world, random, placeX - 38 / 2, Utility.getPlaceToSpawn(world, placeX - 38 / 2, placeZ - 38 / 2, 5, 5) - 5, placeZ - 38 / 2);
				break;
			case 1:
				new TreeLargeCircle(world, random, placeX - 49 / 2, Utility.getPlaceToSpawn(world, placeX - 49 / 2, placeZ - 52 / 2, 5, 5) - 5, placeZ - 52 / 2);
				break;
			case 2:
				new TreeLargeDonut(world, random, placeX - 55 / 2, Utility.getPlaceToSpawn(world, placeX - 55 / 2, placeZ - 57 / 2, 5, 5) - 5, placeZ - 57 / 2);
				break;
			case 3:
				new TreeBranchyType1(world, random, placeX - 49 / 2, Utility.getPlaceToSpawn(world, placeX - 49 / 2, placeZ - 54 / 2, 5, 5) - 5, placeZ - 54 / 2);
				break;
			case 4:
				new TreeBranchyType2(world, random, placeX - 55 / 2, Utility.getPlaceToSpawn(world, placeX - 55 / 2, placeZ - 55 / 2, 5, 5) - 5, placeZ - 55 / 2);
				break;
			default:
				break;
		}
	}

	private void generateSurroundingObject(World world, Random random, int chunkX, int chunkZ) throws UnsupportedLocationException
	{
		int placeX = 0;
		int placeZ = 0;

		switch (random.nextInt(4))
		{
			case 0:
				placeX = chunkX;
				placeZ = chunkZ;

				placeX = placeX + randInt(random, 46, -23);
				placeZ = placeZ + randInt(random, -30, 0);
				break;
			case 1:
				placeX = chunkX;
				placeZ = chunkZ + 23;

				placeX = placeX + randInt(random, 0, -30);
				placeZ = placeZ + randInt(random, 23, -46);
				break;
			case 2:
				placeX = chunkX + 23;
				placeZ = chunkZ + 23;

				placeX = placeX + randInt(random, 23, -46);
				placeZ = placeZ + randInt(random, 0, 30);
				break;
			case 3:
				placeX = chunkX + 23;
				placeZ = chunkZ;

				placeX = placeX + randInt(random, -23, 46);
				placeZ = placeZ + randInt(random, 0, -30);
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

		new BedHouse(world, random, chunkX, y, chunkZ);
	}

	private int randInt(Random random, int min, int max)
	{
		if (min > max)
		{
			int temp = max;
			max = min;
			min = temp;
		}
		return random.nextInt((max - min) + 1) + min;
	}
}
