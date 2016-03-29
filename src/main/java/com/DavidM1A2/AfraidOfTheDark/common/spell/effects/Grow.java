/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class Grow extends Effect
{
	@Override
	public int getCost()
	{
		return 0;
	}

	@Override
	public void performEffect(BlockPos location, World world)
	{
		location = location.offset(EnumFacing.DOWN);
		for (int i = 0; i < 3; i++)
		{
			IBlockState current = world.getBlockState(location);
			if (current.getBlock() instanceof IGrowable)
			{
				IGrowable igrowable = (IGrowable) current.getBlock();

				if (igrowable.canGrow(world, location, current, world.isRemote))
				{
					if (!world.isRemote)
					{
						if (igrowable.canUseBonemeal(world, world.rand, location, current))
						{
							igrowable.grow(world, world.rand, location, current);
							return;
						}
					}
				}
			}
			location = location.offset(EnumFacing.UP);
		}
	}

	@Override
	public void performEffect(Entity entity)
	{
		return;
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
