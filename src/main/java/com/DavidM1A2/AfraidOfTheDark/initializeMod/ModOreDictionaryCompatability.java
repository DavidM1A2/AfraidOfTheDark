package com.DavidM1A2.AfraidOfTheDark.initializeMod;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ModOreDictionaryCompatability 
{
	public static void initialize()
	{
		OreDictionary.registerOre("oreSilver", new ItemStack(ModBlocks.silverOre));
		OreDictionary.registerOre("ingotSilver", new ItemStack(ModItems.silverIngot));
	}
}
