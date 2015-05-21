package com.DavidM1A2.AfraidOfTheDark.block;

import net.minecraft.block.material.Material;

public class BlockMeteoricSilver extends AOTDBlock
{
	public BlockMeteoricSilver(Material material)
	{
		super(material);
		this.setUnlocalizedName("meteoricSilver");
		this.setHardness(10.0F);
		this.setResistance(50.0F);
	}
}
