package com.davidm1a2.afraidofthedark.common.feature.tree

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.RotatedPillarBlock
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.ISeedReader
import net.minecraft.world.gen.ChunkGenerator
import java.util.Random

class SacredMangroveTreeFeature : AOTDTreeFeature(
    ModBlocks.SACRED_MANGROVE.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y),
    ModBlocks.SACRED_MANGROVE_LEAVES.defaultBlockState()
) {
    init {
        setRegistryName(Constants.MOD_ID, "sacred_mangrove_tree")
    }

    override fun place(
        world: ISeedReader,
        chunkGenerator: ChunkGenerator,
        random: Random,
        blockPos: BlockPos,
        logPositions: MutableSet<BlockPos>,
        leafPositions: MutableSet<BlockPos>,
        boundingBox: MutableBoundingBox
    ) {
        // Create a trunk, it's always 5 blocks tall
        for (yOffset in 0 until TREE_HEIGHT) {
            setLog(world, blockPos.offset(0, yOffset, 0), logPositions, boundingBox)
        }

        val topLogPos = blockPos.offset(0, TREE_HEIGHT - 1, 0)

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
                setLeaf(world, topLogPos.offset(x, -1, z), leafPositions, boundingBox)
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
                setLeaf(world, topLogPos.offset(x, 0, z), leafPositions, boundingBox)
            }
        }
        for (z in -1..1) {
            setLeaf(world, topLogPos.offset(-2, 0, z), leafPositions, boundingBox)
            setLeaf(world, topLogPos.offset(2, 0, z), leafPositions, boundingBox)
        }

        /*
        Create the leaves one level above the trunk
        x x x
        x x x
        x x x
         */
        for (x in -1..1) {
            for (z in -1..1) {
                setLeaf(world, topLogPos.offset(x, 1, z), leafPositions, boundingBox)
            }
        }

        /*
        Create the leaves two levels above the trunk
          x
        x x x
          x
         */
        for (i in -1..1) {
            setLeaf(world, topLogPos.offset(0, 2, i), leafPositions, boundingBox)
            setLeaf(world, topLogPos.offset(i, 2, 0), leafPositions, boundingBox)
        }
    }

    companion object {
        // Height of the tree
        private const val TREE_HEIGHT = 6
    }
}