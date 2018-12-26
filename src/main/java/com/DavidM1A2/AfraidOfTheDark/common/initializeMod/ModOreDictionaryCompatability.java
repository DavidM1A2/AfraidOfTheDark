/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.initializeMod;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ModOreDictionaryCompatability
{
	public static void initialize()
	{
		OreDictionary.registerOre("plankWood", new ItemStack(ModBlocks.gravewoodPlanks));
		OreDictionary.registerOre("treeWood", new ItemStack(ModBlocks.gravewood));
		OreDictionary.registerOre("plankWood", new ItemStack(ModBlocks.mangrovePlanks));
		OreDictionary.registerOre("treeWood", new ItemStack(ModBlocks.mangrove));
	}
}
