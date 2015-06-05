/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block;

import net.minecraft.block.material.Material;

// Double gravewood slab
public class BlockGravewoodDoubleSlab extends AOTDSlab
{
	public BlockGravewoodDoubleSlab(final Material material)
	{
		super(material);
		this.setUnlocalizedName("gravewoodDoubleSlab");
	}

	@Override
	public boolean isDouble()
	{
		return true;
	}
}
