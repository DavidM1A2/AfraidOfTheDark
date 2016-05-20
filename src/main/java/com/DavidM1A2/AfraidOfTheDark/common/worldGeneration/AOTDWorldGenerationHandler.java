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

import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDWorldData;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.generatables.AOTDGeneratable;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.generatables.GenerateCrypt;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.generatables.GenerateDarkForestDungeon;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.generatables.GenerateGnomishCity;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.generatables.GenerateSprings;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.generatables.GenerateVoidChest;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.generatables.GenerateWitchHut;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

public class AOTDWorldGenerationHandler implements IWorldGenerator
{
	private final List<AOTDGeneratable> OVERWORLD = new LinkedList<AOTDGeneratable>()
	{
		{
			add(new GenerateCrypt());
			add(new GenerateDarkForestDungeon());
			add(new GenerateGnomishCity());
			add(new GenerateSprings());
			add(new GenerateVoidChest());
			add(new GenerateWitchHut());
		}
	};

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		switch (world.provider.getDimensionId())
		{
			case 0:
			{
				AOTDWorldData.register(world);
				this.generateSurface(world, random, chunkX * 16, chunkZ * 16);
			}
		}
	}

	private void generateSurface(World world, Random random, int chunkX, int chunkZ)
	{
		int currentBiomeID = world.getBiomeGenForCoords(new BlockPos(chunkX, 50, chunkZ)).biomeID;

		Collections.shuffle(OVERWORLD, random);
		double randomNum = random.nextDouble();
		for (AOTDGeneratable generatable : this.OVERWORLD)
			if (randomNum * 100 < generatable.getGenerationChance(currentBiomeID))
				if (generatable.generate(world, random, chunkX, chunkZ))
					return;
	}
}
