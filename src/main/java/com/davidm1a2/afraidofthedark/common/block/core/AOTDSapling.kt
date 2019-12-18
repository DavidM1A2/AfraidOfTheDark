package com.davidm1a2.afraidofthedark.common.block.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.block.BlockBush
import net.minecraft.block.IGrowable
import net.minecraft.block.SoundType
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import java.util.*

/**
 * Class representing the base for a sapling created in afraid of the dark
 *
 * @constructor sets up default sapling properties
 * @param baseName The name of the block to register
 */
abstract class AOTDSapling(baseName: String) : BlockBush(), IGrowable
{
    init
    {
        unlocalizedName = "${Constants.MOD_ID}:$baseName"
        this.setRegistryName("${Constants.MOD_ID}:$baseName")
        setHardness(0.0f)
        setResistance(0.0f)
        this.soundType = SoundType.PLANT
        setLightLevel(0.0f)
        setLightOpacity(0)
        setCreativeTab(Constants.AOTD_CREATIVE_TAB)
    }

    /**
     * Saplings have a special bounding box independent of anything else, so just reutrn the constant bounding box
     *
     * @param state  The state of this sapling
     * @param source Tests if the block can access light
     * @param pos    The position of this sapling
     * @return Sapling bounding box
     */
    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB
    {
        return SAPLING_AABB
    }

    /**
     * Called randomly on this block every few ticks
     *
     * @param world  The world this sapling is ticking in
     * @param pos    The position of this sapling
     * @param state  The state of this sapling
     * @param random The current random object
     */
    override fun updateTick(world: World, pos: BlockPos, state: IBlockState, random: Random)
    {
        // Server side only
        if (!world.isRemote)
        {
            // Perform any original functionality
            super.updateTick(world, pos, state, random)

            // If the area of the world is not loaded, return
            if (!world.isAreaLoaded(pos, 1))
            {
                return
            }

            // If we have enough light and our 1 in 7 chance succeeds, advance our sapling growth
            if (world.getLightFromNeighbors(pos.up()) >= 9 && random.nextInt(7) == 0)
            {
                advanceGrowth(world, pos, state, random)
            }
        }
    }

    /**
     * Called to advance the growth of our tree. If it has grown enough, create a tree
     *
     * @param world  The world the tree is growing in
     * @param pos    The position of the sapling
     * @param state  The current state of the sapling
     * @param random The random object associated with this sapling
     */
    private fun advanceGrowth(world: World, pos: BlockPos, state: IBlockState, random: Random)
    {
        // If the sapling has never grown before, advance it's growth to the next stage. When advance growth is called again the sapling will grow
        if (state.getValue(STAGE) == 0)
        {
            world.setBlockState(pos, state.cycleProperty(STAGE), 4)
        }
        else
        {
            causeTreeToGrow(world, pos, state, random)
        }
    }

    /**
     * Called to make the tree grow. The algorithm is tree type dependent!
     *
     * @param world  The world the sapling is growing in
     * @param pos    The position of the sapling
     * @param state  The current state of the sapling
     * @param random The random object to grow the tree with
     */
    abstract fun causeTreeToGrow(world: World, pos: BlockPos, state: IBlockState, random: Random)

    /**
     * Test if the sapling can grow or not, will always return true
     *
     * @param world    The world the sapling is growing in
     * @param pos      The position of the sapling
     * @param state    The current state of the sapling
     * @param isClient If the function is being called client or server side
     * @return True if the sapling can grow, false otherwise
     */
    override fun canGrow(world: World, pos: BlockPos, state: IBlockState, isClient: Boolean): Boolean
    {
        return true
    }

    /**
     * Tests if the sapling is bone-meal able. True 55% of the time randomly
     *
     * @param world  The world the sapling is growing in
     * @param random The random object to grow the tree with
     * @param pos    The position of the sapling
     * @param state  The current state of the sapling
     * @return True if the bonemeal was successful, false otherwise
     */
    override fun canUseBonemeal(world: World, random: Random, pos: BlockPos, state: IBlockState): Boolean
    {
        return world.rand.nextFloat() < 0.45f
    }

    /**
     * Called to force this sapling to grow, just calls advance growth
     *
     * @param world  The world the sapling is growing in
     * @param random The random object to grow the tree with
     * @param pos    The position of the sapling
     * @param state  The current state of the sapling
     */
    override fun grow(world: World, random: Random, pos: BlockPos, state: IBlockState)
    {
        advanceGrowth(world, pos, state, random)
    }

    /**
     * Converts from metadata integer to block state
     *
     * @param meta The metadata value to convert
     * @return The blockstate to convert to
     */
    override fun getStateFromMeta(meta: Int): IBlockState
    {
        return this.defaultState.withProperty(STAGE, meta)
    }

    /**
     * Converts from state object to metadata integer
     *
     * @param state The block state to convert into integer metadata
     * @return The integer metadata representing this state
     */
    override fun getMetaFromState(state: IBlockState): Int
    {
        return state.getValue(STAGE)
    }

    /**
     * Makes sure to give this block a state
     *
     * @return The block state container with all properties this sapling defines
     */
    override fun createBlockState(): BlockStateContainer
    {
        return BlockStateContainer(this, STAGE)
    }

    companion object
    {
        // What stage of growth the sapling is in
        private val STAGE = PropertyInteger.create("stage", 0, 1)
        // The bounding box of the sapling
        private val SAPLING_AABB = AxisAlignedBB(0.09999999403953552, 0.0, 0.09999999403953552, 0.8999999761581421, 0.800000011920929, 0.8999999761581421)
    }
}