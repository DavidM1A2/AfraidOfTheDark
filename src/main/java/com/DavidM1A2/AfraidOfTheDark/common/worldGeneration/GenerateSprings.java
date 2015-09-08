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

public class GenerateSprings
{
	public GenerateSprings(Random random, int chunkX, int chunkZ, World world)
	{
		this.generateSurface(world, random, chunkX, chunkZ);
	}

	private void generateSurface(World world, Random random, int chunkX, int chunkZ)
	{
		try
		{
			int y = WorldGenerationUtility.getPlaceToSpawnLowest(world, chunkX, chunkZ, 28, 28);

			if (Constants.isDebug)
			{
				LogHelper.info("Spawning a spring at x = " + chunkX + ", y = " + y + ", z = " + chunkZ);
			}

			SchematicGenerator.generateSchematic(Constants.AOTDSchematics.spring, world, chunkX, y - 2, chunkZ);
		}
		catch (UnsupportedLocationException e)
		{
		}
	}
}
