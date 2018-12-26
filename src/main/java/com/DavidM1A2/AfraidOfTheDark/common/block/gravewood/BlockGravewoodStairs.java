/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.gravewood;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDStairs;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;

public class BlockGravewoodStairs extends AOTDStairs
{
	public BlockGravewoodStairs()
	{
		super(ModBlocks.gravewoodPlanks.getDefaultState());
		this.setUnlocalizedName("gravewoodStairs");
	}
}
