package com.davidm1a2.afraidofthedark.common.block.sacred_mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlockSapling
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.BlockLeaves
import net.minecraft.block.BlockLog
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*

/**
 * Block representing a sacred mangrove sapling
 *
 * @constructor initializes the sapling with a name
 */
class BlockSacredMangroveSapling : AOTDBlockSapling("sacred_mangrove_sapling") {
    /**
     * Causes the tree to grow. Uses a custom tree generation algorithm
     *
     * @param world  The world the sapling is growing in
     * @param pos    The position of the sapling
     * @param state  The current state of the sapling
     * @param random The random object to grow the tree with
     */
    override fun causeTreeToGrow(world: World, pos: BlockPos, state: IBlockState, random: Random) {
        // Clear the sapling block
        world.setBlockState(pos, Blocks.AIR.defaultState)

        // Create a trunk, it's always 5 blocks tall
        for (yOffset in 0 until TREE_HEIGHT) {
            tryPlaceBlock(world, pos.add(0, yOffset, 0), SACRED_MANGROVE_LOG_UP)
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
                tryPlaceBlock(world, topLogPos.add(x, -1, z), SACRED_MANGROVE_LEAVES)
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
                tryPlaceBlock(world, topLogPos.add(x, 0, z), SACRED_MANGROVE_LEAVES)
            }
        }
        for (z in -1..1) {
            tryPlaceBlock(world, topLogPos.add(-2, 0, z), SACRED_MANGROVE_LEAVES)
            tryPlaceBlock(world, topLogPos.add(2, 0, z), SACRED_MANGROVE_LEAVES)
        }

        /*
        Create the leaves one level above the trunk
        x x x
        x x x
        x x x
         */
        for (x in -1..1) {
            for (z in -1..1) {
                tryPlaceBlock(world, topLogPos.add(x, 1, z), SACRED_MANGROVE_LEAVES)
            }
        }

        /*
        Create the leaves two levels above the trunk
          x
        x x x
          x
         */
        for (i in -1..1) {
            tryPlaceBlock(world, topLogPos.add(0, 2, i), SACRED_MANGROVE_LEAVES)
            tryPlaceBlock(world, topLogPos.add(i, 2, 0), SACRED_MANGROVE_LEAVES)
        }
    }

    /**
     * Try and place the block at the position if it's replaceable
     *
     * @param world The world to place the block in
     * @param pos The position to place the block at
     * @param state The block to place
     */
    private fun tryPlaceBlock(world: World, pos: BlockPos, state: IBlockState) {
        if (REPLACEABLE_BLOCKS.contains(world.getBlockState(pos).block)) {
            world.setBlockState(pos, state)
        }
    }

    companion object {
        // Reference to the sacred mangrove log pointing upwards
        private val SACRED_MANGROVE_LOG_UP =
            ModBlocks.SACRED_MANGROVE.defaultState.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y)
        // Reference to the sacred leaf block to place
        private val SACRED_MANGROVE_LEAVES = ModBlocks.SACRED_MANGROVE_LEAVES.defaultState
            .withProperty(BlockLeaves.DECAYABLE, true)
            .withProperty(BlockLeaves.CHECK_DECAY, false)

        // Height of the tree
        private const val TREE_HEIGHT = 6

        // A set of blocks that can be replaced by wood upon growing
        private val REPLACEABLE_BLOCKS = setOf(
            Blocks.TALLGRASS,
            Blocks.AIR,
            Blocks.LEAVES,
            Blocks.LEAVES2,
            Blocks.WATER,
            Blocks.FLOWING_WATER,
            Blocks.GRASS,
            ModBlocks.SACRED_MANGROVE_LEAVES
        )
    }
}