/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.gravewood;

import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDSlab;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

// Double half slab
public class BlockGravewoodHalfSlab extends AOTDSlab
{
	public BlockGravewoodHalfSlab()
	{
		super(Material.WOOD);
		this.setUnlocalizedName("gravewood_half_slab");
		this.setRegistryName("gravewood_half_slab");
	}

	@Override
	public boolean isDouble()
	{
		return false;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(ModBlocks.gravewoodHalfSlab);
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
		return new ItemStack(ModBlocks.gravewoodHalfSlab, 1, 0);
	}
}
