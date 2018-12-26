
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.gnomishCity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.IChestGenerator;

import net.minecraft.init.Items;
import net.minecraft.util.WeightedRandomChestContent;

public class GnomishCityLoot implements IChestGenerator
{
	@Override
	public List<WeightedRandomChestContent> getPossibleItems(Random random)
	{
		ArrayList<WeightedRandomChestContent> toReturn = new ArrayList<WeightedRandomChestContent>();

		//                                          Item, meta, min, max, chance
		toReturn.add(new WeightedRandomChestContent(ModItems.woodenBolt, 0, 15, 30, 11));
		toReturn.add(new WeightedRandomChestContent(ModItems.ironBolt, 0, 15, 30, 10));
		toReturn.add(new WeightedRandomChestContent(ModItems.silverBolt, 0, 15, 30, 9));
		toReturn.add(new WeightedRandomChestContent(ModItems.igneousBolt, 0, 15, 30, 7));
		toReturn.add(new WeightedRandomChestContent(ModItems.starMetalBolt, 0, 15, 30, 7));
		toReturn.add(new WeightedRandomChestContent(ModItems.gnomishMetalIngot, 0, 12, 24, 12));
		toReturn.add(new WeightedRandomChestContent(Items.iron_ingot, 0, 5, 10, 10));
		toReturn.add(new WeightedRandomChestContent(Items.experience_bottle, 0, 5, 10, 10));
		toReturn.add(new WeightedRandomChestContent(Items.melon_seeds, 0, 5, 10, 11));
		toReturn.add(new WeightedRandomChestContent(Items.pumpkin_seeds, 0, 5, 10, 11));
		toReturn.add(new WeightedRandomChestContent(Items.leather, 0, 5, 20, 8));

		return toReturn;
	}
}
