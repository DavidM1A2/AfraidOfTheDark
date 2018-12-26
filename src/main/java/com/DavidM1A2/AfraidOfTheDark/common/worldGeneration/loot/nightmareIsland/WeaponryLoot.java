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

public class WeaponryLoot implements IChestGenerator
{
	@Override
	public List<RandomItem> getPossibleItems(Random random)
	{
		ArrayList<RandomItem> toReturn = new ArrayList<RandomItem>();

		//                                          Item, meta, min, max, chance
		toReturn.add(new RandomItem(Items.IRON_SWORD, 10));
		toReturn.add(new RandomItem(Items.GOLDEN_SWORD, 10));
		toReturn.add(new RandomItem(Items.DIAMOND_SWORD, 10));
		toReturn.add(new RandomItem(Items.STONE_SWORD, 10));

		toReturn.add(new RandomItem(Items.DIAMOND_HELMET, 10));
		toReturn.add(new RandomItem(Items.DIAMOND_CHESTPLATE, 10));
		toReturn.add(new RandomItem(Items.DIAMOND_LEGGINGS, 10));
		toReturn.add(new RandomItem(Items.DIAMOND_BOOTS, 10));

		toReturn.add(new RandomItem(Items.GOLDEN_HELMET, 10));
		toReturn.add(new RandomItem(Items.GOLDEN_CHESTPLATE, 10));
		toReturn.add(new RandomItem(Items.GOLDEN_LEGGINGS, 10));
		toReturn.add(new RandomItem(Items.GOLDEN_BOOTS, 10));

		toReturn.add(new RandomItem(Items.IRON_HELMET, 10));
		toReturn.add(new RandomItem(Items.IRON_CHESTPLATE, 10));
		toReturn.add(new RandomItem(Items.IRON_LEGGINGS, 10));
		toReturn.add(new RandomItem(Items.IRON_BOOTS, 10));

		toReturn.add(new RandomItem(Items.LEATHER_HELMET, 10));
		toReturn.add(new RandomItem(Items.LEATHER_CHESTPLATE, 10));
		toReturn.add(new RandomItem(Items.LEATHER_BOOTS, 10));
		toReturn.add(new RandomItem(Items.LEATHER_LEGGINGS, 10));

		return toReturn;
	}
}
