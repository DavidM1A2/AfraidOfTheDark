/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;

import net.minecraft.init.Items;
import net.minecraft.util.WeightedRandomChestContent;

public class CryptChestLoot implements IChestGenerator
{
	@Override
	public List<WeightedRandomChestContent> getPossibleItems(Random random)
	{
		ArrayList<WeightedRandomChestContent> toReturn = new ArrayList<WeightedRandomChestContent>();

		WeightedRandomChestContent journal = new WeightedRandomChestContent(ModItems.journal, 0, 1, 1, 1);
		WeightedRandomChestContent bones = new WeightedRandomChestContent(Items.bone, 0, 5, 15, 10);

		toReturn.add(journal);
		toReturn.add(bones);

		return toReturn;
	}
}
