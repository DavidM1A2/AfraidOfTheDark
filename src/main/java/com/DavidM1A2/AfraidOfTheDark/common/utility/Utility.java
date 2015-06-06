package com.DavidM1A2.AfraidOfTheDark.common.utility;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class Utility
{
	public static int ticksToMilliseconds(int ticks)
	{
		return ticks * 50;
	}

	public static int getPlaceToSpawn(World world, int x, int z, int height, int width) throws UnsupportedLocationException
	{
		int y1 = 0;
		int y2 = 0;
		int y3 = 0;
		int y4 = 0;

		y1 = getTheYValueAtCoords(world, x, z);
		y2 = getTheYValueAtCoords(world, x + width, z);
		y3 = getTheYValueAtCoords(world, x, z + height);
		y4 = getTheYValueAtCoords(world, x + width, z + height);

		if (y1 == 0 || y2 == 0 || y3 == 0 || y4 == 0)
		{
			throw new UnsupportedLocationException(y1, y2, y3, y4);
		}
		else
		{
			return (y1 + y2 + y3 + y4) / 4;
		}
	}

	private static int getTheYValueAtCoords(World world, int x, int z)
	{
		int temp = 255;
		while (temp > 0)
		{
			Block current = world.getBlockState(new BlockPos(x, temp, z)).getBlock();
			if (current.getMaterial() == Material.water)
			{
				return 0;
			}
			if (current instanceof BlockGrass || current instanceof BlockDirt)
			{
				return temp;
			}
			temp = temp - 1;
		}
		return 0;
	}
}
