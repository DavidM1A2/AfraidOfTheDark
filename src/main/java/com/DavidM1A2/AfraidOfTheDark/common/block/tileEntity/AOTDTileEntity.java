/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class AOTDTileEntity extends TileEntity
{
	/*
	 * A base class for tile entities
	 */
	public AOTDTileEntity(final Block block)
	{
		super();
		this.blockType = block;
	}
}
