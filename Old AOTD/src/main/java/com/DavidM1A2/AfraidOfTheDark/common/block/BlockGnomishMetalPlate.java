/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDBlock;

import net.minecraft.block.material.Material;

public class BlockGnomishMetalPlate extends AOTDBlock
{
	public BlockGnomishMetalPlate()
	{
		super(Material.ROCK);
		this.setUnlocalizedName("gnomish_metal_plate");
		this.setRegistryName("gnomish_metal_plate");
		this.setHardness(2.0F);
		this.setResistance(10.0F);
		this.setHarvestLevel("pickaxe", 2);
	}
}
