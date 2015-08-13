/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDBlock;

import net.minecraft.block.material.Material;

public class BlockGlowStalk extends AOTDBlock
{
	public BlockGlowStalk()
	{
		super(Material.rock);
		this.setUnlocalizedName("glowStalk");
		this.setLightLevel(1.0f);
	}
}
