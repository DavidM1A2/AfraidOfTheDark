/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.generatables;

import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.handler.ConfigurationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBiomes;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDLootTables;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDSchematics;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDWorldData;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.SchematicGenerator;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Point3D;
import com.DavidM1A2.AfraidOfTheDark.common.utility.UnsupportedLocationException;
import com.DavidM1A2.AfraidOfTheDark.common.utility.WorldGenerationUtility;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.AOTDDungeonTypes;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class GenerateDarkForestDungeon implements AOTDGeneratable
{
	@Override
	public boolean generate(World world, Random random, int chunkX, int chunkZ)
	{
		if (!AOTDWorldData.get(world).isValidLocation(new Point3D(chunkX + 11, AOTDDungeonTypes.DarkForest.getRadius(), chunkZ + 11), true))
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

		AOTDWorldData.get(world).addDungeon(new Point3D(chunkX + 11, AOTDDungeonTypes.DarkForest.getRadius(), chunkZ + 11), true);

		if (ConfigurationHandler.debugMessages)
		{
			LogHelper.info("Spawning a dark forest at x = " + chunkX + ", y = " + y + ", z = " + chunkZ);
		}

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

		return true;
	}

	@Override
	public double getGenerationChance(int biomeID)
	{
		// Plains
		if (biomeID == 1)
			return ConfigurationHandler.darkForestFrequency * 0.3 * ConfigurationHandler.dungeonFrequencyMultiplier;
		else if (biomeID == Biome.getIdForBiome(ModBiomes.erieForest))
			return ConfigurationHandler.darkForestFrequency * ConfigurationHandler.dungeonFrequencyMultiplier;
		// Savanah
		else if (biomeID == 35)
			return ConfigurationHandler.darkForestFrequency * 0.6 * ConfigurationHandler.dungeonFrequencyMultiplier;
		return 0;
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
				adjustedX = placeX - AOTDSchematics.TreeSmall.getSchematic().getWidth() / 2;
				adjustedZ = placeZ - AOTDSchematics.TreeSmall.getSchematic().getHeight() / 2;
				SchematicGenerator.generateSchematic(AOTDSchematics.TreeSmall.getSchematic(), world, adjustedX, WorldGenerationUtility.getPlaceToSpawnLowest(world, placeX - 4, placeZ - 4, 6, 6) - 4, adjustedZ);
				break;
			case 1:
				adjustedX = placeX - AOTDSchematics.TreeLargeCircle.getSchematic().getWidth() / 2;
				adjustedZ = placeZ - AOTDSchematics.TreeLargeCircle.getSchematic().getHeight() / 2;
				SchematicGenerator.generateSchematic(AOTDSchematics.TreeLargeCircle.getSchematic(), world, adjustedX, WorldGenerationUtility.getPlaceToSpawnLowest(world, placeX - 4, placeZ - 4, 5, 5) - 4, adjustedZ);
				break;
			case 2:
				adjustedX = placeX - AOTDSchematics.TreeLargeDonut.getSchematic().getWidth() / 2;
				adjustedZ = placeZ - AOTDSchematics.TreeLargeDonut.getSchematic().getHeight() / 2;
				SchematicGenerator.generateSchematic(AOTDSchematics.TreeLargeDonut.getSchematic(), world, adjustedX, WorldGenerationUtility.getPlaceToSpawnLowest(world, placeX - 2, placeZ + 1, 5, 5) - 3, adjustedZ);
				break;
			case 3:
				adjustedX = placeX - AOTDSchematics.TreeBranchyType1.getSchematic().getWidth() / 2;
				adjustedZ = placeZ - AOTDSchematics.TreeBranchyType1.getSchematic().getHeight() / 2;
				SchematicGenerator.generateSchematic(AOTDSchematics.TreeBranchyType1.getSchematic(), world, adjustedX, WorldGenerationUtility.getPlaceToSpawnLowest(world, placeX - 7, placeZ + 1, 5, 5) - 3, adjustedZ);
				break;
			case 4:
				adjustedX = placeX - AOTDSchematics.TreeBranchyType2.getSchematic().getWidth() / 2;
				adjustedZ = placeZ - AOTDSchematics.TreeBranchyType2.getSchematic().getHeight() / 2;
				SchematicGenerator.generateSchematic(AOTDSchematics.TreeBranchyType2.getSchematic(), world, adjustedX, WorldGenerationUtility.getPlaceToSpawnLowest(world, placeX + 2, placeZ - 7, 5, 5) - 3, adjustedZ);
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
				SchematicGenerator.generateSchematic(AOTDSchematics.PropBush1.getSchematic(), world, placeX, WorldGenerationUtility.getPlaceToSpawnAverage(world, placeX, placeZ, 4, 3), placeZ);
				break;
			case 1:
				SchematicGenerator.generateSchematic(AOTDSchematics.PropFallenOverLog.getSchematic(), world, placeX, WorldGenerationUtility.getPlaceToSpawnAverage(world, placeX, placeZ, 17, 11), placeZ);
				break;
			case 2:
				SchematicGenerator.generateSchematic(AOTDSchematics.PropFence1.getSchematic(), world, placeX, WorldGenerationUtility.getPlaceToSpawnAverage(world, placeX, placeZ, 1, 6), placeZ);
				break;
			case 3:
				SchematicGenerator.generateSchematic(AOTDSchematics.PropFence2.getSchematic(), world, placeX, WorldGenerationUtility.getPlaceToSpawnAverage(world, placeX, placeZ, 3, 1), placeZ);
				break;
			case 4:
				SchematicGenerator.generateSchematic(AOTDSchematics.PropFountain.getSchematic(), world, placeX, WorldGenerationUtility.getPlaceToSpawnAverage(world, placeX, placeZ, 5, 6), placeZ);
				break;
			case 5:
				SchematicGenerator.generateSchematic(AOTDSchematics.PropLog.getSchematic(), world, placeX, WorldGenerationUtility.getPlaceToSpawnAverage(world, placeX, placeZ, 2, 4), placeZ);
				break;
			case 6:
				SchematicGenerator.generateSchematic(AOTDSchematics.PropPumpkin1.getSchematic(), world, placeX, WorldGenerationUtility.getPlaceToSpawnAverage(world, placeX, placeZ, 4, 5), placeZ);
				break;
			case 7:
				SchematicGenerator.generateSchematic(AOTDSchematics.PropPumpkin2.getSchematic(), world, placeX, WorldGenerationUtility.getPlaceToSpawnAverage(world, placeX, placeZ, 4, 5), placeZ);
				break;
			case 8:
				SchematicGenerator.generateSchematic(AOTDSchematics.PropStump.getSchematic(), world, placeX, WorldGenerationUtility.getPlaceToSpawnAverage(world, placeX, placeZ, 3, 3), placeZ);
				break;
			default:
				break;
		}
	}

	private void generateBedHouse(World world, Random random, int chunkX, int y, int chunkZ) throws UnsupportedLocationException
	{
		SchematicGenerator.generateSchematicWithLoot(AOTDSchematics.BedHouse.getSchematic(), world, chunkX - 2, y, chunkZ - 2, AOTDLootTables.DarkForest.getLootTable());
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
