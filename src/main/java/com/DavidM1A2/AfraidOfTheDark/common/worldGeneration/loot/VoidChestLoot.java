/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.WeightedRandomChestContent;

public class VoidChestLoot implements IChestGenerator
{
	@Override
	public List<WeightedRandomChestContent> getPossibleItems(Random random)
	{
		ArrayList<WeightedRandomChestContent> toReturn = new ArrayList<WeightedRandomChestContent>();

		//                                          Item, meta, min, max, chance
		toReturn.add(new WeightedRandomChestContent(Items.ender_pearl, 0, 1, 3, 7));
		toReturn.add(new WeightedRandomChestContent(Items.ender_eye, 0, 1, 3, 7));
		toReturn.add(new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.end_stone), 0, 1, 30, 7));
		toReturn.add(new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.obsidian), 0, 1, 30, 7));

		return toReturn;
	}
}