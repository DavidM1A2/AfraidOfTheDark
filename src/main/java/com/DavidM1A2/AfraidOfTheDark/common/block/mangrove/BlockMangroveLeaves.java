/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.mangrove;

import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDLeaves;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

public class BlockMangroveLeaves extends AOTDLeaves
{
	/*
	 * Define mangrove leaves block
	 */
	public BlockMangroveLeaves()
	{
		super();
		this.setUnlocalizedName("mangrove_leaves");
		this.setRegistryName("mangrove_leaves");
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(ModBlocks.mangroveSapling);
	}
}