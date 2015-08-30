/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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

			int stairs2To3 = 4;
			int stairs1To2;
			int stairs3To4;

			// Stairs inbetween two main levels
			while (stairs2To3 == 4)
			{
				stairs2To3 = random.nextInt(9);
			}

			// Stairs into the top level
			do
			{
				stairs1To2 = random.nextInt(9);
			}
			while (stairs1To2 == stairs2To3);

			// Stairs to Enaria
			do
			{
				stairs3To4 = random.nextInt(9);
			}
			while (stairs3To4 == stairs2To3);

			List<Integer> rooms = new LinkedList<Integer>();

			// Make sure every room is added
			for (int i = 0; i < 12; i++)
			{
				if (i < Constants.AOTDSchematics.rooms.size())
				{
					rooms.add(i, i);
				}
				else
				{
					rooms.add(i, random.nextInt(Constants.AOTDSchematics.rooms.size()));
				}
			}

			Collections.shuffle(rooms, random);

			// Rooms
			for (int k = 0; k < 2; k++)
			{
				for (int i = 0; i < 3; i++)
				{
					for (int j = 0; j < 3; j++)
					{
						if (i + j * 3 == stairs2To3)
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
						else if (i + j * 3 == stairs1To2 && k == 0)
						{
							SchematicGenerator.generateSchematic(Constants.AOTDSchematics.roomStairDown, world, chunkX + i * 50, y + k * 15, chunkZ + j * 50);
						}
						else if (i + j * 3 == stairs3To4 && k == 1)
						{
							SchematicGenerator.generateSchematic(Constants.AOTDSchematics.roomStairUp, world, chunkX + i * 50, y + k * 15, chunkZ + j * 50);
							SchematicGenerator.generateSchematic(Constants.AOTDSchematics.stairwell, world, chunkX + i * 50 + 13, y + k * 15 + 15, chunkZ + j * 50 + 13);
						}
						else
						{
							SchematicGenerator.generateSchematic(Constants.AOTDSchematics.rooms.get(rooms.get(i + j * 3)), world, chunkX + i * 50, y + k * 15, chunkZ + j * 50);
						}
					}
				}
			}
			//Create tunnels
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
