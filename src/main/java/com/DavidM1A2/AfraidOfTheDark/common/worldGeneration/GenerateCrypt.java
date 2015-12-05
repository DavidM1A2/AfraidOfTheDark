/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration;

import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.handler.ConfigurationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDWorldData;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.SchematicGenerator;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Point3D;
import com.DavidM1A2.AfraidOfTheDark.common.utility.UnsupportedLocationException;
import com.DavidM1A2.AfraidOfTheDark.common.utility.WorldGenerationUtility;

import net.minecraft.world.World;

public class GenerateCrypt
{
	public static boolean generateSurface(World world, Random random, int chunkX, int chunkZ)
	{
		if (!AOTDWorldData.get(world).isValidLocation(new Point3D(chunkX, 23, chunkZ), false))
		{
			return false;
		}

		int y;
		try
		{
			y = WorldGenerationUtility.getPlaceToSpawnLowest(world, chunkX + 12, chunkZ + 12, 7, 7);

			AOTDWorldData.get(world).addDungeon(new Point3D(chunkX, 23, chunkZ), false);
			AOTDWorldData.get(world).setDirty(true);

			if (ConfigurationHandler.debugMessages)
			{
				LogHelper.info("Spawning a crypt at x = " + (chunkX + 12) + ", y = " + (y - 17) + ", z = " + (chunkZ + 12));
			}

			SchematicGenerator.generateSchematicWithLoot(Constants.AOTDSchematics.crypt, world, chunkX + 12, y - 17, chunkZ + 12, Constants.cryptLootTable);

			return true;
		}
		catch (UnsupportedLocationException e)
		{
			return false;
		}
	}
}
