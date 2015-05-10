package com.DavidM1A2.AfraidOfTheDark.block;

import net.minecraft.block.material.Material;

public class BlockGravewoodDoubleSlab extends AOTDSlab
{
	public BlockGravewoodDoubleSlab(Material material)
	{
		super(material);
		this.setUnlocalizedName("gravewoodDoubleSlab");
	}

	@Override
	public boolean isDouble()
	{
		return true;
	}
}
