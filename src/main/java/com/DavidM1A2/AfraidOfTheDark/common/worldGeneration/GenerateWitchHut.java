/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration;

import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.SchematicGenerator;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.UnsupportedLocationException;
import com.DavidM1A2.AfraidOfTheDark.common.utility.WorldGenerationUtility;

import net.minecraft.world.World;

public class GenerateWitchHut
{
	public GenerateWitchHut(Random random, int chunkX, int chunkZ, World world)
	{
		this.generateSurface(world, random, chunkX, chunkZ);
	}

	private void generateSurface(World world, Random random, int chunkX, int chunkZ)
	{
		try
		{
			int y = WorldGenerationUtility.getPlaceToSpawnLowest(world, chunkX, chunkZ, 11, 11);

			if (Constants.isDebug)
			{
				LogHelper.info("Spawning a hut at x = " + chunkX + ", y = " + y + ", z = " + chunkZ);
			}

			SchematicGenerator.generateSchematicWithLoot(Constants.AOTDSchematics.witchHut, world, chunkX, y, chunkZ, Constants.witchHutLootTable);
		}
		catch (UnsupportedLocationException e)
		{
		}
	}
}
