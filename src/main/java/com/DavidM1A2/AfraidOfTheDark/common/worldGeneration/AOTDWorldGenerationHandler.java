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

		if (current instanceof BiomeGenPlains)
		{
			if (random.nextDouble() * 100 < ConfigurationHandler.gnomishCityFrequency * 0.375 * ConfigurationHandler.dungeonFrequencyMultiplier)
			{
				if (GenerateGnomishCity.generateSurface(world, random, chunkX, chunkZ))
					return;
			}
			if (random.nextDouble() * 100 < ConfigurationHandler.darkForestFrequency * 0.3 * ConfigurationHandler.dungeonFrequencyMultiplier)
			{
				if (GenerateDarkForestDungeon.generateSurface(world, random, chunkX, chunkZ))
					return;
			}
			if (random.nextDouble() * 100 < ConfigurationHandler.springFrequency * 0.5 * ConfigurationHandler.dungeonFrequencyMultiplier)
			{
				if (GenerateSprings.generateSurface(world, random, chunkX, chunkZ))
					return;
			}
		}
		else if (current instanceof BiomeErieForest)
		{
			if (random.nextDouble() * 100 < ConfigurationHandler.gnomishCityFrequency * ConfigurationHandler.dungeonFrequencyMultiplier)
			{
				if (GenerateGnomishCity.generateSurface(world, random, chunkX, chunkZ))
					return;
			}
			if (random.nextDouble() * 100 < ConfigurationHandler.darkForestFrequency * ConfigurationHandler.dungeonFrequencyMultiplier)
			{
				if (GenerateDarkForestDungeon.generateSurface(world, random, chunkX, chunkZ))
					return;
			}
			if (random.nextDouble() * 100 < ConfigurationHandler.cryptFrequency * ConfigurationHandler.dungeonFrequencyMultiplier)
			{
				if (GenerateCrypt.generateSurface(world, random, chunkX, chunkZ))
					return;
			}
			if (random.nextDouble() * 100 < ConfigurationHandler.springFrequency * ConfigurationHandler.dungeonFrequencyMultiplier)
			{
				if (GenerateSprings.generateSurface(world, random, chunkX, chunkZ))
					return;
			}
			if (random.nextDouble() * 100 < ConfigurationHandler.witchHutFrequency * 0.5 * ConfigurationHandler.dungeonFrequencyMultiplier)
			{
				if (GenerateWitchHut.generateSurface(world, random, chunkX, chunkZ))
					return;
			}
		}
		else if (current instanceof BiomeGenSavanna)
		{
			if (random.nextDouble() * 100 < ConfigurationHandler.gnomishCityFrequency * 0.5 * ConfigurationHandler.dungeonFrequencyMultiplier)
			{
				if (GenerateGnomishCity.generateSurface(world, random, chunkX, chunkZ))
					return;
			}
			if (random.nextDouble() * 100 < ConfigurationHandler.darkForestFrequency * 0.6 * ConfigurationHandler.dungeonFrequencyMultiplier)
			{
				if (GenerateDarkForestDungeon.generateSurface(world, random, chunkX, chunkZ))
					return;
			}
			if (random.nextDouble() * 100 < ConfigurationHandler.springFrequency * ConfigurationHandler.dungeonFrequencyMultiplier)
			{
				if (GenerateSprings.generateSurface(world, random, chunkX, chunkZ))
					return;
			}
		}
		else if (current instanceof BiomeGenSwamp)
		{
			if (random.nextDouble() * 100 < ConfigurationHandler.witchHutFrequency * ConfigurationHandler.dungeonFrequencyMultiplier)
			{
				if (GenerateWitchHut.generateSurface(world, random, chunkX, chunkZ))
					return;
			}
		}
		else if (current instanceof BiomeGenSnow)
		{
			if (random.nextDouble() * 100 < ConfigurationHandler.gnomishCityFrequency * 0.5 * ConfigurationHandler.dungeonFrequencyMultiplier)
			{
				if (GenerateGnomishCity.generateSurface(world, random, chunkX, chunkZ))
					return;
			}
			if (random.nextDouble() * 100 < ConfigurationHandler.voidChestFrequency * ConfigurationHandler.dungeonFrequencyMultiplier)
			{
				if (GenerateVoidChest.generateSurface(world, random, chunkX, chunkZ))
					return;
			}
		}
	}
}
