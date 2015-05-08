package com.DavidM1A2.AfraidOfTheDark.worldGeneration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.util.WeightedRandomChestContent;

import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModItems;

public class GraveyardChestLoot
{

	public static List getItems(Random rnd)
	{
		ArrayList<WeightedRandomChestContent> ret = new ArrayList<WeightedRandomChestContent>();

		WeightedRandomChestContent n = new WeightedRandomChestContent(ModItems.journal, 1, 1, 1, 1000);
		if (n != null)
		{
			ret.add(n);
		}

		return ret;
	}
}
