/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity.BlockTileEntityDarkForest;

public class BlockDarkForest extends AOTDBlock implements ITileEntityProvider
{
	public BlockDarkForest(Material material)
	{
		super(material);
		this.setUnlocalizedName("darkForest");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new BlockTileEntityDarkForest();
	}
}
