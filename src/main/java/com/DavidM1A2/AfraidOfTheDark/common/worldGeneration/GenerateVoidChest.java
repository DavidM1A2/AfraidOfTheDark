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
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

import net.minecraft.world.World;

public class GenerateVoidChest
{
	public GenerateVoidChest(Random random, int chunkX, int chunkZ, World world)
	{
		this.generateSurface(world, random, chunkX, chunkZ);
	}

	private void generateSurface(World world, Random random, int chunkX, int chunkZ)
	{
		try
		{
			int y = Utility.getPlaceToSpawnLowest(world, chunkX, chunkZ, 14, 14);

			if (Constants.isDebug)
			{
				LogHelper.info("Spawning a void chest at x = " + chunkX + ", y = " + y + ", z = " + chunkZ);
			}

			SchematicGenerator.generateSchematicWithLoot(Constants.AOTDSchematics.voidChest, world, chunkX, y - 6, chunkZ, Constants.voidChestTable);
		}
		catch (UnsupportedLocationException e)
		{
		}
	}
}
