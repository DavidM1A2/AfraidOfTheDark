/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.mangrove;

import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDSlab;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockMangroveDoubleSlab extends AOTDSlab
{
	public BlockMangroveDoubleSlab()
	{
		super(Material.wood);
		this.setUnlocalizedName("mangroveDoubleSlab");
	}

	@Override
	public boolean isDouble()
	{
		return true;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(ModBlocks.mangroveHalfSlab);
	}

	@Override
	public Item getItem(World worldIn, BlockPos pos)
	{
		return Item.getItemFromBlock(ModBlocks.mangroveHalfSlab);
	}
}
