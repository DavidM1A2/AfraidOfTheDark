/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration;

import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.biomes.BiomeErieForest;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenPlains;
import net.minecraft.world.biome.BiomeGenSavanna;
import net.minecraft.world.biome.BiomeGenSnow;
import net.minecraft.world.biome.BiomeGenSwamp;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

public class AOTDWorldGenerationHandler implements IWorldGenerator
{
	private static final double RELEASE_DUNGEON_RARITY = 0.8;

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		switch (world.provider.getDimensionId())
		{
			case 0:
			{
				generateSurface(world, random, chunkX * 16, chunkZ * 16);
			}
		}
	}

	private void generateSurface(World world, Random random, int chunkX, int chunkZ)
	{
		double dungeonRarityMultiplyer = 1.0;

		if (!Constants.isDebug)
		{
			dungeonRarityMultiplyer = RELEASE_DUNGEON_RARITY;
		}

		BiomeGenBase current = world.getBiomeGenForCoords(new BlockPos(chunkX, 50, chunkZ));

		if (current instanceof BiomeGenPlains)
		{
			if (random.nextDouble() < 0.002 * dungeonRarityMultiplyer)
			{
				new GenerateSprings(random, chunkX, chunkZ, world);
			}
			if (random.nextDouble() < 0.0012 * dungeonRarityMultiplyer)
			{
				new GenerateDarkForestDungeon(random, chunkX, chunkZ, world);
			}
		}
		else if (current instanceof BiomeErieForest)
		{
			if (random.nextDouble() < 0.004 * dungeonRarityMultiplyer)
			{
				new GenerateSprings(random, chunkX, chunkZ, world);
			}
			if (random.nextDouble() < 0.003 * dungeonRarityMultiplyer)
			{
				new GenerateDarkForestDungeon(random, chunkX, chunkZ, world);
			}
			if (random.nextDouble() < 0.01 * dungeonRarityMultiplyer)
			{
				new GenerateCrypt(random, chunkX, chunkZ, world);
			}
			if (random.nextDouble() < 0.006 * dungeonRarityMultiplyer)
			{
				new GenerateWitchHut(random, chunkX, chunkZ, world);
			}
		}
		else if (current instanceof BiomeGenSavanna)
		{
			if (random.nextDouble() < 0.004 * dungeonRarityMultiplyer)
			{
				new GenerateSprings(random, chunkX, chunkZ, world);
			}
			if (random.nextDouble() < 0.003 * dungeonRarityMultiplyer)
			{
				new GenerateDarkForestDungeon(random, chunkX, chunkZ, world);
			}
		}
		else if (current instanceof BiomeGenSwamp)
		{
			if (random.nextDouble() < 0.01 * dungeonRarityMultiplyer)
			{
				new GenerateWitchHut(random, chunkX, chunkZ, world);
			}
		}
		else if (current instanceof BiomeGenSnow)
		{
			if (random.nextDouble() < 0.003 * dungeonRarityMultiplyer)
			{
				new GenerateVoidChest(random, chunkX, chunkZ, world);
			}
		}
	}
}
