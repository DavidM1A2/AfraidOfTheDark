package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration;

import java.util.Random;

import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.UnsupportedLocationException;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

public class GenerateSprings
{
	public GenerateSprings(Random random, int chunkX, int chunkZ, World world)
	{
		this.generateSurface(world, random, chunkX, chunkZ);
	}

	private void generateSurface(World world, Random random, int chunkX, int chunkZ)
	{
		int y;
		try
		{
			y = Utility.getPlaceToSpawn(world, chunkX, chunkZ, 28, 28);
		}
		catch (UnsupportedLocationException e)
		{
			y = 0;
		}
		if (y != 0)
		{
			LogHelper.info("Chose the location: x = " + chunkX + ", y = " + y + ", z = " + chunkZ);
			new Spring(world, random, chunkX, y - 2, chunkZ);
		}
	}
}
