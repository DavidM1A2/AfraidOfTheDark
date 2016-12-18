/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;

public class RandomItem extends WeightedRandom.Item
{
	private final ItemStack item;

	public RandomItem(Item item, int itemWeightIn)
	{
		super(itemWeightIn);
		this.item = new ItemStack(item);
	}

	public RandomItem(ItemStack itemStack, int itemWeightIn)
	{
		super(itemWeightIn);
		this.item = itemStack;
	}

	public ItemStack getItem()
	{
		return item;
	}
}
