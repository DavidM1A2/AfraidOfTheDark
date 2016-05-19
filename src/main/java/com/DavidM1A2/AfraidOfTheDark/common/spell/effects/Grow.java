/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

import com.DavidM1A2.AfraidOfTheDark.common.utility.VitaeUtils;

import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class Grow extends Effect
{
	@Override
	public int getCost()
	{
		return 7;
	}

	@Override
	public void performEffect(BlockPos location, World world, double radius)
	{
		int blockRadius = (int) Math.floor(radius);
		if (blockRadius < 0)
			blockRadius = 0;
		for (int x = -blockRadius; x < blockRadius + 1; x++)
			for (int y = -2; y < 1; y++)
				for (int z = -blockRadius; z < blockRadius + 1; z++)
					growLocation(world, location.add(x, y, z));
	}

	@Override
	public void performEffect(Entity entity)
	{
		if (entity.motionY < .2)
			entity.motionY = entity.motionY + 0.4;
	}

	private boolean growLocation(World world, BlockPos location)
	{
		IBlockState current = world.getBlockState(location);
		if (current.getBlock() instanceof IGrowable)
		{
			IGrowable igrowable = (IGrowable) current.getBlock();
			if (!world.isRemote && igrowable.canGrow(world, location, current, world.isRemote))
			{
				igrowable.grow(world, world.rand, location, current);
				VitaeUtils.vitaeReleasedFX(world, location, .4, 2);
			}

			return true;
		}
		return false;

	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
	}

	@Override
	public Effects getType()
	{
		return Effects.Grow;
	}

}
