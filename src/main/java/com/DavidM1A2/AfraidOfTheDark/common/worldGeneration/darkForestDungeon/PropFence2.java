/*
*** MADE BY MRPONYCAPTAIN'S .SCHEMATIC TO .JAVA CONVERTING TOOL v2.0 ***
*/

package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.darkForestDungeon;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class PropFence2
{
	public PropFence2(World world, Random rand, int x, int y, int z)
	{
		this.generate(world, rand, x, y, z);
	}

	public void setBlock(World world, int x, int y, int z, Block block, int metadata)
	{
		world.setBlockState(new BlockPos(x, y, z), block.getStateFromMeta(metadata));
	}

	public boolean generate(World world, Random rand, int i, int j, int k)
	{

		this.setBlock(world, i + 0, j + 0, k + 0, Blocks.stonebrick, 0);
		this.setBlock(world, i + 0, j + 0, k + 1, Blocks.stonebrick, 0);
		this.setBlock(world, i + 0, j + 0, k + 2, Blocks.stonebrick, 0);
		this.setBlock(world, i + 0, j + 1, k + 0, Blocks.stonebrick, 0);
		this.setBlock(world, i + 0, j + 1, k + 1, Blocks.iron_bars, 0);
		this.setBlock(world, i + 0, j + 1, k + 2, Blocks.iron_bars, 0);

		return true;
	}
}