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
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.RandomItem;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

public class RandomBlockLoot implements IChestGenerator
{
	@Override
	public List<RandomItem> getPossibleItems(Random random)
	{
		ArrayList<RandomItem> toReturn = new ArrayList<RandomItem>();

		//                                          Item, meta, min, max, chance
		toReturn.add(new RandomItem(Item.getItemFromBlock(Blocks.STONE), 10));
		toReturn.add(new RandomItem(Item.getItemFromBlock(Blocks.GRAVEL), 10));
		toReturn.add(new RandomItem(Item.getItemFromBlock(Blocks.STONEBRICK), 10));
		toReturn.add(new RandomItem(Item.getItemFromBlock(Blocks.DIRT), 10));
		toReturn.add(new RandomItem(Item.getItemFromBlock(Blocks.GRASS), 10));
		toReturn.add(new RandomItem(Item.getItemFromBlock(Blocks.COBBLESTONE), 10));
		toReturn.add(new RandomItem(Item.getItemFromBlock(Blocks.PLANKS), 10));
		toReturn.add(new RandomItem(Item.getItemFromBlock(Blocks.LOG), 10));
		toReturn.add(new RandomItem(Item.getItemFromBlock(Blocks.LEAVES), 10));
		toReturn.add(new RandomItem(Item.getItemFromBlock(Blocks.SAND), 10));
		toReturn.add(new RandomItem(Item.getItemFromBlock(Blocks.SANDSTONE), 10));
		toReturn.add(new RandomItem(Item.getItemFromBlock(Blocks.GLASS), 10));
		toReturn.add(new RandomItem(Item.getItemFromBlock(Blocks.GLASS_PANE), 10));
		toReturn.add(new RandomItem(Item.getItemFromBlock(Blocks.STONE_SLAB), 10));
		toReturn.add(new RandomItem(Item.getItemFromBlock(Blocks.SNOW), 10));
		toReturn.add(new RandomItem(Item.getItemFromBlock(Blocks.QUARTZ_BLOCK), 10));
		toReturn.add(new RandomItem(Item.getItemFromBlock(Blocks.HARDENED_CLAY), 10));
		toReturn.add(new RandomItem(Item.getItemFromBlock(Blocks.COAL_BLOCK), 10));
		toReturn.add(new RandomItem(Item.getItemFromBlock(Blocks.RED_SANDSTONE), 10));

		return toReturn;
	}
}
