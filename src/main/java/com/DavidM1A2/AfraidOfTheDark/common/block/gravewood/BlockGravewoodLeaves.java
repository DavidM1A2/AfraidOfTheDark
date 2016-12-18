/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.gravewood;

import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDLeaves;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

public class BlockGravewoodLeaves extends AOTDLeaves
{
	/*
	 * Define gravewood leaves block
	 */
	public BlockGravewoodLeaves()
	{
		super();
		this.setUnlocalizedName("gravewoodLeaves");
		this.setRegistryName("gravewoodLeaves");
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(ModBlocks.gravewoodSapling);
	}
}
