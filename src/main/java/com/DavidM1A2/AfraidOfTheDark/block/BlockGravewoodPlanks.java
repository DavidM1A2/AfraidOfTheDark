package com.DavidM1A2.AfraidOfTheDark.block;

import net.minecraft.block.Block;

public class BlockGravewoodPlanks extends BlockBase
{
	public BlockGravewoodPlanks()
	{
		// Simply silver ore
		super();
		this.setUnlocalizedName("gravewoodPlanks");
		this.setStepSound(Block.soundTypeWood);
		this.blockHardness = 2.0F;
		this.blockResistance = 1.0F;
	}
}
