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

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLeaves;
import net.minecraft.util.BlockPos;
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
			int y = 20;
			chunkX = chunkX - 65;
			chunkX = chunkZ - 65;

			if (Constants.isDebug)
			{
				LogHelper.info("Spawning a gnomish city at x = " + (chunkX + 65) + ", y = " + y + ", z = " + (chunkZ + 65));
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
			while (stairs3To4 == stairs2To3 || stairs3To4 == stairs1To2);

			List<Integer> rooms = new LinkedList<Integer>();

			// Make sure every room is added
			for (int i = 0; i < 14; i++)
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
			// k = floor 0 (bottom) or 1 (upper)
			for (int k = 0; k < 2; k++)
			{
				// i and j is the room in the x and y dimension
				for (int i = 0; i < 3; i++)
				{
					for (int j = 0; j < 3; j++)
					{
						int currentRoom = i + j * 3;
						if (currentRoom == stairs2To3)
						{
							if (k == 0)
							{
								SchematicGenerator.generateSchematicWithLoot(Constants.AOTDSchematics.roomStairUp, world, chunkX + i * 50, y + k * 15, chunkZ + j * 50, Constants.gnomishCityLootTable);
							}
							else
							{
								SchematicGenerator.generateSchematicWithLoot(Constants.AOTDSchematics.roomStairDown, world, chunkX + i * 50, y + k * 15, chunkZ + j * 50, Constants.gnomishCityLootTable);
							}
						}
						else if (currentRoom == stairs1To2 && k == 0)
						{
							SchematicGenerator.generateSchematicWithLoot(Constants.AOTDSchematics.roomStairDown, world, chunkX + i * 50, y + k * 15, chunkZ + j * 50, Constants.gnomishCityLootTable);

							// Create enaria's lair
							SchematicGenerator.generateSchematic(Constants.AOTDSchematics.enariaLair, world, chunkX + i * 50 - 14, y + k * 15 - 15, chunkZ + j * 50 - 73);
						}
						else if (currentRoom == stairs3To4 && k == 1)
						{
							SchematicGenerator.generateSchematicWithLoot(Constants.AOTDSchematics.roomStairUp, world, chunkX + i * 50, y + k * 15, chunkZ + j * 50, Constants.gnomishCityLootTable);

							SchematicGenerator.generateSchematic(Constants.AOTDSchematics.stairwell, world, chunkX + i * 50 + 13, y + k * 15 + 15, chunkZ + j * 50 + 13);
							SchematicGenerator.generateSchematic(Constants.AOTDSchematics.stairwell, world, chunkX + i * 50 + 13, y + k * 15 + 31, chunkZ + j * 50 + 13);

							this.fixStairs(world, chunkX + i * 50 + 13, y + k * 15 + 47, chunkZ + j * 50 + 13);
						}
						else
						{
							SchematicGenerator.generateSchematicWithLoot(Constants.AOTDSchematics.rooms.get(rooms.get(currentRoom)), world, chunkX + i * 50, y + k * 15, chunkZ + j * 50, Constants.gnomishCityLootTable);
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

		// +22 for the enaria dungeon
	}

	private void fixStairs(World world, int x, int y, int z)
	{
		while (y > 5)
		{
			Block block1 = world.getBlockState(new BlockPos(x + 3, y, z - 1)).getBlock();
			Block block2 = world.getBlockState(new BlockPos(x - 1, y, z + 3)).getBlock();
			Block block3 = world.getBlockState(new BlockPos(x + 7, y, z + 3)).getBlock();
			Block block4 = world.getBlockState(new BlockPos(x + 3, y, z + 7)).getBlock();

			boolean levelValidToRemove = block1 instanceof BlockAir || block1 instanceof BlockLeaves;
			levelValidToRemove = levelValidToRemove && (block2 instanceof BlockAir || block2 instanceof BlockLeaves);
			levelValidToRemove = levelValidToRemove && (block3 instanceof BlockAir || block3 instanceof BlockLeaves);
			levelValidToRemove = levelValidToRemove && (block4 instanceof BlockAir || block4 instanceof BlockLeaves);

			if (levelValidToRemove)
			{
				this.clearLevel(world, x, y, z);
			}
			else
			{
				return;
			}

			y = y - 1;
		}
	}

	private void clearLevel(World world, int x, int y, int z)
	{
		for (int i = 0; i < 7; i++)
		{
			for (int j = 0; j < 7; j++)
			{
				world.setBlockToAir(new BlockPos(x + i, y, z + j));
			}
		}
	}
}
