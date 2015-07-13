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

public class GenerateCrypt
{
	public GenerateCrypt(Random random, int chunkX, int chunkZ, World world)
	{
		this.generateSurface(world, random, chunkX, chunkZ);
	}

	private void generateSurface(World world, Random random, int chunkX, int chunkZ)
	{
		int y;
		try
		{
			y = Utility.getPlaceToSpawnLowest(world, chunkX + 12, chunkZ + 12, 8, 8);

			LogHelper.info("Spawning a crypt at x = " + (chunkX + 12) + ", y = " + (y - 17) + ", z = " + (chunkZ + 12));

			SchematicGenerator.generateSchematicWithLoot(Constants.AOTDSchematics.crypt, world, chunkX + 12, y - 17, chunkZ + 12, Constants.cryptLootTable);
		}
		catch (UnsupportedLocationException e)
		{
			return;
		}
	}
}
