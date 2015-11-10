
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.gnomishCity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.IChestGenerator;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.WeightedRandomChestContent;

public class GnomishCityLoot implements IChestGenerator
{
	@Override
	public List<WeightedRandomChestContent> getPossibleItems(Random random)
	{
		ArrayList<WeightedRandomChestContent> toReturn = new ArrayList<WeightedRandomChestContent>();

		//                                          Item, meta, min, max, chance
		toReturn.add(new WeightedRandomChestContent(ModItems.woodenBolt, 0, 1, 15, 15));
		toReturn.add(new WeightedRandomChestContent(ModItems.ironBolt, 0, 1, 15, 13));
		toReturn.add(new WeightedRandomChestContent(ModItems.silverBolt, 0, 1, 13, 10));
		toReturn.add(new WeightedRandomChestContent(ModItems.igneousBolt, 0, 1, 6, 4));
		toReturn.add(new WeightedRandomChestContent(ModItems.starMetalBolt, 0, 1, 6, 4));
		toReturn.add(new WeightedRandomChestContent(Item.getItemFromBlock(ModBlocks.gnomishMetalPlate), 0, 3, 5, 25));
		toReturn.add(new WeightedRandomChestContent(Item.getItemFromBlock(ModBlocks.gnomishMetalStrut), 0, 3, 5, 25));
		toReturn.add(Items.enchanted_book.func_92112_a(random, 1, 1, 3));
		toReturn.add(new WeightedRandomChestContent(Items.diamond, 0, 1, 5, 1));
		toReturn.add(new WeightedRandomChestContent(Items.gold_nugget, 0, 5, 40, 18));

		return toReturn;
	}
}
