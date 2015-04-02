/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.block;

import net.minecraft.entity.player.EntityPlayer;

import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModBlocks;

public class BlockTileEntityDarkness extends BlockTileEntityBase
{
	private int ticksExisted = 0;
	private final int NUMBEROFBLOCKSAWAYTOCHECK = 10;
	private final int TICKSINBETWEENCHECKS = 100;

	/*
	 * An example tile entity
	 */
	public BlockTileEntityDarkness()
	{
		super(ModBlocks.darkness);
	}

	// Update is called every tick (20 times per second)
	@Override
	public void updateEntity()
	{
		if (ticksExisted % TICKSINBETWEENCHECKS == 0)
		{
			EntityPlayer entityPlayer = anyPlayerInRange();
			if (entityPlayer != null)
			{
				// Do Stuff
			}
			ticksExisted = 1;
		}
		else
		{
			ticksExisted = ticksExisted + 1;
		}
	}

	// Quick example function to get any players within some range
	public EntityPlayer anyPlayerInRange()
	{
		return this.worldObj.getClosestPlayer((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D, NUMBEROFBLOCKSAWAYTOCHECK);
	}
}
