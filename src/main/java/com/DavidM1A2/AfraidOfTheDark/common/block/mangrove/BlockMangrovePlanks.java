/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.mangrove;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDBlock;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockMangrovePlanks extends AOTDBlock
{
	public BlockMangrovePlanks()
	{
		super(Material.wood);
		this.setUnlocalizedName("mangrovePlanks");
		this.setStepSound(Block.soundTypeWood);
		this.blockHardness = 2.0F;
		this.blockResistance = 1.0F;
	}
}
