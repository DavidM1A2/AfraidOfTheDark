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
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.RandomItem;

import net.minecraft.init.Items;

public class FoodLoot implements IChestGenerator
{
	@Override
	public List<RandomItem> getPossibleItems(Random random)
	{
		ArrayList<RandomItem> toReturn = new ArrayList<RandomItem>();

		//                                          Item, meta, min, max, chance
		toReturn.add(new RandomItem(Items.COOKED_BEEF, 10));
		toReturn.add(new RandomItem(Items.APPLE, 10));
		toReturn.add(new RandomItem(Items.BAKED_POTATO, 10));
		toReturn.add(new RandomItem(Items.BREAD, 10));
		toReturn.add(new RandomItem(Items.CARROT, 10));
		toReturn.add(new RandomItem(Items.COOKED_CHICKEN, 10));
		toReturn.add(new RandomItem(Items.COOKED_FISH, 10));
		toReturn.add(new RandomItem(Items.COOKED_MUTTON, 10));
		toReturn.add(new RandomItem(Items.COOKED_PORKCHOP, 10));
		toReturn.add(new RandomItem(Items.COOKED_RABBIT, 10));
		toReturn.add(new RandomItem(Items.COOKIE, 10));
		toReturn.add(new RandomItem(Items.GOLDEN_APPLE, 10));
		toReturn.add(new RandomItem(Items.MELON, 10));
		toReturn.add(new RandomItem(Items.POTATO, 10));
		toReturn.add(new RandomItem(Items.RABBIT_STEW, 10));
		toReturn.add(new RandomItem(Items.ROTTEN_FLESH, 10));

		return toReturn;
	}
}
