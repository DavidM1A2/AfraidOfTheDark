/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDBlock;

import net.minecraft.block.material.Material;

public class BlockGnomishMetalStrut extends AOTDBlock
{
	public BlockGnomishMetalStrut()
	{
		super(Material.rock);
		this.setUnlocalizedName("gnomishMetalStrut");
		this.setHardness(2.0F);
		this.setResistance(10.0F);
		this.setHarvestLevel("pickaxe", 2);
	}
}
