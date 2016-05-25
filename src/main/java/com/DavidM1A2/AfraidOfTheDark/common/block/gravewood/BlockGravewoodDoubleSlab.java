/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.gravewood;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDSlab;

import net.minecraft.block.material.Material;

// Double gravewood slab
public class BlockGravewoodDoubleSlab extends AOTDSlab
{
	public BlockGravewoodDoubleSlab()
	{
		super(Material.wood);
		this.setUnlocalizedName("gravewoodDoubleSlab");
	}

	@Override
	public boolean isDouble()
	{
		return true;
	}
}
