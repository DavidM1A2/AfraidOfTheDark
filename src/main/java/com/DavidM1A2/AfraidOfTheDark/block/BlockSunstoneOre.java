package com.DavidM1A2.AfraidOfTheDark.block;

import net.minecraft.block.material.Material;

public class BlockSunstoneOre extends AOTDBlock
{
	public BlockSunstoneOre(Material material)
	{
		super(material);
		this.setUnlocalizedName("sunstone");
		this.setLightLevel(1.0f);
		this.setHardness(10.0F);
		this.setResistance(50.0F);
	}
}
