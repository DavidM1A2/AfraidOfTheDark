/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration;

import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.handler.ConfigurationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.AOTDLootTables;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.AOTDSchematics;
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
		if (!AOTDWorldData.get(world).isValidLocation(new Point3D(chunkX, AOTDDungeonTypes.Crypt.getRadius(), chunkZ), false))
		{
			return false;
		}

		int y;
		try
		{
			y = WorldGenerationUtility.getPlaceToSpawnLowest(world, chunkX + 12, chunkZ + 12, 7, 7);

			AOTDWorldData.get(world).addDungeon(new Point3D(chunkX, AOTDDungeonTypes.Crypt.getRadius(), chunkZ), false);

			if (ConfigurationHandler.debugMessages)
			{
				LogHelper.info("Spawning a crypt at x = " + (chunkX + 12) + ", y = " + (y - 17) + ", z = " + (chunkZ + 12));
			}

			SchematicGenerator.generateSchematicWithLoot(AOTDSchematics.Crypt.getSchematic(), world, chunkX + 12, y - 17, chunkZ + 12, AOTDLootTables.Crypt.getLootTable());

			return true;
		}
		catch (UnsupportedLocationException e)
		{
			return false;
		}
	}
}
