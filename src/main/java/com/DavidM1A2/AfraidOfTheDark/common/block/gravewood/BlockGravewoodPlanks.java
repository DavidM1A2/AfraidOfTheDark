/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.gravewood;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDBlock;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockGravewoodPlanks extends AOTDBlock
{
	public BlockGravewoodPlanks()
	{
		// Simply silver ore
		super(Material.WOOD);
		this.setUnlocalizedName("gravewood_planks");
		this.setRegistryName("gravewood_planks");
		this.setSoundType(SoundType.WOOD);
		this.blockHardness = 2.0F;
		this.blockResistance = 1.0F;
	}
}
