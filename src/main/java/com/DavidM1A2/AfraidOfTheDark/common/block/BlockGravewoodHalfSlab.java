/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block;

import net.minecraft.block.material.Material;

// Double half slab
public class BlockGravewoodHalfSlab extends AOTDSlab
{
	public BlockGravewoodHalfSlab(final Material material)
	{
		super(material);
		this.setUnlocalizedName("gravewoodHalfSlab");
	}

	@Override
	public boolean isDouble()
	{
		return false;
	}
}
