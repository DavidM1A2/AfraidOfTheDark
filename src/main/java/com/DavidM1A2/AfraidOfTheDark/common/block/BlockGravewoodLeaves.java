/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;

public class BlockGravewoodLeaves extends AOTDLeaves
{
	/*
	 * Define gravewood leaves block
	 */
	public BlockGravewoodLeaves()
	{
		super();
		this.setUnlocalizedName("gravewoodLeaves");
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(ModBlocks.gravewoodSapling);
	}
}
