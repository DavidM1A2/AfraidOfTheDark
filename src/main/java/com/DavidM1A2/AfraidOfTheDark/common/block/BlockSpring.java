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

import com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity.BlockTileEntitySpring;

public class BlockSpring extends AOTDBlock implements ITileEntityProvider
{
	public BlockSpring(Material material)
	{
		super(material);
		this.setUnlocalizedName("spring");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new BlockTileEntitySpring();
	}
}
