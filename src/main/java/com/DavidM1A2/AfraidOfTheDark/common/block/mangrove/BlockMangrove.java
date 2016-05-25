/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.mangrove;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDLog;

import net.minecraft.block.Block;

public class BlockMangrove extends AOTDLog
{
	/*
	 * Define a mangrove block
	 */
	public BlockMangrove()
	{
		super();
		this.setStepSound(Block.soundTypeWood);
		this.setUnlocalizedName("mangrove");
	}
}
