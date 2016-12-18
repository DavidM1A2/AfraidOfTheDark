/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.IChestGenerator;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.RandomItem;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class ValueableLoot implements IChestGenerator
{
	@Override
	public List<RandomItem> getPossibleItems(Random random)
	{
		ArrayList<RandomItem> toReturn = new ArrayList<RandomItem>();

		//                                          Item, meta, min, max, chance
		toReturn.add(new RandomItem(Items.DIAMOND, 10));
		toReturn.add(new RandomItem(Items.GOLD_INGOT, 10));
		toReturn.add(new RandomItem(ModItems.astralSilverIngot, 10));
		toReturn.add(new RandomItem(Items.GOLD_NUGGET, 10));
		toReturn.add(new RandomItem(Item.getItemFromBlock(Blocks.DIAMOND_BLOCK), 10));
		toReturn.add(new RandomItem(Item.getItemFromBlock(Blocks.GOLD_BLOCK), 10));
		toReturn.add(new RandomItem(Item.getItemFromBlock(Blocks.REDSTONE_ORE), 10));
		toReturn.add(new RandomItem(Item.getItemFromBlock(Blocks.IRON_BLOCK), 10));
		toReturn.add(new RandomItem(Items.IRON_INGOT, 10));

		return toReturn;
	}
}
