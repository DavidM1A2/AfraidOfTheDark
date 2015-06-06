/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.initializeMod;

import net.minecraftforge.fml.common.registry.GameRegistry;

import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.GenerateSprings;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.VeronaTreeV40withleaves;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.darkForestDungeon.GenerateDarkForestDungeon;

public class ModGeneration
{
	// Declare ore generators
	public static final VeronaTreeV40withleaves generateTree = new VeronaTreeV40withleaves();
	public static final GenerateSprings generateSprings = new GenerateSprings();
	public static final GenerateDarkForestDungeon generateDarkForestDungeon = new GenerateDarkForestDungeon();

	public static void initialize()
	{
		// Register generators
		GameRegistry.registerWorldGenerator(ModGeneration.generateSprings, 1);
		GameRegistry.registerWorldGenerator(ModGeneration.generateDarkForestDungeon, 1);
		// GameRegistry.registerWorldGenerator(generateTree, 1);
	}
}
