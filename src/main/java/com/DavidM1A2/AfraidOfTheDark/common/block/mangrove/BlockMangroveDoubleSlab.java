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
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockMangroveDoubleSlab extends AOTDSlab
{
	public BlockMangroveDoubleSlab()
	{
		super(Material.WOOD);
		this.setUnlocalizedName("mangrove_upper_slab");
		this.setRegistryName("mangrove_upper_slab");
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
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
		return new ItemStack(ModBlocks.mangroveHalfSlab, 1, 0);
	}
}
