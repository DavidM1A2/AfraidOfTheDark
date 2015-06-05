/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.biomes;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.AOTDTreeTypes;

public class WorldGenGravewoodTrees extends WorldGenAbstractTree
{
	private boolean field_150531_a;

	public WorldGenGravewoodTrees(final boolean p_i2027_1_)
	{
		super(p_i2027_1_);
	}

	// Slightly modified tree generator based on default MC tree used to get
	// Gravewood Leaves and Wood
	@Override
	public boolean generate(final World worldIn, final Random p_180709_2_, final BlockPos p_180709_3_)
	{
		int i = p_180709_2_.nextInt(3) + 5;

		if (this.field_150531_a)
		{
			i += p_180709_2_.nextInt(7);
		}

		boolean flag = true;

		if ((p_180709_3_.getY() >= 1) && ((p_180709_3_.getY() + i + 1) <= 256))
		{
			int k;
			int l;

			for (int j = p_180709_3_.getY(); j <= (p_180709_3_.getY() + 1 + i); ++j)
			{
				byte b0 = 1;

				if (j == p_180709_3_.getY())
				{
					b0 = 0;
				}

				if (j >= ((p_180709_3_.getY() + 1 + i) - 2))
				{
					b0 = 2;
				}

				for (k = p_180709_3_.getX() - b0; (k <= (p_180709_3_.getX() + b0)) && flag; ++k)
				{
					for (l = p_180709_3_.getZ() - b0; (l <= (p_180709_3_.getZ() + b0)) && flag; ++l)
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
				final BlockPos down = p_180709_3_.offsetDown();
				final Block block1 = worldIn.getBlockState(down).getBlock();
				final boolean isSoil = block1 instanceof BlockDirt;
				if (isSoil && (p_180709_3_.getY() < (256 - i - 1)))
				{
					block1.onPlantGrow(worldIn, down, p_180709_3_);
					int i2;

					for (i2 = (p_180709_3_.getY() - 3) + i; i2 <= (p_180709_3_.getY() + i); ++i2)
					{
						k = i2 - (p_180709_3_.getY() + i);
						l = 1 - (k / 2);

						for (int i1 = p_180709_3_.getX() - l; i1 <= (p_180709_3_.getX() + l); ++i1)
						{
							final int j1 = i1 - p_180709_3_.getX();

							for (int k1 = p_180709_3_.getZ() - l; k1 <= (p_180709_3_.getZ() + l); ++k1)
							{
								final int l1 = k1 - p_180709_3_.getZ();

								if ((Math.abs(j1) != l) || (Math.abs(l1) != l) || ((p_180709_2_.nextInt(2) != 0) && (k != 0)))
								{
									final BlockPos blockpos1 = new BlockPos(i1, i2, k1);
									final Block block = worldIn.getBlockState(blockpos1).getBlock();

									if (block.isAir(worldIn, blockpos1) || block.isLeaves(worldIn, blockpos1))
									{
										this.func_175905_a(worldIn, blockpos1, ModBlocks.gravewoodLeaves, AOTDTreeTypes.GRAVEWOOD.getMetadata());
									}
								}
							}
						}
					}

					for (i2 = 0; i2 < i; ++i2)
					{
						final BlockPos upN = p_180709_3_.offsetUp(i2);
						final Block block2 = worldIn.getBlockState(upN).getBlock();

						if (block2.isAir(worldIn, upN) || block2.isLeaves(worldIn, upN))
						{
							this.func_175905_a(worldIn, p_180709_3_.offsetUp(i2), ModBlocks.gravewood, AOTDTreeTypes.GRAVEWOOD.getMetadata());
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
