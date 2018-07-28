/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.generatables;

import java.util.Random;

import net.minecraft.world.World;

public interface AOTDGeneratable
{
	/**
	 * 
	 * @param world
	 *            The world to generate the structure in
	 * @param random
	 *            The random with which to generate the structure
	 * @param chunkX
	 *            X coord
	 * @param chunkZ
	 *            Y coord
	 * @return If the generation was sucessful or not
	 */
	public boolean generate(World world, Random random, int chunkX, int chunkZ);

	/**
	 * 
	 * @return A value between 0 and 1 representing the chance for the the structure to generate in the biome
	 */
	public double getGenerationChance(int biomeID);
}
