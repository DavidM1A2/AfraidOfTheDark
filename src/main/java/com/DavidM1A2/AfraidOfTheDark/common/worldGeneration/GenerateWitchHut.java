package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration;

import java.util.Random;

import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.SchematicGenerator;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.UnsupportedLocationException;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

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
			int y = Utility.getPlaceToSpawnLowest(world, chunkX, chunkZ, 11, 11);

			LogHelper.info("Spawning a hut at x = " + chunkX + ", y = " + y + ", z = " + chunkZ);

			SchematicGenerator.generateSchematic(Constants.AOTDSchematics.witchHut, world, chunkX, y, chunkZ, new WitchHutLoot(), 3);
		}
		catch (UnsupportedLocationException e)
		{
		}
	}
}
