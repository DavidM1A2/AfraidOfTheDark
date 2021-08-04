package com.davidm1a2.afraidofthedark.common.feature.tree

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.util.math.BlockPos
import net.minecraft.world.ISeedReader
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig
import net.minecraft.world.gen.feature.Feature
import java.util.*

class SacredMangroveTreeFeature : Feature<BaseTreeFeatureConfig>(BaseTreeFeatureConfig.CODEC) {
    init {
        setRegistryName(Constants.MOD_ID, "sacred_mangrove_tree")
    }

    override fun place(
        world: ISeedReader,
        chunkGenerator: ChunkGenerator,
        random: Random,
        blockPos: BlockPos,
        ignored: BaseTreeFeatureConfig
    ): Boolean {
        // Create a trunk, it's always 5 blocks tall
        for (yOffset in 0 until TREE_HEIGHT) {
            func_227216_a_(world, random, pos.add(0, yOffset, 0), logPositions, boundingBox, config)
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
                func_227219_b_(world, random, topLogPos.add(x, -1, z), leafPositions, boundingBox, config)
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
                func_227219_b_(world, random, topLogPos.add(x, 0, z), leafPositions, boundingBox, config)
            }
        }
        for (z in -1..1) {
            func_227219_b_(world, random, topLogPos.add(-2, 0, z), leafPositions, boundingBox, config)
            func_227219_b_(world, random, topLogPos.add(2, 0, z), leafPositions, boundingBox, config)
        }

        /*
        Create the leaves one level above the trunk
        x x x
        x x x
        x x x
         */
        for (x in -1..1) {
            for (z in -1..1) {
                func_227219_b_(world, random, topLogPos.add(x, 1, z), leafPositions, boundingBox, config)
            }
        }

        /*
        Create the leaves two levels above the trunk
          x
        x x x
          x
         */
        for (i in -1..1) {
            func_227219_b_(world, random, topLogPos.add(0, 2, i), leafPositions, boundingBox, config)
            func_227219_b_(world, random, topLogPos.add(i, 2, 0), leafPositions, boundingBox, config)
        }

        // True since the tree grew
        return true
    }

    companion object {
        // Height of the tree
        private const val TREE_HEIGHT = 6
    }
}