/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.mangrove;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDBlock;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockMangrovePlanks extends AOTDBlock
{
	public BlockMangrovePlanks()
	{
		super(Material.WOOD);
		this.setUnlocalizedName("mangrove_planks");
		this.setRegistryName("mangrove_planks");
		this.setSoundType(SoundType.WOOD);
		this.blockHardness = 2.0F;
		this.blockResistance = 1.0F;
	}
}
