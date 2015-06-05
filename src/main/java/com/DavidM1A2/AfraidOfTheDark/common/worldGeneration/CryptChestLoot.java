package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Items;
import net.minecraft.util.WeightedRandomChestContent;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;

public class CryptChestLoot
{
	public static List getItems(Random rnd)
	{
		ArrayList<WeightedRandomChestContent> toReturn = new ArrayList<WeightedRandomChestContent>();

		WeightedRandomChestContent journal = new WeightedRandomChestContent(ModItems.journal, 0, 1, 1, 1);
		WeightedRandomChestContent bones = new WeightedRandomChestContent(Items.bone, 0, 5, 15, 10);

		toReturn.add(journal);
		toReturn.add(bones);

		return toReturn;
	}
}
