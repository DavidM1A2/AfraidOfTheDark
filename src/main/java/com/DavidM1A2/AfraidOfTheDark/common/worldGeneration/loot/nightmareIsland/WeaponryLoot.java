/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.IChestGenerator;

import net.minecraft.init.Items;
import net.minecraft.util.WeightedRandomChestContent;

public class WeaponryLoot implements IChestGenerator
{
	@Override
	public List<WeightedRandomChestContent> getPossibleItems(Random random)
	{
		ArrayList<WeightedRandomChestContent> toReturn = new ArrayList<WeightedRandomChestContent>();

		//                                          Item, meta, min, max, chance
		toReturn.add(new WeightedRandomChestContent(Items.iron_sword, 0, 1, 1, 10));
		toReturn.add(new WeightedRandomChestContent(Items.golden_sword, 0, 1, 1, 10));
		toReturn.add(new WeightedRandomChestContent(Items.diamond_sword, 0, 1, 1, 10));
		toReturn.add(new WeightedRandomChestContent(Items.stone_sword, 0, 1, 1, 10));

		toReturn.add(new WeightedRandomChestContent(Items.diamond_helmet, 0, 1, 1, 10));
		toReturn.add(new WeightedRandomChestContent(Items.diamond_chestplate, 0, 1, 1, 10));
		toReturn.add(new WeightedRandomChestContent(Items.diamond_leggings, 0, 1, 1, 10));
		toReturn.add(new WeightedRandomChestContent(Items.diamond_boots, 0, 1, 1, 10));

		toReturn.add(new WeightedRandomChestContent(Items.golden_helmet, 0, 1, 1, 10));
		toReturn.add(new WeightedRandomChestContent(Items.golden_chestplate, 0, 1, 1, 10));
		toReturn.add(new WeightedRandomChestContent(Items.golden_leggings, 0, 1, 1, 10));
		toReturn.add(new WeightedRandomChestContent(Items.golden_boots, 0, 1, 1, 10));

		toReturn.add(new WeightedRandomChestContent(Items.iron_helmet, 0, 1, 1, 10));
		toReturn.add(new WeightedRandomChestContent(Items.iron_chestplate, 0, 1, 1, 10));
		toReturn.add(new WeightedRandomChestContent(Items.iron_leggings, 0, 1, 1, 10));
		toReturn.add(new WeightedRandomChestContent(Items.iron_boots, 0, 1, 1, 10));

		toReturn.add(new WeightedRandomChestContent(Items.leather_helmet, 0, 1, 1, 10));
		toReturn.add(new WeightedRandomChestContent(Items.leather_chestplate, 0, 1, 1, 10));
		toReturn.add(new WeightedRandomChestContent(Items.leather_boots, 0, 1, 1, 10));
		toReturn.add(new WeightedRandomChestContent(Items.leather_leggings, 0, 1, 1, 10));

		return toReturn;
	}
}
