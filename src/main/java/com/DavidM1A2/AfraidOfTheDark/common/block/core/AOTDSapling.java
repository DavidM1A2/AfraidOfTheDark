/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.core;

import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.reference.Reference;

import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class AOTDSapling extends BlockBush implements IGrowable
{
	public static final PropertyInteger STAGE_PROP = PropertyInteger.create("stage", 0, 1);
	protected static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

	public AOTDSapling()
	{
		super();
		this.setHardness(0.0F);
		this.setSoundType(SoundType.PLANT);
		this.setDefaultState(this.blockState.getBaseState().withProperty(STAGE_PROP, Integer.valueOf(0)));
		this.setCreativeTab(Reference.AFRAID_OF_THE_DARK);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, net.minecraft.util.math.BlockPos pos)
	{
		return SAPLING_AABB;
	}

	@Override
	public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random)
	{
		if (!world.isRemote)
		{
			super.updateTick(world, blockPos, iBlockState, random);

			if (world.getLightFromNeighbors(blockPos.up()) >= 9 && random.nextInt(7) == 0)
			{
				this.grow(world, blockPos, iBlockState, random);
			}
		}
	}

	public void grow(World world, BlockPos blockPos, IBlockState iBlockState, Random random)
	{
		if (((Integer) iBlockState.getValue(STAGE_PROP)).intValue() == 0)
		{
			world.setBlockState(blockPos, iBlockState.cycleProperty(STAGE_PROP), 4);
		}
		else
		{
			this.causeTreeToGrow(world, blockPos, iBlockState, random);
		}
	}

	public abstract void causeTreeToGrow(World world, BlockPos blockPos, IBlockState iBlockState, Random random);

	/**
	 * Get the damage value that this Block should drop
	 */
	@Override
	public int damageDropped(IBlockState state)
	{
		return 0;
	}

	@Override
	public boolean canUseBonemeal(World world, Random random, BlockPos blockPos, IBlockState iBlockState)
	{
		return world.rand.nextFloat() < 0.45D;
	}

	@Override
	public void grow(World world, Random random, BlockPos blockPos, IBlockState iBlockState)
	{
		this.grow(world, blockPos, iBlockState, random);
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(STAGE_PROP, Integer.valueOf((meta & 8) >> 3));
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state)
	{
		byte b0 = 0;
		int i = b0;
		i |= ((Integer) state.getValue(STAGE_PROP)).intValue() << 3;
		return i;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[]
		{ STAGE_PROP });
	}

	@Override
	public String getUnlocalizedName()
	{
		return String.format("tile.%s%s", Reference.MOD_ID.toLowerCase() + ":", this.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
		// Format for a block is: tile.modid:blockname.name
	}

	// Get the unlocalized name
	protected String getUnwrappedUnlocalizedName(final String unlocalizedName)
	{
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}
}
