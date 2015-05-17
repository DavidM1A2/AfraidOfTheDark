/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;

import com.DavidM1A2.AfraidOfTheDark.block.BlockGravewoodDoubleSlab;
import com.DavidM1A2.AfraidOfTheDark.block.BlockGravewoodHalfSlab;

// Item version of the slab
public class ItemGravewoodSlab extends ItemSlab
{
	public ItemGravewoodSlab(final Block block, final BlockGravewoodHalfSlab blockSlabHalf, final BlockGravewoodDoubleSlab blockSlabDouble, final Boolean stacked)
	{
		super(block, blockSlabHalf, blockSlabDouble);
	}
}
