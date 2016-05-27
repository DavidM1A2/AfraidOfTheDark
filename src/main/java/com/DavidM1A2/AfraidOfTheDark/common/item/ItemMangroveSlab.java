/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import com.DavidM1A2.AfraidOfTheDark.common.block.mangrove.BlockMangroveDoubleSlab;
import com.DavidM1A2.AfraidOfTheDark.common.block.mangrove.BlockMangroveHalfSlab;
import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItemSlab;

import net.minecraft.block.Block;

public class ItemMangroveSlab extends AOTDItemSlab
{
	public ItemMangroveSlab(final Block block, final BlockMangroveHalfSlab blockSlabHalf, final BlockMangroveDoubleSlab blockSlabDouble, final Boolean stacked)
	{
		super(block, blockSlabHalf, blockSlabDouble);
	}
}