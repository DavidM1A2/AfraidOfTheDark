package com.DavidM1A2.AfraidOfTheDark.common.utility;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDirt.DirtType;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockSnow;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;

import com.DavidM1A2.AfraidOfTheDark.common.block.BlockGravewood;

public class Utility
{
	public static int ticksToMilliseconds(int ticks)
	{
		return ticks * 50;
	}

	public static int getPlaceToSpawnAverage(World world, int x, int z, int height, int width) throws UnsupportedLocationException
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

	public static int getPlaceToSpawnLowest(World world, int x, int z, int height, int width) throws UnsupportedLocationException
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
			return Math.min(y1, Math.min(y2, Math.min(y3, y4)));
		}
	}

	private static int getTheYValueAtCoords(World world, int x, int z)
	{
		int temp = 255;
		while (temp > 0)
		{
			Block current = world.getBlockState(new BlockPos(x, temp, z)).getBlock();
			if (current instanceof BlockFluidBase)
			{
				LogHelper.info("Fluid  " + current + "  y = " + temp);
				world.setBlockState(new BlockPos(x, temp, z), Blocks.diamond_block.getDefaultState());
				return 0;
			}
			if (current instanceof BlockGrass)
			{
				LogHelper.info("Grass  " + current + "  y = " + temp);
				world.setBlockState(new BlockPos(x, temp, z), Blocks.diamond_block.getDefaultState());
				return temp;
			}
			if (current instanceof BlockDirt)
			{
				if (world.canSeeSky(new BlockPos(x, temp, z)))
				{
					LogHelper.info("Sky  " + current + "  y = " + temp);
					world.setBlockState(new BlockPos(x, temp, z), Blocks.diamond_block.getDefaultState());
					return temp;
				}
				else if (world.getBlockState(new BlockPos(x, temp, z)) == Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, DirtType.PODZOL))
				{
					LogHelper.info("Podzol  " + current + "  y = " + temp);
					world.setBlockState(new BlockPos(x, temp, z), Blocks.diamond_block.getDefaultState());
					return temp;
				}
				else if (world.getBlockState(new BlockPos(x, temp + 1, z)).getBlock() instanceof BlockLog || world.getBlockState(new BlockPos(x, temp + 1, z)).getBlock() instanceof BlockGravewood)
				{
					LogHelper.info("Log  " + current + "  y = " + temp);
					world.setBlockState(new BlockPos(x, temp, z), Blocks.diamond_block.getDefaultState());
					return temp;
				}
			}
			if (world.getBlockState(new BlockPos(x, temp + 1, z)).getBlock() instanceof BlockSnow)
			{
				LogHelper.info("Snow  " + current + "  y = " + temp);
				world.setBlockState(new BlockPos(x, temp, z), Blocks.diamond_block.getDefaultState());
				return temp;
			}
			temp = temp - 1;
		}
		return 0;
	}
}
