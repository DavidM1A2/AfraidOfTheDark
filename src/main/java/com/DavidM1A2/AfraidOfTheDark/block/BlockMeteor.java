package com.DavidM1A2.AfraidOfTheDark.block;

import net.minecraft.block.material.Material;

public class BlockMeteor extends AOTDBlock
{
	public BlockMeteor(final Material material)
	{
		super(material);
		this.setUnlocalizedName("meteor");
		this.setHardness(10.0F);
		this.setResistance(50.0F);
	}
}
