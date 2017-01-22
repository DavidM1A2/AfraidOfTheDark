/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import com.DavidM1A2.AfraidOfTheDark.common.block.gravewood.BlockGravewoodDoubleSlab;
import com.DavidM1A2.AfraidOfTheDark.common.block.gravewood.BlockGravewoodHalfSlab;
import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItemSlab;

import net.minecraft.block.Block;

// Item version of the slab
public class ItemGravewoodSlab extends AOTDItemSlab
{
	public ItemGravewoodSlab(final Block block, final BlockGravewoodHalfSlab blockSlabHalf, final BlockGravewoodDoubleSlab blockSlabDouble, final Boolean stacked)
	{
		super(block, blockSlabHalf, blockSlabDouble);
		if (!stacked)
			this.setRegistryName("gravewood_half_slab");
		else
			this.setRegistryName("gravewood_upper_slab");
	}
}
