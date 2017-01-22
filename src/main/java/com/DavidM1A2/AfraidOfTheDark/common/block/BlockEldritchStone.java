/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDBlock;

import net.minecraft.block.material.Material;

public class BlockEldritchStone extends AOTDBlock
{
	public BlockEldritchStone()
	{
		super(Material.ROCK);
		this.setUnlocalizedName("eldritch_stone");
		this.setRegistryName("eldritch_stone");
		this.setHardness(5.0F);
		this.setHarvestLevel("pickaxe", 1);
	}
}
