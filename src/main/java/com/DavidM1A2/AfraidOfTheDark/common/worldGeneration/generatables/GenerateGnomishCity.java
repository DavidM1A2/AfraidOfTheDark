/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.generatables;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.handler.ConfigurationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBiomes;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDLootTables;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDSchematics;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDWorldData;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.SchematicGenerator;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Point3D;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.AOTDDungeonTypes;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLeaves;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class GenerateGnomishCity implements AOTDGeneratable
{
	@Override
	public boolean generate(World world, Random random, int chunkX, int chunkZ)
	{
		int y = 20;

		if (!AOTDWorldData.get(world).isValidLocation(new Point3D(chunkX, AOTDDungeonTypes.GnomishCity.getRadius(), chunkZ), false))
		{
			return false;
		}
		AOTDWorldData.get(world).addDungeon(new Point3D(chunkX, AOTDDungeonTypes.GnomishCity.getRadius(), chunkZ), false);

		if (ConfigurationHandler.debugMessages)
		{
			LogHelper.info("Spawning a gnomish city at x = " + chunkX + ", y = " + y + ", z = " + chunkZ);
		}

		chunkX = chunkX - 65;
		chunkZ = chunkZ - 65;

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

		// Make sure every room is added at least once
		for (int i = 0; i < AOTDSchematics.getGnomishCityRooms().size(); i++)
		{
			rooms.add(i);
			rooms.add(i);
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
							SchematicGenerator.generateSchematicWithLoot(AOTDSchematics.RoomStairUp.getSchematic(), world, chunkX + i * 50, y + k * 15, chunkZ + j * 50, AOTDLootTables.NightmareIsland.getLootTable());
						}
						else
						{
							SchematicGenerator.generateSchematicWithLoot(AOTDSchematics.RoomStairDown.getSchematic(), world, chunkX + i * 50, y + k * 15, chunkZ + j * 50, AOTDLootTables.NightmareIsland.getLootTable());
						}
					}
					else if (currentRoom == stairs1To2 && k == 0)
					{
						SchematicGenerator.generateSchematicWithLoot(AOTDSchematics.RoomStairDown.getSchematic(), world, chunkX + i * 50, y + k * 15, chunkZ + j * 50, AOTDLootTables.NightmareIsland.getLootTable());

						// Create enaria's lair
						SchematicGenerator.generateSchematic(AOTDSchematics.EnariaLair.getSchematic(), world, chunkX + i * 50 - 14, y + k * 15 - 15, chunkZ + j * 50 - 73);
					}
					else if (currentRoom == stairs3To4 && k == 1)
					{
						SchematicGenerator.generateSchematicWithLoot(AOTDSchematics.RoomStairUp.getSchematic(), world, chunkX + i * 50, y + k * 15, chunkZ + j * 50, AOTDLootTables.NightmareIsland.getLootTable());

						SchematicGenerator.generateSchematic(AOTDSchematics.Stairwell.getSchematic(), world, chunkX + i * 50 + 13, y + k * 15 + 15, chunkZ + j * 50 + 13);
						SchematicGenerator.generateSchematic(AOTDSchematics.Stairwell.getSchematic(), world, chunkX + i * 50 + 13, y + k * 15 + 31, chunkZ + j * 50 + 13);

						this.fixStairs(world, chunkX + i * 50 + 13, y + k * 15 + 47, chunkZ + j * 50 + 13);
					}
					else
					{
						SchematicGenerator.generateSchematicWithLoot(AOTDSchematics.getGnomishCityRooms().get(rooms.get(0)), world, chunkX + i * 50, y + k * 15, chunkZ + j * 50, AOTDLootTables.NightmareIsland.getLootTable());
						rooms.remove(0);
					}
				}
			}
		}

		// Create tunnels
		for (int k = 0; k < 2; k++)
		{
			for (int i = 0; i < 3; i++)
			{
				for (int j = 0; j < 2; j++)
				{
					SchematicGenerator.generateSchematic(AOTDSchematics.TunnelEW.getSchematic(), world, chunkX + i * 50 + 13, y + k * 15 + 7, chunkZ + j * 50 + 32);
					SchematicGenerator.generateSchematic(AOTDSchematics.TunnelNS.getSchematic(), world, chunkX + j * 50 + 32, y + k * 15 + 7, chunkZ + i * 50 + 13);
				}
			}
		}

		return true;
	}

	@Override
	public double getGenerationChance(int biomeID)
	{
		if (biomeID == BiomeGenBase.plains.biomeID)
			return ConfigurationHandler.gnomishCityFrequency * 0.375 * ConfigurationHandler.dungeonFrequencyMultiplier;
		else if (biomeID == ModBiomes.erieForest.biomeID)
			return ConfigurationHandler.gnomishCityFrequency * ConfigurationHandler.dungeonFrequencyMultiplier;
		else if (biomeID == BiomeGenBase.savanna.biomeID)
			return ConfigurationHandler.gnomishCityFrequency * 0.5 * ConfigurationHandler.dungeonFrequencyMultiplier;
		else if (biomeID == BiomeGenBase.icePlains.biomeID || biomeID == BiomeGenBase.iceMountains.biomeID)
			return ConfigurationHandler.gnomishCityFrequency * 0.5 * ConfigurationHandler.dungeonFrequencyMultiplier;
		return 0;
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
