/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block;

import net.minecraft.block.Block;

public class BlockGravewoodPlanks extends AOTDBlock
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
