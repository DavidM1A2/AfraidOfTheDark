/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDBlock;

public class BlockIgneous extends AOTDBlock
{
	public BlockIgneous()
	{
		super();
		this.setUnlocalizedName("igneousBlock");
		this.blockHardness = 4.0F;
		this.blockResistance = 1.0F;
		this.setLightLevel(1.0f);
		this.setHarvestLevel("pickaxe", 2);
	}
}
