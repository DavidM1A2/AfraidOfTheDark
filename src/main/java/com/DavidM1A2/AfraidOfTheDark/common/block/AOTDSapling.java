package com.DavidM1A2.AfraidOfTheDark.common.block;

import java.util.Random;

import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.AOTDTreeTypes;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Refrence;

public abstract class AOTDSapling extends BlockBush implements IGrowable
{
	public static final PropertyEnum TYPE_PROP = PropertyEnum.create("type", AOTDTreeTypes.class);
	public static final PropertyInteger STAGE_PROP = PropertyInteger.create("stage", 0, 1);

	public AOTDSapling()
	{
		this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE_PROP, AOTDTreeTypes.GRAVEWOOD).withProperty(STAGE_PROP, Integer.valueOf(0)));
		float f = 0.4F;
		this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
		this.setCreativeTab(Refrence.AFRAID_OF_THE_DARK);
		this.setStepSound(soundTypeGrass);
	}

	@Override
	public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random)
	{
		if (!world.isRemote)
		{
			super.updateTick(world, blockPos, iBlockState, random);

			if (world.getLightFromNeighbors(blockPos.offsetUp()) >= 9 && random.nextInt(7) == 0)
			{
				this.updateBlock(world, blockPos, iBlockState, random);
			}
		}
	}

	public void updateBlock(World world, BlockPos blockPos, IBlockState iBlockState, Random random)
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
		return ((AOTDTreeTypes) state.getValue(TYPE_PROP)).getMetadata();
	}

	@Override
	public boolean isStillGrowing(World worldIn, BlockPos blockPos, IBlockState iBlockState, boolean booleanPar)
	{
		return true;
	}

	@Override
	public boolean canUseBonemeal(World world, Random random, BlockPos blockPos, IBlockState iBlockState)
	{
		return (double) world.rand.nextFloat() < 0.45D;
	}

	@Override
	public void grow(World world, Random random, BlockPos blockPos, IBlockState iBlockState)
	{
		this.updateBlock(world, blockPos, iBlockState, random);
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(TYPE_PROP, AOTDTreeTypes.getTypeFromMeta(meta & 7)).withProperty(STAGE_PROP, Integer.valueOf((meta & 8) >> 3));
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state)
	{
		byte b0 = 0;
		int i = b0 | ((AOTDTreeTypes) state.getValue(TYPE_PROP)).getMetadata();
		i |= ((Integer) state.getValue(STAGE_PROP)).intValue() << 3;
		return i;
	}

	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[]
		{ TYPE_PROP, STAGE_PROP });
	}

	@Override
	public String getUnlocalizedName()
	{
		return String.format("tile.%s%s", Refrence.MOD_ID.toLowerCase() + ":", this.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
		// Format for a block is: tile.modid:blockname.name
	}

	// Get the unlocalized name
	protected String getUnwrappedUnlocalizedName(final String unlocalizedName)
	{
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}
}
