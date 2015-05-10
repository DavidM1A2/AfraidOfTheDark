package com.DavidM1A2.AfraidOfTheDark.block;

import net.minecraft.block.material.Material;

public class BlockGravewoodHalfSlab extends AOTDSlab
{
	public BlockGravewoodHalfSlab(Material material)
	{
		super(material);
		this.setUnlocalizedName("gravewoodHalfSlab");
	}

	@Override
	public boolean isDouble()
	{
		return false;
	}
}
