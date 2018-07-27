/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.mangrove;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDStairs;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;

public class BlockMangroveStairs extends AOTDStairs
{
	public BlockMangroveStairs()
	{
		super(ModBlocks.mangrovePlanks.getDefaultState());
		this.setUnlocalizedName("mangrove_stairs");
		this.setRegistryName("mangrove_stairs");
	}
}
