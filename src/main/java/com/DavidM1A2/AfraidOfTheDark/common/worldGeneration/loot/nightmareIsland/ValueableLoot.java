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

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.WeightedRandomChestContent;

public class ValueableLoot implements IChestGenerator
{
	@Override
	public List<WeightedRandomChestContent> getPossibleItems(Random random)
	{
		ArrayList<WeightedRandomChestContent> toReturn = new ArrayList<WeightedRandomChestContent>();

		//                                          Item, meta, min, max, chance
		toReturn.add(new WeightedRandomChestContent(Items.diamond, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(ModItems.astralSilverIngot, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.gold_nugget, 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.diamond_block), 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.gold_block), 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.redstone_ore), 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.iron_block), 0, 1, 5, 10));
		toReturn.add(new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10));

		return toReturn;
	}
}
