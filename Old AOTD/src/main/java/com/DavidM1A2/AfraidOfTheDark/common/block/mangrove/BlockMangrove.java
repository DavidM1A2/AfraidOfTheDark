/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.mangrove;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDLog;

import net.minecraft.block.SoundType;

public class BlockMangrove extends AOTDLog
{
	/*
	 * Define a mangrove block
	 */
	public BlockMangrove()
	{
		super();
		this.setSoundType(SoundType.WOOD);
		this.setUnlocalizedName("mangrove");
		this.setRegistryName("mangrove");
	}
}
