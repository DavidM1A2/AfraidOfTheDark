/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.initializeMod;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ModOreDictionaryCompatability
{
	public static void initialize()
	{
		// We want our silver to be compatable with other silver ores
		OreDictionary.registerOre("plankWood", new ItemStack(ModBlocks.gravewoodPlanks));
		OreDictionary.registerOre("treeWood", new ItemStack(ModBlocks.gravewood));
	}
}
