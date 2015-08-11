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

public class WitchHutLoot implements IChestGenerator
{
	@Override
	public List<WeightedRandomChestContent> getPossibleItems(Random random)
	{
		ArrayList<WeightedRandomChestContent> toReturn = new ArrayList<WeightedRandomChestContent>();

		//                                          Item, meta, min, max, chance
		toReturn.add(new WeightedRandomChestContent(Items.potionitem, random.nextInt(20) + 5, 1, 1, 8));

		toReturn.add(new WeightedRandomChestContent(ModItems.researchScrollWristCrossbow, 0, 1, 1, 1));

		return toReturn;
	}
}