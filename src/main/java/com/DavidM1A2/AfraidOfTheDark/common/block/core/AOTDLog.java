package com.DavidM1A2.afraidofthedark.common.block.core;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import net.minecraft.block.BlockLog;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Base class for all AOTD log blocks
 */
public abstract class AOTDLog extends BlockLog
{
	/**
	 * Constructor just sets default state and initializes sounds
	 *
	 * @param baseName The name of the block to register
	 */
	public AOTDLog(String baseName)
	{
		super();
		this.setUnlocalizedName(Constants.MOD_ID + ":" + baseName);
		this.setRegistryName(Constants.MOD_ID + ":" + baseName);
		this.setHardness(2.0f);
		this.setSoundType(SoundType.WOOD);
		this.setCreativeTab(Constants.AOTD_CREATIVE_TAB);
		this.setDefaultState(this.getDefaultState().withProperty(BlockLog.LOG_AXIS, EnumAxis.Y));
	}

	/**
	 * Convert the given metadata into a BlockState for this Block. For a log this depends on the axis
	 *
	 * @param meta The metadata of the block representing the axis
	 * @return A block state with the given metadata integer
	 */
	@Override
	public IBlockState getStateFromMeta(final int meta)
	{
		switch (meta)
		{
			case 0:
				return this.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y);
			case 1:
				return this.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.X);
			case 2:
				return this.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Z);
			default:
				return this.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.NONE);
		}
	}

	/**
	 * Convert the BlockState into the correct metadata integer value based on block axis
	 *
	 * @param state The current block state
	 * @return An integer representing this block's state as an axis
	 */
	@Override
	public int getMetaFromState(IBlockState state)
	{
		switch (state.getValue(BlockLog.LOG_AXIS))
		{
			case X:
				return 1;
			case Y:
				return 0;
			case Z:
				return 2;
			default:
				return 3;
		}
	}

	/**
	 * Makes sure to give this block a state
	 *
	 * @return The block state container with all properties this log defines
	 */
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, BlockLog.LOG_AXIS);
	}

	/**
	 * The damage of the item dropped will always be the default item state's metadata
	 *
	 * @param state The current state is ignored
	 * @return A metadata value to be dropped representing this block
	 */
	@Override
	public int damageDropped(IBlockState state)
	{
		return this.getMetaFromState(this.getDefaultState());
	}

	/**
	 * Upon using silk touch we return the block in its Y axis orientation
	 *
	 * @param state The current block state, ignored
	 * @return The block as a new item stack oriented in the Y axis orientation
	 */
	@Override
	protected ItemStack getSilkTouchDrop(IBlockState state)
	{
		return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(this.getDefaultState()));
	}
}
