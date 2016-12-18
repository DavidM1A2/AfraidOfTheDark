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

public class VoidChestLoot implements IChestGenerator
{
	@Override
	public List<RandomItem> getPossibleItems(Random random)
	{
		ArrayList<RandomItem> toReturn = new ArrayList<RandomItem>();

		//                                          Item, meta, min, max, chance
		toReturn.add(new RandomItem(Items.ENDER_PEARL, 2));
		toReturn.add(new RandomItem(Items.ENDER_EYE, 2));
		toReturn.add(new RandomItem(Item.getItemFromBlock(Blocks.END_STONE), 7));
		toReturn.add(new RandomItem(Item.getItemFromBlock(Blocks.OBSIDIAN), 7));

		return toReturn;
	}
}