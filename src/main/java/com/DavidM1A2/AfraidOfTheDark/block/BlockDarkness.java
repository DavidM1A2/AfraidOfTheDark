/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

public class BlockDarkness extends BlockContainer
{
	/*
	 * Temporary tile entity block to understand how TE's work
	 */
	public BlockDarkness(Material material)
	{
		super(material);
		this.setHardness(9.0F);
		this.setResistance(5.0F);
		this.isBlockContainer = true;
		this.setCreativeTab(Refrence.AFRAID_OF_THE_DARK);
		this.setUnlocalizedName("darkness");
	}

	// Decide what type of tile entity this should be
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new BlockTileEntityDarkness();
	}

	@Override
	public String getUnlocalizedName()
	{
		return String.format("tile.%s%s", Refrence.MOD_ID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
		// tile.modid:blockname.name
	}

	protected String getUnwrappedUnlocalizedName(String unlocalizedName)
	{
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}
}
