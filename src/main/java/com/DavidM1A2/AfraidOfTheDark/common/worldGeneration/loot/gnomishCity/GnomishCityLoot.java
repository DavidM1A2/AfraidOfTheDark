
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.gnomishCity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.IChestGenerator;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.RandomItem;

import net.minecraft.init.Items;

public class GnomishCityLoot implements IChestGenerator
{
	@Override
	public List<RandomItem> getPossibleItems(Random random)
	{
		ArrayList<RandomItem> toReturn = new ArrayList<RandomItem>();

		//                                          Item, meta, min, max, chance
		toReturn.add(new RandomItem(ModItems.woodenBolt, 11));
		toReturn.add(new RandomItem(ModItems.ironBolt, 10));
		toReturn.add(new RandomItem(ModItems.silverBolt, 9));
		toReturn.add(new RandomItem(ModItems.igneousBolt, 7));
		toReturn.add(new RandomItem(ModItems.starMetalBolt, 7));
		toReturn.add(new RandomItem(ModItems.gnomishMetalIngot, 12));
		toReturn.add(new RandomItem(Items.IRON_INGOT, 10));
		toReturn.add(new RandomItem(Items.EXPERIENCE_BOTTLE, 10));
		toReturn.add(new RandomItem(Items.MELON_SEEDS, 11));
		toReturn.add(new RandomItem(Items.PUMPKIN_SEEDS, 11));
		toReturn.add(new RandomItem(Items.LEATHER, 8));
		return toReturn;
	}
}
