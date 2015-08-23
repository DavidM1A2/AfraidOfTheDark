/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration;

import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.biomes.BiomeErieForest;
import com.DavidM1A2.AfraidOfTheDark.common.handler.ConfigurationHandler;

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
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		switch (world.provider.getDimensionId())
		{
			case 0:
			{
				this.generateSurface(world, random, chunkX * 16, chunkZ * 16);
			}
		}
	}

	private void generateSurface(World world, Random random, int chunkX, int chunkZ)
	{
		BiomeGenBase current = world.getBiomeGenForCoords(new BlockPos(chunkX, 50, chunkZ));

		if (chunkX == 0 && chunkZ == 0)
		{
			new GenerateGnomishCity(random, chunkX, chunkZ, world);
		}
		else if (current instanceof BiomeGenPlains)
		{
			if (random.nextDouble() < 0.0016 * ConfigurationHandler.dungeonFrequencyMultiplier)
			{
				new GenerateSprings(random, chunkX, chunkZ, world);
			}
			if (random.nextDouble() < 0.00096 * ConfigurationHandler.dungeonFrequencyMultiplier)
			{
				new GenerateDarkForestDungeon(random, chunkX, chunkZ, world);
			}
		}
		else if (current instanceof BiomeErieForest)
		{
			if (random.nextDouble() < 0.0032 * ConfigurationHandler.dungeonFrequencyMultiplier)
			{
				new GenerateSprings(random, chunkX, chunkZ, world);
			}
			if (random.nextDouble() < 0.002 * ConfigurationHandler.dungeonFrequencyMultiplier)
			{
				new GenerateDarkForestDungeon(random, chunkX, chunkZ, world);
			}
			if (random.nextDouble() < 0.01 * ConfigurationHandler.dungeonFrequencyMultiplier)
			{
				new GenerateCrypt(random, chunkX, chunkZ, world);
			}
			if (random.nextDouble() < 0.0048 * ConfigurationHandler.dungeonFrequencyMultiplier)
			{
				new GenerateWitchHut(random, chunkX, chunkZ, world);
			}
		}
		else if (current instanceof BiomeGenSavanna)
		{
			if (random.nextDouble() < 0.0032 * ConfigurationHandler.dungeonFrequencyMultiplier)
			{
				new GenerateSprings(random, chunkX, chunkZ, world);
			}
			if (random.nextDouble() < 0.0024 * ConfigurationHandler.dungeonFrequencyMultiplier)
			{
				new GenerateDarkForestDungeon(random, chunkX, chunkZ, world);
			}
		}
		else if (current instanceof BiomeGenSwamp)
		{
			if (random.nextDouble() < 0.008 * ConfigurationHandler.dungeonFrequencyMultiplier)
			{
				new GenerateWitchHut(random, chunkX, chunkZ, world);
			}
		}
		else if (current instanceof BiomeGenSnow)
		{
			if (random.nextDouble() < 0.0024 * ConfigurationHandler.dungeonFrequencyMultiplier)
			{
				new GenerateVoidChest(random, chunkX, chunkZ, world);
			}
		}
	}
}
