/*
*** MADE BY MRPONYCAPTAIN'S .SCHEMATIC TO .JAVA CONVERTING TOOL v2.0 ***
*/

package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.darkForestDungeon;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import java.util.Random;

public class PropFountain
{
	public PropFountain(World world, Random rand, int x, int y, int z) 
	{
		this.generate(world, rand, x, y, z);
	}

	public void setBlock(World world, int x, int y, int z, Block block, int metadata)
	{
		world.setBlockState(new BlockPos(x, y, z), block.getStateFromMeta(metadata));
	}

	public boolean generate(World world, Random rand, int i, int j, int k) {
		this.setBlock(world, i + 0, j + 0, k + 1, Blocks.leaves, 5);
		this.setBlock(world, i + 0, j + 0, k + 2, Blocks.leaves, 13);
		this.setBlock(world, i + 1, j + 0, k + 1, Blocks.cobblestone, 0);
		this.setBlock(world, i + 1, j + 0, k + 2, Blocks.cobblestone, 0);
		this.setBlock(world, i + 1, j + 0, k + 3, Blocks.cobblestone, 0);
		this.setBlock(world, i + 1, j + 1, k + 1, Blocks.air, 0);
		this.setBlock(world, i + 1, j + 1, k + 2, Blocks.air, 0);
		this.setBlock(world, i + 1, j + 1, k + 3, Blocks.air, 0);
		this.setBlock(world, i + 1, j + 2, k + 1, Blocks.air, 0);
		this.setBlock(world, i + 1, j + 2, k + 2, Blocks.air, 0);
		this.setBlock(world, i + 1, j + 2, k + 3, Blocks.air, 0);
		this.setBlock(world, i + 1, j + 3, k + 1, Blocks.air, 0);
		this.setBlock(world, i + 1, j + 3, k + 2, Blocks.air, 0);
		this.setBlock(world, i + 1, j + 3, k + 3, Blocks.air, 0);
		this.setBlock(world, i + 2, j + 0, k + 0, Blocks.cobblestone, 0);
		this.setBlock(world, i + 2, j + 0, k + 1, Blocks.air, 0);
		this.setBlock(world, i + 2, j + 0, k + 2, Blocks.air, 0);
		this.setBlock(world, i + 2, j + 0, k + 3, Blocks.air, 0);
		this.setBlock(world, i + 2, j + 0, k + 4, Blocks.cobblestone, 0);
		this.setBlock(world, i + 2, j + 1, k + 0, Blocks.air, 0);
		this.setBlock(world, i + 2, j + 1, k + 1, Blocks.air, 0);
		this.setBlock(world, i + 2, j + 1, k + 2, Blocks.air, 0);
		this.setBlock(world, i + 2, j + 1, k + 3, Blocks.air, 0);
		this.setBlock(world, i + 2, j + 1, k + 4, Blocks.air, 0);
		this.setBlock(world, i + 2, j + 2, k + 0, Blocks.air, 0);
		this.setBlock(world, i + 2, j + 2, k + 1, Blocks.air, 0);
		this.setBlock(world, i + 2, j + 2, k + 2, Blocks.air, 0);
		this.setBlock(world, i + 2, j + 2, k + 3, Blocks.air, 0);
		this.setBlock(world, i + 2, j + 2, k + 4, Blocks.air, 0);
		this.setBlock(world, i + 2, j + 3, k + 0, Blocks.air, 0);
		this.setBlock(world, i + 2, j + 3, k + 1, Blocks.air, 0);
		this.setBlock(world, i + 2, j + 3, k + 2, Blocks.air, 0);
		this.setBlock(world, i + 2, j + 3, k + 3, Blocks.air, 0);
		this.setBlock(world, i + 2, j + 3, k + 4, Blocks.air, 0);
		this.setBlock(world, i + 3, j + 0, k + 0, Blocks.cobblestone, 0);
		this.setBlock(world, i + 3, j + 0, k + 1, Blocks.air, 0);
		this.setBlock(world, i + 3, j + 0, k + 2, Blocks.cobblestone, 0);
		this.setBlock(world, i + 3, j + 0, k + 3, Blocks.air, 0);
		this.setBlock(world, i + 3, j + 0, k + 4, Blocks.cobblestone, 0);
		this.setBlock(world, i + 3, j + 1, k + 0, Blocks.air, 0);
		this.setBlock(world, i + 3, j + 1, k + 1, Blocks.air, 0);
		this.setBlock(world, i + 3, j + 1, k + 2, Blocks.cobblestone, 0);
		this.setBlock(world, i + 3, j + 1, k + 3, Blocks.air, 0);
		this.setBlock(world, i + 3, j + 1, k + 4, Blocks.air, 0);
		this.setBlock(world, i + 3, j + 2, k + 0, Blocks.air, 0);
		this.setBlock(world, i + 3, j + 2, k + 1, Blocks.air, 0);
		this.setBlock(world, i + 3, j + 2, k + 2, Blocks.cobblestone, 0);
		this.setBlock(world, i + 3, j + 2, k + 3, Blocks.air, 0);
		this.setBlock(world, i + 3, j + 2, k + 4, Blocks.air, 0);
		this.setBlock(world, i + 3, j + 3, k + 0, Blocks.air, 0);
		this.setBlock(world, i + 3, j + 3, k + 1, Blocks.air, 0);
		this.setBlock(world, i + 3, j + 3, k + 3, Blocks.air, 0);
		this.setBlock(world, i + 3, j + 3, k + 4, Blocks.air, 0);
		this.setBlock(world, i + 4, j + 0, k + 0, Blocks.cobblestone, 0);
		this.setBlock(world, i + 4, j + 0, k + 1, Blocks.air, 0);
		this.setBlock(world, i + 4, j + 0, k + 2, Blocks.air, 0);
		this.setBlock(world, i + 4, j + 0, k + 3, Blocks.air, 0);
		this.setBlock(world, i + 4, j + 0, k + 4, Blocks.cobblestone, 0);
		this.setBlock(world, i + 4, j + 0, k + 5, Blocks.leaves, 13);
		this.setBlock(world, i + 4, j + 1, k + 0, Blocks.air, 0);
		this.setBlock(world, i + 4, j + 1, k + 1, Blocks.air, 0);
		this.setBlock(world, i + 4, j + 1, k + 2, Blocks.air, 0);
		this.setBlock(world, i + 4, j + 1, k + 3, Blocks.air, 0);
		this.setBlock(world, i + 4, j + 1, k + 4, Blocks.air, 0);
		this.setBlock(world, i + 4, j + 2, k + 0, Blocks.air, 0);
		this.setBlock(world, i + 4, j + 2, k + 1, Blocks.air, 0);
		this.setBlock(world, i + 4, j + 2, k + 2, Blocks.air, 0);
		this.setBlock(world, i + 4, j + 2, k + 3, Blocks.air, 0);
		this.setBlock(world, i + 4, j + 2, k + 4, Blocks.air, 0);
		this.setBlock(world, i + 4, j + 3, k + 0, Blocks.air, 0);
		this.setBlock(world, i + 4, j + 3, k + 1, Blocks.air, 0);
		this.setBlock(world, i + 4, j + 3, k + 2, Blocks.air, 0);
		this.setBlock(world, i + 4, j + 3, k + 3, Blocks.air, 0);
		this.setBlock(world, i + 4, j + 3, k + 4, Blocks.air, 0);
		this.setBlock(world, i + 5, j + 0, k + 1, Blocks.cobblestone, 0);
		this.setBlock(world, i + 5, j + 0, k + 2, Blocks.cobblestone, 0);
		this.setBlock(world, i + 5, j + 0, k + 3, Blocks.cobblestone, 0);
		this.setBlock(world, i + 5, j + 1, k + 1, Blocks.air, 0);
		this.setBlock(world, i + 5, j + 1, k + 2, Blocks.air, 0);
		this.setBlock(world, i + 5, j + 1, k + 3, Blocks.air, 0);
		this.setBlock(world, i + 5, j + 2, k + 1, Blocks.air, 0);
		this.setBlock(world, i + 5, j + 2, k + 2, Blocks.air, 0);
		this.setBlock(world, i + 5, j + 2, k + 3, Blocks.air, 0);
		this.setBlock(world, i + 5, j + 3, k + 1, Blocks.air, 0);
		this.setBlock(world, i + 5, j + 3, k + 2, Blocks.air, 0);
		this.setBlock(world, i + 5, j + 3, k + 3, Blocks.air, 0);
		this.setBlock(world, i + 3, j + 3, k + 2, Blocks.water, 0);

		return true;
	}
}