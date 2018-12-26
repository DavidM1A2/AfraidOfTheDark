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

public class RandomItemLoot implements IChestGenerator
{
	@Override
	public List<RandomItem> getPossibleItems(Random random)
	{
		ArrayList<RandomItem> toReturn = new ArrayList<RandomItem>();

		//                                          Item, meta, min, max, chance
		toReturn.add(new RandomItem(Items.COMPASS, 10));
		toReturn.add(new RandomItem(Items.PAINTING, 10));
		toReturn.add(new RandomItem(Items.SIGN, 10));
		toReturn.add(new RandomItem(Items.SADDLE, 10));
		toReturn.add(new RandomItem(Items.MINECART, 10));
		toReturn.add(new RandomItem(Items.SNOWBALL, 10));
		toReturn.add(new RandomItem(Items.PAPER, 10));
		toReturn.add(new RandomItem(Items.BOOK, 10));
		toReturn.add(new RandomItem(Items.BANNER, 10));
		toReturn.add(new RandomItem(Items.FISHING_ROD, 10));
		toReturn.add(new RandomItem(Items.DYE, 10));
		toReturn.add(new RandomItem(Items.MAP, 10));
		toReturn.add(new RandomItem(Items.REDSTONE, 10));
		toReturn.add(new RandomItem(Items.SPIDER_EYE, 10));
		toReturn.add(new RandomItem(Items.WRITABLE_BOOK, 10));
		toReturn.add(new RandomItem(Items.WRITTEN_BOOK, 10));
		toReturn.add(new RandomItem(Items.SKULL, 10));
		toReturn.add(new RandomItem(Items.LEAD, 10));
		toReturn.add(new RandomItem(Items.DIAMOND_HORSE_ARMOR, 10));

		return toReturn;
	}
}
