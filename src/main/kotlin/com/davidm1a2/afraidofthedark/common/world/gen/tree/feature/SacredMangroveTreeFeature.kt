package com.davidm1a2.afraidofthedark.common.world.gen.tree.feature

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.BlockLog
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.gen.feature.AbstractTreeFeature
import net.minecraft.world.gen.feature.NoFeatureConfig
import java.util.*

/**
 * Mangrove tree feature
 *
 * @param notify True if placing blocks should notify, false otherwise
 */
class SacredMangroveTreeFeature(notify: Boolean) : AbstractTreeFeature<NoFeatureConfig>(notify) {
    /**
     * Causes the tree to grow. Uses a custom tree generation algorithm
     *
     * @param changedBlocks A list of log blocks that were placed
     * @param world The world the tree is growing in
     * @param random The random object to grow the tree with
     * @param pos The position of the tree
     */
    override fun place(changedBlocks: MutableSet<BlockPos>, world: IWorld, random: Random, pos: BlockPos): Boolean {
        // Create a trunk, it's always 5 blocks tall
        for (yOffset in 0 until TREE_HEIGHT) {
            setBlockIfPossible(changedBlocks, world, pos.add(0, yOffset, 0), SACRED_MANGROVE_LOG_UP)
        }

        val topLogPos = pos.add(0, TREE_HEIGHT - 1, 0)

        /*
        Create the leaves around the trunk down a level
        x x x x x
        x x x x x
        x x   x x
        x x x x x
        x x x x x
         */
        for (x in -2..2) {
            for (z in -2..2) {
                setBlockIfPossible(changedBlocks, world, topLogPos.add(x, -1, z), SACRED_MANGROVE_LEAVES)
            }
        }

        /*
        Create the leaves around the trunk level without corners
          x x x
        x x x x x
        x x   x x
        x x x x x
          x x x
         */
        for (x in -1..1) {
            for (z in -2..2) {
                setBlockIfPossible(changedBlocks, world, topLogPos.add(x, 0, z), SACRED_MANGROVE_LEAVES)
            }
        }
        for (z in -1..1) {
            setBlockIfPossible(changedBlocks, world, topLogPos.add(-2, 0, z), SACRED_MANGROVE_LEAVES)
            setBlockIfPossible(changedBlocks, world, topLogPos.add(2, 0, z), SACRED_MANGROVE_LEAVES)
        }

        /*
        Create the leaves one level above the trunk
        x x x
        x x x
        x x x
         */
        for (x in -1..1) {
            for (z in -1..1) {
                setBlockIfPossible(changedBlocks, world, topLogPos.add(x, 1, z), SACRED_MANGROVE_LEAVES)
            }
        }

        /*
        Create the leaves two levels above the trunk
          x
        x x x
          x
         */
        for (i in -1..1) {
            setBlockIfPossible(changedBlocks, world, topLogPos.add(0, 2, i), SACRED_MANGROVE_LEAVES)
            setBlockIfPossible(changedBlocks, world, topLogPos.add(i, 2, 0), SACRED_MANGROVE_LEAVES)
        }

        // True since the tree grew
        return true
    }

    /**
     * Sets a block to a given block state if nothing is in the way
     *
     * @param changedBlocks The set of log blocks
     * @param world      The world to set the block in
     * @param location   The location to place the block
     * @param blockState THe state to place at the position
     */
    private fun setBlockIfPossible(changedBlocks: MutableSet<BlockPos>, world: IWorld, location: BlockPos, blockState: IBlockState) {
        // Grab the current block at the position
        val current = world.getBlockState(location)
        // Test if we can overwrite the block
        if (REPLACEABLE_BLOCKS.contains(current.block)) {
            func_208520_a(changedBlocks, world, location, blockState)
        }
    }

    companion object {
        // Reference to the sacred mangrove log pointing upwards
        private val SACRED_MANGROVE_LOG_UP = ModBlocks.SACRED_MANGROVE.defaultState.with(BlockLog.AXIS, EnumFacing.Axis.Y)

        // Reference to the sacred leaf block to place
        private val SACRED_MANGROVE_LEAVES = ModBlocks.SACRED_MANGROVE_LEAVES.defaultState

        // Height of the tree
        private const val TREE_HEIGHT = 6

        // A set of blocks that can be replaced by wood upon growing
        private val REPLACEABLE_BLOCKS = setOf(
            Blocks.TALL_GRASS,
            Blocks.AIR,
            Blocks.ACACIA_LEAVES,
            Blocks.BIRCH_LEAVES,
            Blocks.DARK_OAK_LEAVES,
            Blocks.JUNGLE_LEAVES,
            Blocks.OAK_LEAVES,
            Blocks.SPRUCE_LEAVES,
            ModBlocks.MANGROVE_LEAVES,
            ModBlocks.GRAVEWOOD_LEAVES,
            Blocks.WATER,
            Blocks.GRASS
        )
    }
}