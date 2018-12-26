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

public class RandomItemLoot implements IChestGenerator
{
	@Override
	public List<WeightedRandomChestContent> getPossibleItems(Random random)
	{
		ArrayList<WeightedRandomChestContent> toReturn = new ArrayList<WeightedRandomChestContent>();

		//                                          Item, meta, min, max, chance
		toReturn.add(new WeightedRandomChestContent(Items.compass, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.painting, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.sign, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.saddle, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.minecart, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.snowball, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.paper, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.book, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.banner, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.fishing_rod, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.dye, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.map, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.redstone, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.spider_eye, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.writable_book, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.writable_book, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.skull, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.lead, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 5, 10));

		return toReturn;
	}
}
