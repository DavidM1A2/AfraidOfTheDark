/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.mangrove;

import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDSapling;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;

import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockMangroveSapling extends AOTDSapling
{
	private IBlockState logUp = ModBlocks.mangrove.getDefaultState().withProperty(BlockLog.LOG_AXIS, EnumAxis.Y);

	public BlockMangroveSapling()
	{
		super();
		this.setUnlocalizedName("mangroveSapling");
	}

	@Override
	public void causeTreeToGrow(World world, BlockPos blockPos, IBlockState iBlockState, Random random)
	{
		world.setBlockState(blockPos, Blocks.air.getDefaultState());

		BlockPos topOfTrunk = this.generateTrunk(world, blockPos, iBlockState, random);

		this.generateBranches(world, topOfTrunk, iBlockState, random);
	}

	// Returns the blockpos of the top of the trunk
	private BlockPos generateTrunk(World world, BlockPos blockPos, IBlockState iBlockState, Random random)
	{
		int heightBeforeTrunk = random.nextInt(4) + 4;
		int numTrunkBranches = random.nextInt(2) + 3;

		for (int i = 0; i < numTrunkBranches; i++)
		{
			EnumFacing offsetDirection = EnumFacing.HORIZONTALS[random.nextInt(EnumFacing.HORIZONTALS.length)];
			int offsetAmount = random.nextInt(3) + 2;
			BlockPos starting = blockPos.offset(offsetDirection, offsetAmount);
			for (int j = 0; j < heightBeforeTrunk; j++)
			{
				// Create little "chunks" coming off of the 4 axis
				if (offsetAmount == 1)
					if (random.nextDouble() < 0.2D)
						this.setBlockIfPossible(world, starting.offset(random.nextBoolean() ? offsetDirection.rotateY() : offsetDirection.rotateY().getOpposite()), logUp);

				// Not in center yet, so check to see if we move towards mid
				if (offsetAmount != 0)
					if (random.nextBoolean())
					{
						starting = starting.offset(offsetDirection.getOpposite());
						offsetAmount--;
					}

				// Add a log
				this.setBlockIfPossible(world, starting, logUp);

				// If random check suceeds, add an extra log above or below
				if (random.nextDouble() < 0.2)
					this.setBlockIfPossible(world, starting.offset(random.nextBoolean() ? EnumFacing.UP : EnumFacing.DOWN), logUp);

				// Move up
				starting = starting.up();
			}
			// Add horizontal logs if neceessary
			while (offsetAmount > 0)
			{
				starting = starting.offset(offsetDirection.getOpposite());
				this.setBlockIfPossible(world, starting, logUp);
				offsetAmount--;
			}
		}

		return blockPos.offset(EnumFacing.UP, heightBeforeTrunk);
	}

	private void generateBranches(World world, BlockPos topOfTree, IBlockState iBlockState, Random random)
	{
		int numBranches = random.nextInt(3) + 3;
		for (int i = 0; i < numBranches; i++)
		{
			EnumFacing direction = EnumFacing.getHorizontal(random.nextInt(4));
			EnumFacing directionOther = random.nextBoolean() ? direction.rotateY() : direction.rotateYCCW();
			int branchLength = random.nextInt(5) + 3;
			BlockPos current = topOfTree.offset(direction);
			for (int j = 0; j < branchLength; j++)
			{
				this.setBlockIfPossible(world, current, logUp);

				// If random check suceeds, add an extra log above or below
				if (random.nextDouble() < 0.2)
					this.setBlockIfPossible(world, current.offset(random.nextBoolean() ? EnumFacing.UP : EnumFacing.DOWN), logUp);

				// The next place we move to will be somewhat random
				EnumFacing nextMove = random.nextDouble() > 0.4 ? direction : directionOther;

				// Randomly generate clusters of leaves along the way
				if (random.nextDouble() < 0.15)
					this.createLeafCluster(world, current);

				// Move
				current = current.offset(nextMove);

				// Randomly move up
				if (random.nextDouble() < 0.2)
					current = current.offset(EnumFacing.UP);
			}

			this.createLeafCluster(world, current);
		}

		this.createLeafCluster(world, topOfTree);
	}

	private void setBlockIfPossible(World world, BlockPos location, IBlockState blockState)
	{
		IBlockState current = world.getBlockState(location);
		if (current.getBlock().isAir(world, location) || current.getBlock().isLeaves(world, location))
		{
			world.setBlockState(location, blockState);
		}
	}

	private void createLeafCluster(World world, BlockPos location)
	{
		this.setBlockIfPossible(world, location, logUp);

		for (int y = -1; y < 2; y++)
			for (int x = -2; x < 3; x++)
				for (int z = -2; z < 3; z++)
				{
					BlockPos current = location.add(x, y, z);
					if (current.compareTo(location) != 0 && Math.sqrt(current.distanceSq(location)) < (y == 0 ? 2.5 : 2))
					{
						if (world.rand.nextDouble() > 0.1)
						{
							// Random vines coming down
							if (world.rand.nextDouble() < 0.03)
							{
								this.setBlockIfPossible(world, current, logUp);
								int vineLength = world.rand.nextInt(3) + 2;
								for (int i = 1; i < vineLength; i++)
									this.setBlockIfPossible(world, current.offset(EnumFacing.DOWN, i), ModBlocks.mangroveLeaves.getDefaultState());
							}
							// Just a leaf
							else
							{
								this.setBlockIfPossible(world, current, ModBlocks.mangroveLeaves.getDefaultState());
							}
						}
					}
				}
	}

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
	{
		return true;
	}
}