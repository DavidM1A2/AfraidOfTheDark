package com.DavidM1A2.AfraidOfTheDark.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;

import com.DavidM1A2.AfraidOfTheDark.block.BlockGravewoodDoubleSlab;
import com.DavidM1A2.AfraidOfTheDark.block.BlockGravewoodHalfSlab;

public class ItemGravewoodSlab extends ItemSlab
{
	public ItemGravewoodSlab(final Block block, final BlockGravewoodHalfSlab blockSlabHalf, final BlockGravewoodDoubleSlab blockSlabDouble, final Boolean stacked)
	{
		super(block, blockSlabHalf, blockSlabDouble);
	}

}
