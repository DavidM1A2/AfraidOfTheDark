/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.block;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class BlockTileEntityBase extends TileEntity
{
	/*
	 * A base class for tile entities
	 */
	public BlockTileEntityBase(Block block)
	{
		super();
		this.blockType = block;
	}

}
