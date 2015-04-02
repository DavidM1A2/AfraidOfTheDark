package com.DavidM1A2.AfraidOfTheDark.worldGeneration;

import java.util.ArrayList;
import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModItems;

import net.minecraft.item.Item;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

public class GraveyardChestLoot
{

	public static WeightedRandomChestContent[] getItems(Random rnd)
	{
		ArrayList<WeightedRandomChestContent> ret = new ArrayList<WeightedRandomChestContent>();

		WeightedRandomChestContent n = new WeightedRandomChestContent(ModItems.journal, 1, 1, 1, 1000);
		if (n != null)
		{
			ret.add(n);
		}
		
		return ret.toArray(new WeightedRandomChestContent[ret.size()]);
	}
}
