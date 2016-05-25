/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.gravewood;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDSlab;

import net.minecraft.block.material.Material;

// Double half slab
public class BlockGravewoodHalfSlab extends AOTDSlab
{
	public BlockGravewoodHalfSlab()
	{
		super(Material.wood);
		this.setUnlocalizedName("gravewoodHalfSlab");
	}

	@Override
	public boolean isDouble()
	{
		return false;
	}
}
