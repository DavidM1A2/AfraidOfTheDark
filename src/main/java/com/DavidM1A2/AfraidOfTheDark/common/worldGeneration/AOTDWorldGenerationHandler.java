/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration;

import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.biomes.BiomeErieForest;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenPlains;
import net.minecraft.world.biome.BiomeGenSavanna;
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
				generateSurface(world, random, chunkX * 16, chunkZ * 16);
			}
		}
	}

	private void generateSurface(World world, Random random, int chunkX, int chunkZ)
	{
		if (world.getBiomeGenForCoords(new BlockPos(chunkX, 50, chunkZ)) instanceof BiomeGenPlains)
		{
			if (random.nextDouble() < 0.002)
			{
				new GenerateSprings(random, chunkX, chunkZ, world);
			}
			if (random.nextDouble() < 0.0016)
			{
				new GenerateDarkForestDungeon(random, chunkX, chunkZ, world);
			}
		}
		else if (world.getBiomeGenForCoords(new BlockPos(chunkX, 50, chunkZ)) instanceof BiomeErieForest)
		{
			if (random.nextDouble() < 0.002)
			{
				new GenerateSprings(random, chunkX, chunkZ, world);
			}
			if (random.nextDouble() < 0.0016)
			{
				new GenerateDarkForestDungeon(random, chunkX, chunkZ, world);
			}
			if (random.nextDouble() < 0.003)
			{
				new GenerateCrypt(random, chunkX, chunkZ, world);
			}
			if (random.nextDouble() < 0.005)
			{
				new GenerateWitchHut(random, chunkX, chunkZ, world);
			}
		}
		else if (world.getBiomeGenForCoords(new BlockPos(chunkX, 50, chunkZ)) instanceof BiomeGenSavanna)
		{
			if (random.nextDouble() < 0.002)
			{
				new GenerateSprings(random, chunkX, chunkZ, world);
			}
			if (random.nextDouble() < 0.002)
			{
				new GenerateDarkForestDungeon(random, chunkX, chunkZ, world);
			}
		}
		else if (world.getBiomeGenForCoords(new BlockPos(chunkX, 50, chunkZ)) instanceof BiomeGenSwamp)
		{
			if (random.nextDouble() < 0.01)
			{
				new GenerateWitchHut(random, chunkX, chunkZ, world);
			}
		}
	}
}
