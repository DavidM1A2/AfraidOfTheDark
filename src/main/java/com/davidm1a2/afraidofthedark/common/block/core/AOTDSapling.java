package com.davidm1a2.afraidofthedark.common.block.core;

import com.davidm1a2.afraidofthedark.common.constants.Constants;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Class representing the base for a sapling created in afraid of the dark
 */
public abstract class AOTDSapling extends BlockBush implements IGrowable
{
    // What stage of growth the sapling is in
    private static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);
    // The bounding box of the sapling
    private static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

    /**
     * Constructor sets up default sapling properties
     *
     * @param baseName The name of the block to register
     */
    public AOTDSapling(String baseName)
    {
        super();
        this.setUnlocalizedName(Constants.MOD_ID + ":" + baseName);
        this.setRegistryName(Constants.MOD_ID + ":" + baseName);
        this.setHardness(0.0f);
        this.setResistance(0.0f);
        this.setSoundType(SoundType.PLANT);
        this.setLightLevel(0.0f);
        this.setLightOpacity(0);
        this.setCreativeTab(Constants.AOTD_CREATIVE_TAB);
    }

    /**
     * Saplings have a special bounding box independent of anything else, so just reutrn the constant bounding box
     *
     * @param state  The state of this sapling
     * @param source Tests if the block can access light
     * @param pos    The position of this sapling
     * @return Sapling bounding box
     */
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return SAPLING_AABB;
    }

    /**
     * Called randomly on this block every few ticks
     *
     * @param world  The world this sapling is ticking in
     * @param pos    The position of this sapling
     * @param state  The state of this sapling
     * @param random The current random object
     */
    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random)
    {
        // Server side only
        if (!world.isRemote)
        {
            // Perform any original functionality
            super.updateTick(world, pos, state, random);

            // If the area of the world is not loaded, return
            if (!world.isAreaLoaded(pos, 1))
            {
                return;
            }

            // If we have enough light and our 1 in 7 chance succeeds, advance our sapling growth
            if (world.getLightFromNeighbors(pos.up()) >= 9 && random.nextInt(7) == 0)
            {
                this.advanceGrowth(world, pos, state, random);
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
    private void advanceGrowth(World world, BlockPos pos, IBlockState state, Random random)
    {
        // If the sapling has never grown before, advance it's growth to the next stage. When advance growth is called again the sapling will grow
        if (state.getValue(STAGE) == 0)
        {
            world.setBlockState(pos, state.cycleProperty(STAGE), 4);
        }
        // If the sapling has grown before, cause the tree to grow
        else
        {
            this.causeTreeToGrow(world, pos, state, random);
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
    public abstract void causeTreeToGrow(World world, BlockPos pos, IBlockState state, Random random);

    /**
     * Test if the sapling can grow or not, will always return true
     *
     * @param world    The world the sapling is growing in
     * @param pos      The position of the sapling
     * @param state    The current state of the sapling
     * @param isClient If the function is being called client or server side
     * @return True if the sapling can grow, false otherwise
     */
    @Override
    public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient)
    {
        return true;
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
    @Override
    public boolean canUseBonemeal(World world, Random random, BlockPos pos, IBlockState state)
    {
        return world.rand.nextFloat() < 0.45f;
    }

    /**
     * Called to force this sapling to grow, just calls advance growth
     *
     * @param world  The world the sapling is growing in
     * @param random The random object to grow the tree with
     * @param pos    The position of the sapling
     * @param state  The current state of the sapling
     */
    @Override
    public void grow(World world, Random random, BlockPos pos, IBlockState state)
    {
        this.advanceGrowth(world, pos, state, random);
    }

    /**
     * Converts from metadata integer to block state
     *
     * @param meta The metadata value to convert
     * @return The blockstate to convert to
     */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(STAGE, meta);
    }

    /**
     * Converts from state object to metadata integer
     *
     * @param state The block state to convert into integer metadata
     * @return The integer metadata representing this state
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(STAGE);
    }

    /**
     * Makes sure to give this block a state
     *
     * @return The block state container with all properties this sapling defines
     */
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, STAGE);
    }
}
