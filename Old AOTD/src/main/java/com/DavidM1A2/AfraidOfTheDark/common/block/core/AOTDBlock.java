/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.core;

import com.DavidM1A2.AfraidOfTheDark.common.reference.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * This will be the base for all of our modded blocks
 */
public abstract class AOTDBlock extends Block
{
	public AOTDBlock(final Material material)
	{
		// Set the material and creative tab
		super(material);
		if (this.displayInCreative())
		{
			this.setCreativeTab(Reference.AFRAID_OF_THE_DARK);
		}
		this.setUnlocalizedName("FORGOT TO SET");
	}

	protected boolean displayInCreative()
	{
		return true;
	}

	// Default material is rock (stone)
	public AOTDBlock()
	{
		this(Material.ROCK);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this);
	}

	// the block will render in the SOLID layer. See
	// http://greyminecraftcoder.blogspot.co.at/2014/12/block-rendering-18.html
	// for more information.
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.SOLID;
	}

	// used by the renderer to control lighting and visibility of other blocks.
	// set to true because this block is opaque and occupies the entire 1x1x1
	// space
	// not strictly required because the default (super method) is true
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return true;
	}

	// used by the renderer to control lighting and visibility of other blocks,
	// also by
	// (eg) wall or fence to control whether the fence joins itself to this
	// block
	// set to true because this block occupies the entire 1x1x1 space
	// not strictly required because the default (super method) is true
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return true;
	}

	// render using a BakedModel (mbe01_block_simple.json -->
	// mbe01_block_simple_model.json)
	// not strictly required because the default (super method) is 3.
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public String getUnlocalizedName()
	{
		return String.format("tile.%s%s", Reference.MOD_ID.toLowerCase() + ":", this.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
		// Format for a block is: tile.modid:blockname.name
	}

	// Get the unlocalized name
	protected String getUnwrappedUnlocalizedName(final String unlocalizedName)
	{
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}
}
