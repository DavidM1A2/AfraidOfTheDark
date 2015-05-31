/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.block;

import net.minecraft.block.material.Material;

public class BlockAstralSilverOre extends AOTDBlock
{
	public BlockAstralSilverOre(final Material material)
	{
		super(material);
		this.setUnlocalizedName("astralSilverOre");
		this.setHardness(10.0F);
		this.setResistance(50.0F);
	}
}
