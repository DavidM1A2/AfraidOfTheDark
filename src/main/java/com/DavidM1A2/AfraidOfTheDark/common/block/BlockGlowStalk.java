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
		super(Material.GROUND);
		this.setUnlocalizedName("glow_stalk");
		this.setRegistryName("glow_stalk");
		this.setLightLevel(1.0f);
		this.setHardness(1.0F);
		this.setResistance(4.0F);
	}
}
