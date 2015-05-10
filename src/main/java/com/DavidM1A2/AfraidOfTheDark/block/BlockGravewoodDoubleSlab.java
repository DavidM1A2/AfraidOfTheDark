package com.DavidM1A2.AfraidOfTheDark.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModBlocks;

public class BlockGravewoodDoubleSlab extends AOTDSlab
{
	public BlockGravewoodDoubleSlab(Material material)
	{
		super(material);
		this.setUnlocalizedName("gravewoodDoubleSlab");
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(ModBlocks.gravewoodDoubleSlab);
	}

	@Override
	public boolean isDouble()
	{
		return true;
	}
}
