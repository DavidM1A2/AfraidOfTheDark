/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDBlock;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockGravewoodPlanks extends AOTDBlock
{
	public BlockGravewoodPlanks()
	{
		// Simply silver ore
		super(Material.wood);
		this.setUnlocalizedName("gravewoodPlanks");
		this.setStepSound(Block.soundTypeWood);
		this.blockHardness = 2.0F;
		this.blockResistance = 1.0F;
	}
}
