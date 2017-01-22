/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.initializeMod;

import net.minecraftforge.oredict.OreDictionary;

public class ModOreDictionaryCompatability
{
	public static void initialize()
	{
		OreDictionary.registerOre("plankWood", ModBlocks.gravewoodPlanks);
		OreDictionary.registerOre("treeWood", ModBlocks.gravewood);
		OreDictionary.registerOre("plankWood", ModBlocks.mangrovePlanks);
		OreDictionary.registerOre("treeWood", ModBlocks.mangrove);
	}
}
