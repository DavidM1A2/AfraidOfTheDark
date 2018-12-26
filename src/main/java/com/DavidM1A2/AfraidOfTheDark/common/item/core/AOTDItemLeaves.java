/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item.core;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDLeaves;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AOTDItemLeaves extends ItemBlock
{
	private final AOTDLeaves leaves;

	public AOTDItemLeaves(final Block leaves)
	{
		super(leaves);
		this.leaves = (AOTDLeaves) leaves;
	}

	/**
	 * Converts the given ItemStack damage value into a metadata value to be placed in the world when this Item is placed as a Block (mostly used with
	 * ItemBlocks).
	 */
	@Override
	public int getMetadata(final int damage)
	{
		return damage | 4;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(final ItemStack stack, final int renderPass)
	{
		return this.leaves.getRenderColor(this.leaves.getStateFromMeta(stack.getMetadata()));
	}
}