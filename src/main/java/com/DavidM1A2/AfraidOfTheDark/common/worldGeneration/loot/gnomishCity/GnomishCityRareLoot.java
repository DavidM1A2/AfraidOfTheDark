/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.gnomishCity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.IChestGenerator;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.RandomItem;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GnomishCityRareLoot implements IChestGenerator
{
	@Override
	public List<RandomItem> getPossibleItems(Random random)
	{
		ArrayList<RandomItem> toReturn = new ArrayList<RandomItem>();

		//                                          Item, meta, min, max, chance
		toReturn.add(new RandomItem(EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.ENCHANTED_BOOK), 25, true), 15));
		toReturn.add(new RandomItem(Items.DIAMOND, 5));
		toReturn.add(new RandomItem(ModItems.igneousGem, 5));
		toReturn.add(new RandomItem(ModItems.starMetalIngot, 5));
		toReturn.add(new RandomItem(ModItems.sunstoneFragment, 8));
		toReturn.add(new RandomItem(Items.GOLD_INGOT, 8));

		return toReturn;
	}
}
