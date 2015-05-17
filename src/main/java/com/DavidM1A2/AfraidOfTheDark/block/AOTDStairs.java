/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.block;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;

import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

public abstract class AOTDStairs extends BlockStairs
{
	protected AOTDStairs(IBlockState modelState)
	{
		super(modelState);
		this.setUnlocalizedName("FORGOT TO SET");
		this.setCreativeTab(Refrence.AFRAID_OF_THE_DARK);
		this.useNeighborBrightness = true;
	}

	@Override
	public String getUnlocalizedName()
	{
		return String.format("tile.%s%s", Refrence.MOD_ID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
		// Format for a block is: tile.modid:blockname.name
	}

	// Get the unlocalized name
	protected String getUnwrappedUnlocalizedName(String unlocalizedName)
	{
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}
}
