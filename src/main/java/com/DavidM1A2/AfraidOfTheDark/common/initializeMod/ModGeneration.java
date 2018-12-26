/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.initializeMod;

import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.AOTDWorldGenerationHandler;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModGeneration
{
	public static final AOTDWorldGenerationHandler aotdWorldGenerator = new AOTDWorldGenerationHandler();

	public static void initialize()
	{
		// Register generators
		GameRegistry.registerWorldGenerator(ModGeneration.aotdWorldGenerator, 1);
	}
}
