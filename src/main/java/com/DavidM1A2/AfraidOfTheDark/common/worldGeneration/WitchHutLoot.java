package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Items;
import net.minecraft.util.WeightedRandomChestContent;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;

public class WitchHutLoot implements IChestGenerator
{
	@Override
	public List<WeightedRandomChestContent> getPossibleItems(Random random)
	{
		ArrayList<WeightedRandomChestContent> toReturn = new ArrayList<WeightedRandomChestContent>();

		for (int i = 5; i < 25; i++)
		{
			//                                          Item, meta, min, max, chance
			toReturn.add(new WeightedRandomChestContent(Items.potionitem, i, 1, 3, 7));
		}

		toReturn.add(new WeightedRandomChestContent(ModItems.researchScrollWristCrossbow, 0, 1, 1, 4));
		toReturn.add(new WeightedRandomChestContent(ModItems.researchScrollCloakOfAgility, 0, 1, 1, 4));

		return toReturn;
	}
}