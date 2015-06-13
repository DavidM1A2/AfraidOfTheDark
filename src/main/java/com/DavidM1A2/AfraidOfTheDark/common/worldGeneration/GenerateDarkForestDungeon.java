package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration;

import java.util.Random;

import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.SchematicGenerator;
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
			int y = Utility.getPlaceToSpawnAverage(world, chunkX, chunkZ, 23, 23);
			LogHelper.info("Spawning a dark forest at x = " + chunkX + ", y = " + y + ", z = " + chunkZ);

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
				this.generateBedHouse(world, random, chunkX, y, chunkZ);
			}
			catch (UnsupportedLocationException e)
			{
			}
			for (int i = 0; i < 40; i++)
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

				placeX = placeX + randInt(random, 23, -23);
				placeZ = placeZ + randInt(random, -30, -10);
				break;
			case 1:
				placeX = chunkX;
				placeZ = chunkZ + 23;

				placeX = placeX + randInt(random, -10, -30);
				placeZ = placeZ + randInt(random, -23, 23);
				break;
			case 2:
				placeX = chunkX + 23;
				placeZ = chunkZ + 23;

				placeX = placeX + randInt(random, 23, -23);
				placeZ = placeZ + randInt(random, 10, 30);
				break;
			case 3:
				placeX = chunkX + 23;
				placeZ = chunkZ;

				placeX = placeX + randInt(random, 10, 30);
				placeZ = placeZ + randInt(random, 23, -23);
				break;
			default:
				break;
		}

		int adjustedX;
		int adjustedZ;

		switch (random.nextInt(5))
		{
			case 0:
				adjustedX = placeX - Constants.AOTDSchematics.treeSmall.getWidth() / 2;
				adjustedZ = placeZ - Constants.AOTDSchematics.treeSmall.getHeight() / 2;
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.treeSmall, world, adjustedX, Utility.getPlaceToSpawnLowest(world, placeX - 4, placeZ - 4, 6, 6) - 4, adjustedZ, null, 0);
				break;
			case 1:
				adjustedX = placeX - Constants.AOTDSchematics.treeLargeCircle.getWidth() / 2;
				adjustedZ = placeZ - Constants.AOTDSchematics.treeLargeCircle.getHeight() / 2;
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.treeLargeCircle, world, adjustedX, Utility.getPlaceToSpawnLowest(world, placeX - 4, placeZ - 4, 5, 5) - 4, adjustedZ, null, 0);
				break;
			case 2:
				adjustedX = placeX - Constants.AOTDSchematics.treeLargeDonut.getWidth() / 2;
				adjustedZ = placeZ - Constants.AOTDSchematics.treeLargeDonut.getHeight() / 2;
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.treeLargeDonut, world, adjustedX, Utility.getPlaceToSpawnLowest(world, placeX - 2, placeZ + 1, 5, 5) - 3, adjustedZ, null, 0);
				break;
			case 3:
				adjustedX = placeX - Constants.AOTDSchematics.treeBranchyType1.getWidth() / 2;
				adjustedZ = placeZ - Constants.AOTDSchematics.treeBranchyType1.getHeight() / 2;
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.treeBranchyType1, world, adjustedX, Utility.getPlaceToSpawnLowest(world, placeX - 7, placeZ + 1, 5, 5) - 3, adjustedZ, null, 0);
				break;
			case 4:
				adjustedX = placeX - Constants.AOTDSchematics.treeBranchyType2.getWidth() / 2;
				adjustedZ = placeZ - Constants.AOTDSchematics.treeBranchyType2.getHeight() / 2;
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.treeBranchyType2, world, adjustedX, Utility.getPlaceToSpawnLowest(world, placeX + 2, placeZ - 7, 5, 5) - 3, adjustedZ, null, 0);
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
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.propBush1, world, placeX, Utility.getPlaceToSpawnAverage(world, placeX, placeZ, 4, 3), placeZ, null, 0);
				break;
			case 1:
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.propFallenOverLog, world, placeX, Utility.getPlaceToSpawnAverage(world, placeX, placeZ, 17, 11), placeZ, null, 0);
				break;
			case 2:
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.propFence1, world, placeX, Utility.getPlaceToSpawnAverage(world, placeX, placeZ, 1, 6), placeZ, null, 0);
				break;
			case 3:
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.propFence2, world, placeX, Utility.getPlaceToSpawnAverage(world, placeX, placeZ, 3, 1), placeZ, null, 0);
				break;
			case 4:
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.propFountain, world, placeX, Utility.getPlaceToSpawnAverage(world, placeX, placeZ, 5, 6), placeZ, null, 0);
				break;
			case 5:
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.propLog, world, placeX, Utility.getPlaceToSpawnAverage(world, placeX, placeZ, 2, 4), placeZ, null, 0);
				break;
			case 6:
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.propPumpkin1, world, placeX, Utility.getPlaceToSpawnAverage(world, placeX, placeZ, 4, 5), placeZ, null, 0);
				break;
			case 7:
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.propPumpkin2, world, placeX, Utility.getPlaceToSpawnAverage(world, placeX, placeZ, 4, 5), placeZ, null, 0);
				break;
			case 8:
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.propStump, world, placeX, Utility.getPlaceToSpawnAverage(world, placeX, placeZ, 3, 3), placeZ, null, 0);
				break;
			default:
				break;
		}
	}

	private void generateBedHouse(World world, Random random, int chunkX, int y, int chunkZ) throws UnsupportedLocationException
	{
		SchematicGenerator.generateSchematic(Constants.AOTDSchematics.bedHouse, world, chunkX, y, chunkZ, new DarkForestChestLoot(), 6);
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
