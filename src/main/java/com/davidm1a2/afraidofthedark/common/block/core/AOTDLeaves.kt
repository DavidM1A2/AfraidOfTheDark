package com.davidm1a2.afraidofthedark.common.block.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.block.BlockLeaves
import net.minecraft.block.BlockPlanks
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import org.apache.commons.lang3.BitField

/**
 * A base class for any leaves added by the AOTD mod
 *
 * @constructor just requires the name of the block to register as the registry name
 * @param baseName The name to be used by the registry and unlocalized names
 */
abstract class AOTDLeaves(baseName: String) : BlockLeaves()
{
    init
    {
        unlocalizedName = "${Constants.MOD_ID}:$baseName"
        this.setRegistryName("${Constants.MOD_ID}:$baseName")
        // Don't forget the creative tab...
        setCreativeTab(Constants.AOTD_CREATIVE_TAB)
        // Set the default state
        this.defaultState = this.defaultState.withProperty(CHECK_DECAY, true)
            .withProperty(DECAYABLE, true)
    }

    /**
     * Upon silk-touching a block, return this leaf block not nothing like normal
     *
     * @param state The block state that was mined
     * @return An itemstack of size 1 containing the leaf block
     */
    override fun getSilkTouchDrop(state: IBlockState): ItemStack
    {
        return ItemStack(Item.getItemFromBlock(this))
    }

    /**
     * Gets the block state from a given metadata value
     *
     * @param meta The metadata value to start from
     * @return The block state to convert the metadata into
     */
    override fun getStateFromMeta(meta: Int): IBlockState
    {
        return this.defaultState.withProperty(DECAYABLE, DECAYABLE_FIELD.getValue(meta) == 1)
            .withProperty(CHECK_DECAY, CHECK_DECAY_FIELD.getValue(meta) == 1)
    }

    /**
     * Opposite of getStateFromMeta, returns the block metadata value from the state
     *
     * @param state The block state in the world
     * @return The metadata value to conver the state into
     */
    override fun getMetaFromState(state: IBlockState): Int
    {
        var meta = 0
        meta = DECAYABLE_FIELD.setBoolean(meta, state.getValue(DECAYABLE))
        meta = CHECK_DECAY_FIELD.setBoolean(meta, state.getValue(CHECK_DECAY))
        return meta
    }

    /**
     * This is unused by any mod leaves
     *
     * @param meta ignored
     * @return null
     */
    override fun getWoodType(meta: Int): BlockPlanks.EnumType?
    {
        return null
    }

    /**
     * Our leaves will have a decayable property and a check decay property
     *
     * @return A container with all properties of the leaf
     */
    override fun createBlockState(): BlockStateContainer
    {
        return BlockStateContainer(this, DECAYABLE, CHECK_DECAY)
    }

    /**
     * Upon shearing this block we return it like silk touch would
     *
     * @param item    The shear item used to mine the leaves
     * @param world   The block that is being mined
     * @param pos     The location of the block being mined
     * @param fortune If the shears have fortune
     * @return A list of itemstacks to drop after this leaf block is sheared
     */
    override fun onSheared(item: ItemStack, world: IBlockAccess, pos: BlockPos, fortune: Int): List<ItemStack>
    {
        return NonNullList.withSize(1, ItemStack(this, 1, 0))
    }

    companion object
    {
        private val CHECK_DECAY_FIELD = BitField(0x1)
        private val DECAYABLE_FIELD = BitField(0x2)
    }
}