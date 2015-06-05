/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.initializeMod;

import net.minecraftforge.fml.common.registry.GameRegistry;

import com.DavidM1A2.AfraidOfTheDark.worldGeneration.GenerateSprings;
import com.DavidM1A2.AfraidOfTheDark.worldGeneration.VeronaTreeV40withleaves;

public class ModGeneration
{
	// Declare ore generators
	public static final VeronaTreeV40withleaves generateTree = new VeronaTreeV40withleaves();
	public static final GenerateSprings generateSprings = new GenerateSprings();

	public static void initialize()
	{
		// Register generators
		GameRegistry.registerWorldGenerator(ModGeneration.generateSprings, 1);
		// GameRegistry.registerWorldGenerator(generateTree, 1);
	}
}
