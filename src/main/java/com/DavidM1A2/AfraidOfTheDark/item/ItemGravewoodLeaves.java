/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.DavidM1A2.AfraidOfTheDark.block.AOTDLeaves;
import com.DavidM1A2.AfraidOfTheDark.block.BlockGravewoodLeaves;

// Leaves must have an item attached to allow for colored leaves to be held
public class ItemGravewoodLeaves extends ItemBlock
{
	private AOTDLeaves leaves;

	public ItemGravewoodLeaves(Block leaves)
	{
		super(leaves);
		this.leaves = (BlockGravewoodLeaves) leaves;
	}

	/**
	 * Converts the given ItemStack damage value into a metadata value to be placed in the world when this Item is placed as a Block (mostly used with
	 * ItemBlocks).
	 */
	public int getMetadata(int damage)
	{
		return damage | 4;
	}

	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int renderPass)
	{
		return this.leaves.getRenderColor(this.leaves.getStateFromMeta(stack.getMetadata()));
	}
}
