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

import net.minecraft.init.Items;
import net.minecraft.util.WeightedRandomChestContent;

public class GnomishCityRareLoot implements IChestGenerator
{
	@Override
	public List<WeightedRandomChestContent> getPossibleItems(Random random)
	{
		ArrayList<WeightedRandomChestContent> toReturn = new ArrayList<WeightedRandomChestContent>();

		//                                          Item, meta, min, max, chance
		toReturn.add(Items.enchanted_book.getRandom(random, 1, 1, 15));
		toReturn.add(new WeightedRandomChestContent(Items.diamond, 0, 1, 10, 5));
		toReturn.add(new WeightedRandomChestContent(ModItems.igneousGem, 0, 1, 10, 5));
		toReturn.add(new WeightedRandomChestContent(ModItems.starMetalIngot, 0, 1, 10, 5));
		toReturn.add(new WeightedRandomChestContent(ModItems.sunstoneFragment, 0, 1, 10, 8));
		toReturn.add(new WeightedRandomChestContent(Items.gold_ingot, 0, 5, 20, 8));

		return toReturn;
	}
}
