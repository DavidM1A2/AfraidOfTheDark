/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.core;

import net.minecraft.block.Block;
import net.minecraft.util.ITickable;

public abstract class AOTDTickingTileEntity extends AOTDTileEntity implements ITickable
{
	protected long ticksExisted = 0;

	public AOTDTickingTileEntity(Block block)
	{
		super(block);
	}

	@Override
	public void update()
	{
		this.ticksExisted = this.ticksExisted + 1;
	}
}
