/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.generatables;

import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.handler.ConfigurationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBiomes;
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

public class GenerateSprings implements AOTDGeneratable
{
	@Override
	public boolean generate(World world, Random random, int chunkX, int chunkZ)
	{
		if (!AOTDWorldData.get(world).isValidLocation(new Point3D(chunkX + 14, AOTDDungeonTypes.Spring.getRadius(), chunkZ + 14), true))
		{
			return false;
		}

		try
		{
			int y = WorldGenerationUtility.getPlaceToSpawnLowest(world, chunkX, chunkZ, 28, 28);

			AOTDWorldData.get(world).addDungeon(new Point3D(chunkX + 14, AOTDDungeonTypes.Spring.getRadius(), chunkZ + 14), true);

			if (ConfigurationHandler.debugMessages)
			{
				LogHelper.info("Spawning a spring at x = " + chunkX + ", y = " + y + ", z = " + chunkZ);
			}

			SchematicGenerator.generateSchematic(AOTDSchematics.Spring.getSchematic(), world, chunkX, y - 2, chunkZ);

			return true;
		}
		catch (UnsupportedLocationException e)
		{
			return false;
		}
	}

	@Override
	public double getGenerationChance(int biomeID)
	{
		// Plains
		if (biomeID == 2)
			return ConfigurationHandler.springFrequency * 0.5 * ConfigurationHandler.dungeonFrequencyMultiplier;
		else if (biomeID == Biome.getIdForBiome(ModBiomes.erieForest))
			return ConfigurationHandler.springFrequency * ConfigurationHandler.dungeonFrequencyMultiplier;
		// Savanna
		else if (biomeID == 35)
			return ConfigurationHandler.springFrequency * ConfigurationHandler.dungeonFrequencyMultiplier;
		return 0;
	}
}
