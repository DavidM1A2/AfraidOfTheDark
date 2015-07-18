/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.utility;

import net.minecraft.item.ItemStack;

public class ConvertedRecipe
{
	private final int width;
	private final int height;
	private final ItemStack output;
	private final ItemStack[] input;

	public ConvertedRecipe(int width, int height, ItemStack output, ItemStack[] input)
	{
		this.width = width;
		this.height = height;
		this.output = output;
		this.input = input;
	}

	public int getWidth()
	{
		return this.width;
	}

	public int getHeight()
	{
		return this.height;
	}

	public ItemStack getOutput()
	{
		return this.output;
	}

	public ItemStack[] getInput()
	{
		return this.input;
	}
}
