package com.DavidM1A2.AfraidOfTheDark.block;

import net.minecraft.block.material.Material;

public class BlockStarMetal extends AOTDBlock
{
	public BlockStarMetal(Material material)
	{
		super(material);
		this.setUnlocalizedName("starMetal");
		this.setHardness(10.0F);
		this.setResistance(50.0F);
	}
}
