/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.core;

import net.minecraft.block.Block;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;

public abstract class AOTDTileEntity extends TileEntity implements IUpdatePlayerListBox
{
	/*
	 * A base class for tile entities
	 */
	public AOTDTileEntity(final Block block)
	{
		super();
		this.blockType = block;
	}

	@Override
	public void update()
	{
	}
}
