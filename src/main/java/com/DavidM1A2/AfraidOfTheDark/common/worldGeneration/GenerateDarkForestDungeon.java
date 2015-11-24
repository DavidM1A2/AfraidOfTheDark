/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration;

import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDWorldData;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.SchematicGenerator;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Point3D;
import com.DavidM1A2.AfraidOfTheDark.common.utility.UnsupportedLocationException;
import com.DavidM1A2.AfraidOfTheDark.common.utility.WorldGenerationUtility;

import net.minecraft.world.World;

public class GenerateDarkForestDungeon
{
	public static boolean generateSurface(World world, Random random, int chunkX, int chunkZ)
	{
		if (!AOTDWorldData.get(world).isValidLocation(new Point3D(chunkX + 11, 85, chunkZ + 11), true))
		{
			return false;
		}

		int y;
		try
		{
			y = WorldGenerationUtility.getPlaceToSpawnAverage(world, chunkX, chunkZ, 23, 23);
		}
		catch (UnsupportedLocationException e)
		{
			return false;
		}

		AOTDWorldData.get(world).addDungeon(new Point3D(chunkX + 11, 85, chunkZ + 11), true);
		AOTDWorldData.get(world).setDirty(true);

		//if (Constants.isDebug)
		{
			LogHelper.info("Spawning a dark forest at x = " + chunkX + ", y = " + y + ", z = " + chunkZ);
		}

		for (int i = 0; i < 25; i++)
		{
			try
			{
				GenerateDarkForestDungeon.generateSurroundingObject(world, random, chunkX, chunkZ);
			}
			catch (UnsupportedLocationException e)
			{
			}
		}

		try
		{
			GenerateDarkForestDungeon.generateBedHouse(world, random, chunkX, y, chunkZ);
		}
		catch (UnsupportedLocationException e)
		{
		}

		for (int i = 0; i < 40; i++)
		{
			try
			{
				GenerateDarkForestDungeon.generateSurroundingTree(world, random, chunkX, chunkZ);
			}
			catch (UnsupportedLocationException e)
			{
			}
		}

		return true;
	}

	private static void generateSurroundingTree(World world, Random random, int chunkX, int chunkZ) throws UnsupportedLocationException
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
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.treeSmall, world, adjustedX, WorldGenerationUtility.getPlaceToSpawnLowest(world, placeX - 4, placeZ - 4, 6, 6) - 4, adjustedZ);
				break;
			case 1:
				adjustedX = placeX - Constants.AOTDSchematics.treeLargeCircle.getWidth() / 2;
				adjustedZ = placeZ - Constants.AOTDSchematics.treeLargeCircle.getHeight() / 2;
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.treeLargeCircle, world, adjustedX, WorldGenerationUtility.getPlaceToSpawnLowest(world, placeX - 4, placeZ - 4, 5, 5) - 4, adjustedZ);
				break;
			case 2:
				adjustedX = placeX - Constants.AOTDSchematics.treeLargeDonut.getWidth() / 2;
				adjustedZ = placeZ - Constants.AOTDSchematics.treeLargeDonut.getHeight() / 2;
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.treeLargeDonut, world, adjustedX, WorldGenerationUtility.getPlaceToSpawnLowest(world, placeX - 2, placeZ + 1, 5, 5) - 3, adjustedZ);
				break;
			case 3:
				adjustedX = placeX - Constants.AOTDSchematics.treeBranchyType1.getWidth() / 2;
				adjustedZ = placeZ - Constants.AOTDSchematics.treeBranchyType1.getHeight() / 2;
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.treeBranchyType1, world, adjustedX, WorldGenerationUtility.getPlaceToSpawnLowest(world, placeX - 7, placeZ + 1, 5, 5) - 3, adjustedZ);
				break;
			case 4:
				adjustedX = placeX - Constants.AOTDSchematics.treeBranchyType2.getWidth() / 2;
				adjustedZ = placeZ - Constants.AOTDSchematics.treeBranchyType2.getHeight() / 2;
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.treeBranchyType2, world, adjustedX, WorldGenerationUtility.getPlaceToSpawnLowest(world, placeX + 2, placeZ - 7, 5, 5) - 3, adjustedZ);
				break;
			default:
				break;
		}
	}

	private static void generateSurroundingObject(World world, Random random, int chunkX, int chunkZ) throws UnsupportedLocationException
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
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.propBush1, world, placeX, WorldGenerationUtility.getPlaceToSpawnAverage(world, placeX, placeZ, 4, 3), placeZ);
				break;
			case 1:
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.propFallenOverLog, world, placeX, WorldGenerationUtility.getPlaceToSpawnAverage(world, placeX, placeZ, 17, 11), placeZ);
				break;
			case 2:
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.propFence1, world, placeX, WorldGenerationUtility.getPlaceToSpawnAverage(world, placeX, placeZ, 1, 6), placeZ);
				break;
			case 3:
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.propFence2, world, placeX, WorldGenerationUtility.getPlaceToSpawnAverage(world, placeX, placeZ, 3, 1), placeZ);
				break;
			case 4:
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.propFountain, world, placeX, WorldGenerationUtility.getPlaceToSpawnAverage(world, placeX, placeZ, 5, 6), placeZ);
				break;
			case 5:
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.propLog, world, placeX, WorldGenerationUtility.getPlaceToSpawnAverage(world, placeX, placeZ, 2, 4), placeZ);
				break;
			case 6:
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.propPumpkin1, world, placeX, WorldGenerationUtility.getPlaceToSpawnAverage(world, placeX, placeZ, 4, 5), placeZ);
				break;
			case 7:
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.propPumpkin2, world, placeX, WorldGenerationUtility.getPlaceToSpawnAverage(world, placeX, placeZ, 4, 5), placeZ);
				break;
			case 8:
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.propStump, world, placeX, WorldGenerationUtility.getPlaceToSpawnAverage(world, placeX, placeZ, 3, 3), placeZ);
				break;
			default:
				break;
		}
	}

	private static void generateBedHouse(World world, Random random, int chunkX, int y, int chunkZ) throws UnsupportedLocationException
	{
		SchematicGenerator.generateSchematicWithLoot(Constants.AOTDSchematics.bedHouse, world, chunkX - 2, y, chunkZ - 2, Constants.darkForestLootTable);
	}

	private static int randInt(Random random, int min, int max)
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
