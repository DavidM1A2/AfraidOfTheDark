package com.davidm1a2.afraidofthedark.common.block.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.block.BlockLog
import net.minecraft.block.SoundType
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

/**
 * Base class for all AOTD log blocks
 *
 * @constructor just sets default state and initializes sounds
 * @param baseName The name of the block to register
 */
abstract class AOTDBlockLog(baseName: String) : BlockLog()
{
    init
    {
        unlocalizedName = "${Constants.MOD_ID}:$baseName"
        this.setRegistryName("${Constants.MOD_ID}:$baseName")
        setHardness(2.0f)
        this.soundType = SoundType.WOOD
        setCreativeTab(Constants.AOTD_CREATIVE_TAB)
        this.defaultState = this.defaultState.withProperty(LOG_AXIS, EnumAxis.Y)
    }

    /**
     * Convert the given metadata into a BlockState for this Block. For a log this depends on the axis
     *
     * @param meta The metadata of the block representing the axis
     * @return A block state with the given metadata integer
     */
    override fun getStateFromMeta(meta: Int): IBlockState {
        return when (meta) {
            0 -> this.defaultState.withProperty(LOG_AXIS, EnumAxis.Y)
            1 -> this.defaultState.withProperty(LOG_AXIS, EnumAxis.X)
            2 -> this.defaultState.withProperty(LOG_AXIS, EnumAxis.Z)
            else -> this.defaultState.withProperty(LOG_AXIS, EnumAxis.NONE)
        }
    }

    /**
     * Convert the BlockState into the correct metadata integer value based on block axis
     *
     * @param state The current block state
     * @return An integer representing this block's state as an axis
     */
    override fun getMetaFromState(state: IBlockState): Int {
        return when (state.getValue(LOG_AXIS)) {
            EnumAxis.X -> 1
            EnumAxis.Y -> 0
            EnumAxis.Z -> 2
            else -> 3
        }
    }

    /**
     * Makes sure to give this block a state
     *
     * @return The block state container with all properties this log defines
     */
    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, LOG_AXIS)
    }

    /**
     * The damage of the item dropped will always be the default item state's metadata
     *
     * @param state The current state is ignored
     * @return A metadata value to be dropped representing this block
     */
    override fun damageDropped(state: IBlockState): Int {
        return getMetaFromState(this.defaultState)
    }

    /**
     * Upon using silk touch we return the block in its Y axis orientation
     *
     * @param state The current block state, ignored
     * @return The block as a new item stack oriented in the Y axis orientation
     */
    override fun getSilkTouchDrop(state: IBlockState): ItemStack {
        return ItemStack(Item.getItemFromBlock(this), 1, getMetaFromState(this.defaultState))
    }
}