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

public class FoodLoot implements IChestGenerator
{
	@Override
	public List<WeightedRandomChestContent> getPossibleItems(Random random)
	{
		ArrayList<WeightedRandomChestContent> toReturn = new ArrayList<WeightedRandomChestContent>();

		//                                          Item, meta, min, max, chance
		toReturn.add(new WeightedRandomChestContent(Items.cooked_beef, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.apple, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.baked_potato, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.bread, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.carrot, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.cooked_chicken, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.cooked_fish, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.cooked_mutton, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.cooked_porkchop, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.cooked_rabbit, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.cookie, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.golden_apple, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.melon, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.potato, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.rabbit_stew, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.rotten_flesh, 0, 1, 5, 10));

		return toReturn;
	}
}
