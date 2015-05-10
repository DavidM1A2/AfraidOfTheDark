package com.DavidM1A2.AfraidOfTheDark.block;

import net.minecraft.block.Block;

public class BlockGravewoodPlanks extends AOTDBlock
{
	// public static final PropertyEnum VARIANT_PROP = PropertyEnum.create("variant", AOTDTreeTypes.class, new Predicate()
	// {
	// @Override
	// public boolean apply(Object input)
	// {
	// AOTDTreeTypes type = (AOTDTreeTypes) input;
	// if (type == AOTDTreeTypes.GRAVEWOOD)
	// {
	// return true;
	// }
	// return false;
	// }
	// });

	public BlockGravewoodPlanks()
	{
		// Simply silver ore
		super();
		this.setUnlocalizedName("gravewoodPlanks");
		this.setStepSound(Block.soundTypeWood);
		this.blockHardness = 2.0F;
		this.blockResistance = 1.0F;
	}

	// protected BlockState createBlockState()
	// {
	// return new BlockState(this, VARIANT_PROP);
	// }
}
