/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block;

import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDSapling;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDTreeTypes;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockGravewoodSapling extends AOTDSapling
{
	public BlockGravewoodSapling()
	{
		super();
		this.setUnlocalizedName("gravewoodSapling");
	}

	@Override
	public void causeTreeToGrow(World world, BlockPos blockPos, IBlockState iBlockState, Random random)
	{
		world.setBlockState(blockPos, Blocks.air.getDefaultState());

		int i = random.nextInt(3) + 5;
		int i2;
		int k;
		int l;

		for (i2 = (blockPos.getY() - 3) + i; i2 <= (blockPos.getY() + i); ++i2)
		{
			k = i2 - (blockPos.getY() + i);
			l = 1 - (k / 2);

			for (int i1 = blockPos.getX() - l; i1 <= (blockPos.getX() + l); ++i1)
			{
				final int j1 = i1 - blockPos.getX();

				for (int k1 = blockPos.getZ() - l; k1 <= (blockPos.getZ() + l); ++k1)
				{
					final int l1 = k1 - blockPos.getZ();

					if ((Math.abs(j1) != l) || (Math.abs(l1) != l) || ((random.nextInt(2) != 0) && (k != 0)))
					{
						final BlockPos blockpos1 = new BlockPos(i1, i2, k1);
						final Block block = world.getBlockState(blockpos1).getBlock();

						if (block.isAir(world, blockpos1) || block.isLeaves(world, blockpos1))
						{
							world.setBlockState(blockpos1, ModBlocks.gravewoodLeaves.getStateFromMeta(AOTDTreeTypes.GRAVEWOOD.getMetadata()));
						}
					}
				}
			}
		}

		for (i2 = 0; i2 < i; ++i2)
		{
			final BlockPos upN = blockPos.offset(EnumFacing.UP, i2);
			final Block block2 = world.getBlockState(upN).getBlock();

			if (block2.isAir(world, upN) || block2.isLeaves(world, upN))
			{
				world.setBlockState(blockPos.offset(EnumFacing.UP, i2), ModBlocks.gravewood.getStateFromMeta(AOTDTreeTypes.GRAVEWOOD.getMetadata()));
			}
		}
	}

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
	{
		return true;
	}
}
