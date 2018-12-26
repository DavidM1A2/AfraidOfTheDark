/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.gravewood;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDLog;

import net.minecraft.block.Block;

public class BlockGravewood extends AOTDLog
{
	/*
	 * Define a gravewood block
	 */
	public BlockGravewood()
	{
		super();
		this.setStepSound(Block.soundTypeWood);
		this.setUnlocalizedName("gravewood");
	}
}
