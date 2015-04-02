package com.DavidM1A2.AfraidOfTheDark.initializeMod;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.DavidM1A2.AfraidOfTheDark.block.BlockSilverOre;
import com.DavidM1A2.AfraidOfTheDark.item.ItemSilverIngot;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModFurnaceRecipies 
{
	public static void initialize()
	{
		GameRegistry.addSmelting(ModBlocks.silverOre, new ItemStack(ModItems.silverIngot), .4F);
	}
}
