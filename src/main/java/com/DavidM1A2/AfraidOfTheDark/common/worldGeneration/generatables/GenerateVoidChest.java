/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.generatables;

import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.handler.ConfigurationHandler;
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

public class GenerateVoidChest implements AOTDGeneratable
{
	@Override
	public boolean generate(World world, Random random, int chunkX, int chunkZ)
	{
		if (!AOTDWorldData.get(world).isValidLocation(new Point3D(chunkX + 7, AOTDDungeonTypes.VoidChest.getRadius(), chunkZ + 7), true))
		{
			return false;
		}

		try
		{
			int y = WorldGenerationUtility.getPlaceToSpawnLowest(world, chunkX, chunkZ, 14, 14);

			AOTDWorldData.get(world).addDungeon(new Point3D(chunkX + 7, AOTDDungeonTypes.VoidChest.getRadius(), chunkZ + 7), true);

			if (ConfigurationHandler.debugMessages)
			{
				LogHelper.info("Spawning a void chest at x = " + chunkX + ", y = " + y + ", z = " + chunkZ);
			}

			SchematicGenerator.generateSchematicWithLoot(AOTDSchematics.VoidChest.getSchematic(), world, chunkX, y - 6, chunkZ, AOTDLootTables.VoidChest.getLootTable());

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
		// Ice plains and mountains
		if (biomeID == 12 || biomeID == 13)
			return ConfigurationHandler.voidChestFrequency * ConfigurationHandler.dungeonFrequencyMultiplier;
		return 0;
	}
}
