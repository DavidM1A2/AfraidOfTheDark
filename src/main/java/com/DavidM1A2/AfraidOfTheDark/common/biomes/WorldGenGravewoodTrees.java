/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.biomes;

import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDTreeTypes;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class WorldGenGravewoodTrees extends WorldGenAbstractTree
{
	private IBlockState leavesMeta = ModBlocks.gravewoodLeaves.getStateFromMeta(AOTDTreeTypes.GRAVEWOOD.getMetadata());
	private IBlockState woodMeta = ModBlocks.gravewood.getStateFromMeta(AOTDTreeTypes.GRAVEWOOD.getMetadata());

	public WorldGenGravewoodTrees(final boolean notify)
	{
		super(notify);
	}

	// Slightly modified tree generator based on default MC tree used to get
	// Gravewood Leaves and Wood
	@Override
	public boolean generate(final World worldIn, final Random random, final BlockPos location)
	{
		int i = random.nextInt(3) + 5;

		boolean flag = true;

		if ((location.getY() >= 1) && ((location.getY() + i + 1) <= 256))
		{
			int k;
			int l;

			for (int j = location.getY(); j <= (location.getY() + 1 + i); ++j)
			{
				byte b0 = 1;

				if (j == location.getY())
				{
					b0 = 0;
				}

				if (j >= ((location.getY() + 1 + i) - 2))
				{
					b0 = 2;
				}

				for (k = location.getX() - b0; (k <= (location.getX() + b0)) && flag; ++k)
				{
					for (l = location.getZ() - b0; (l <= (location.getZ() + b0)) && flag; ++l)
					{
						if ((j >= 0) && (j < 256))
						{
							if (!this.isReplaceable(worldIn, new BlockPos(k, j, l)))
							{
								flag = false;
							}
						}
						else
						{
							flag = false;
						}
					}
				}
			}

			if (!flag)
			{
				return false;
			}
			else
			{
				final BlockPos down = location.offset(EnumFacing.DOWN);
				final Block block1 = worldIn.getBlockState(down).getBlock();
				final boolean isSoil = block1 instanceof BlockDirt;
				if (isSoil && (location.getY() < (256 - i - 1)))
				{
					block1.onPlantGrow(worldIn, down, location);
					int i2;

					for (i2 = (location.getY() - 3) + i; i2 <= (location.getY() + i); ++i2)
					{
						k = i2 - (location.getY() + i);
						l = 1 - (k / 2);

						for (int i1 = location.getX() - l; i1 <= (location.getX() + l); ++i1)
						{
							final int j1 = i1 - location.getX();

							for (int k1 = location.getZ() - l; k1 <= (location.getZ() + l); ++k1)
							{
								final int l1 = k1 - location.getZ();

								if ((Math.abs(j1) != l) || (Math.abs(l1) != l) || ((random.nextInt(2) != 0) && (k != 0)))
								{
									final BlockPos blockpos1 = new BlockPos(i1, i2, k1);
									final Block block = worldIn.getBlockState(blockpos1).getBlock();

									if (block.isAir(worldIn, blockpos1) || block.isLeaves(worldIn, blockpos1))
									{
										this.setBlockAndNotifyAdequately(worldIn, blockpos1, this.leavesMeta);
									}
								}
							}
						}
					}

					for (i2 = 0; i2 < i; ++i2)
					{
						final BlockPos upN = location.offset(EnumFacing.UP, i2);
						final Block block2 = worldIn.getBlockState(upN).getBlock();

						if (block2.isAir(worldIn, upN) || block2.isLeaves(worldIn, upN))
						{
							this.setBlockAndNotifyAdequately(worldIn, location.offset(EnumFacing.UP, i2), this.woodMeta);
						}
					}

					return true;
				}
				else
				{
					return false;
				}
			}
		}
		else
		{
			return false;
		}
	}
}
