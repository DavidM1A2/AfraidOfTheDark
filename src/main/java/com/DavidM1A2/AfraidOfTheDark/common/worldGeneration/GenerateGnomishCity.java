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

import net.minecraft.world.World;

public class GenerateGnomishCity
{
	public GenerateGnomishCity(Random random, int chunkX, int chunkZ, World world)
	{
		this.generateSurface(world, random, chunkX, chunkZ);
	}

	private void generateSurface(World world, Random random, int chunkX, int chunkZ)
	{
		try
		{
			int y = 150;//Utility.getFirstNonAirBlock(world, chunkX, chunkZ);

			if (Constants.isDebug)
			{
				LogHelper.info("Spawning a gnomish city at x = " + chunkX + ", y = " + y + ", z = " + chunkZ);
			}

			int stairs = random.nextInt(9);

			for (int k = 0; k < 2; k++)
			{
				for (int i = 0; i < 3; i++)
				{
					for (int j = 0; j < 3; j++)
					{
						if (i + j * 3 == stairs)
						{
							if (k == 0)
							{
								SchematicGenerator.generateSchematic(Constants.AOTDSchematics.roomStairUp, world, chunkX + i * 50, y + k * 15, chunkZ + j * 50);
							}
							else
							{
								SchematicGenerator.generateSchematic(Constants.AOTDSchematics.roomStairDown, world, chunkX + i * 50, y + k * 15, chunkZ + j * 50);
							}
						}
						else
						{
							SchematicGenerator.generateSchematic(Constants.AOTDSchematics.rooms.get(random.nextInt(Constants.AOTDSchematics.rooms.size())), world, chunkX + i * 50, y + k * 15, chunkZ + j * 50);
						}
					}
				}
			}
			for (int k = 0; k < 2; k++)
			{
				for (int i = 0; i < 3; i++)
				{
					for (int j = 0; j < 2; j++)
					{
						SchematicGenerator.generateSchematic(Constants.AOTDSchematics.tunnelEW, world, chunkX + i * 50 + 13, y + k * 15 + 7, chunkZ + j * 50 + 32);
						SchematicGenerator.generateSchematic(Constants.AOTDSchematics.tunnelNS, world, chunkX + j * 50 + 32, y + k * 15 + 7, chunkZ + i * 50 + 13);
					}
				}
			}
		}
		catch (UnsupportedLocationException e)
		{
		}
	}
}
