/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.nightmareIsland;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.IChestGenerator;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.WeightedRandomChestContent;

public class RandomBlockLoot implements IChestGenerator
{
	@Override
	public List<WeightedRandomChestContent> getPossibleItems(Random random)
	{
		ArrayList<WeightedRandomChestContent> toReturn = new ArrayList<WeightedRandomChestContent>();

		//                                          Item, meta, min, max, chance
		toReturn.add(new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.stone), 0, 1, 15, 10));
		toReturn.add(new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.gravel), 0, 1, 15, 10));
		toReturn.add(new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.stonebrick), 0, 1, 15, 10));
		toReturn.add(new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.dirt), 0, 1, 15, 10));
		toReturn.add(new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.grass), 0, 1, 15, 10));
		toReturn.add(new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.cobblestone), 0, 1, 15, 10));
		toReturn.add(new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.planks), 0, 1, 15, 10));
		toReturn.add(new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.log), 0, 1, 15, 10));
		toReturn.add(new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.leaves), 0, 1, 15, 10));
		toReturn.add(new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.sand), 0, 1, 15, 10));
		toReturn.add(new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.sandstone), 0, 1, 15, 10));
		toReturn.add(new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.glass), 0, 1, 15, 10));
		toReturn.add(new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.glass_pane), 0, 1, 15, 10));
		toReturn.add(new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.stone_slab), 0, 1, 15, 10));
		toReturn.add(new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.snow), 0, 1, 15, 10));
		toReturn.add(new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.quartz_block), 0, 1, 15, 10));
		toReturn.add(new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.hardened_clay), 0, 1, 15, 10));
		toReturn.add(new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.coal_block), 0, 1, 15, 10));
		toReturn.add(new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.red_sandstone), 0, 1, 15, 10));

		return toReturn;
	}
}
