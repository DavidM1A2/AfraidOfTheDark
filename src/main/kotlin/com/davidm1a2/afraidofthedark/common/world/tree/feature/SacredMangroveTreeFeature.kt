package com.davidm1a2.afraidofthedark.common.world.tree.feature

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.LogBlock
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.gen.IWorldGenerationReader
import net.minecraft.world.gen.feature.NoFeatureConfig
import net.minecraft.world.gen.feature.TreeFeature
import java.util.*

/**
 * Mangrove tree feature
 *
 * @param notify True if placing blocks should notify, false otherwise
 */
class SacredMangroveTreeFeature(notify: Boolean) : TreeFeature({ NoFeatureConfig.NO_FEATURE_CONFIG }, notify) {
    /**
     * Causes the tree to grow. Uses a custom tree generation algorithm
     *
     * @param changedBlocks A list of log blocks that were placed
     * @param world The world the tree is growing in
     * @param random The random object to grow the tree with
     * @param pos The position of the tree
     */
    override fun place(
        changedBlocks: MutableSet<BlockPos>,
        world: IWorldGenerationReader,
        random: Random,
        pos: BlockPos,
        boundingBox: MutableBoundingBox
    ): Boolean {
        // Create a trunk, it's always 5 blocks tall
        for (yOffset in 0 until TREE_HEIGHT) {
            setLogState(changedBlocks, world, pos.add(0, yOffset, 0), SACRED_MANGROVE_LOG_UP, boundingBox)
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
                setLogState(changedBlocks, world, topLogPos.add(x, -1, z), SACRED_MANGROVE_LEAVES, boundingBox)
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
                setLogState(changedBlocks, world, topLogPos.add(x, 0, z), SACRED_MANGROVE_LEAVES, boundingBox)
            }
        }
        for (z in -1..1) {
            setLogState(changedBlocks, world, topLogPos.add(-2, 0, z), SACRED_MANGROVE_LEAVES, boundingBox)
            setLogState(changedBlocks, world, topLogPos.add(2, 0, z), SACRED_MANGROVE_LEAVES, boundingBox)
        }

        /*
        Create the leaves one level above the trunk
        x x x
        x x x
        x x x
         */
        for (x in -1..1) {
            for (z in -1..1) {
                setLogState(changedBlocks, world, topLogPos.add(x, 1, z), SACRED_MANGROVE_LEAVES, boundingBox)
            }
        }

        /*
        Create the leaves two levels above the trunk
          x
        x x x
          x
         */
        for (i in -1..1) {
            setLogState(changedBlocks, world, topLogPos.add(0, 2, i), SACRED_MANGROVE_LEAVES, boundingBox)
            setLogState(changedBlocks, world, topLogPos.add(i, 2, 0), SACRED_MANGROVE_LEAVES, boundingBox)
        }

        // True since the tree grew
        return true
    }

    companion object {
        // Reference to the sacred mangrove log pointing upwards
        private val SACRED_MANGROVE_LOG_UP = ModBlocks.SACRED_MANGROVE.defaultState.with(LogBlock.AXIS, Direction.Axis.Y)

        // Reference to the sacred leaf block to place
        private val SACRED_MANGROVE_LEAVES = ModBlocks.SACRED_MANGROVE_LEAVES.defaultState

        // Height of the tree
        private const val TREE_HEIGHT = 6
    }
}