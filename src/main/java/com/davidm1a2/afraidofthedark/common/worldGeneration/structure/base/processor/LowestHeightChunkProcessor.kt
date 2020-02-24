package com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.processor

import com.davidm1a2.afraidofthedark.common.capabilities.world.IHeightmap
import net.minecraft.util.math.ChunkPos
import kotlin.math.min

/**
 * Utility processor for finding the minimum ground height within a region
 *
 * @constructor initializes the heightmap field
 * @param heightmap The heightmap to use
 * @property minGroundHeight The minimum ground height
 */
class LowestHeightChunkProcessor(private val heightmap: IHeightmap) : IChunkProcessor<Int> {
    private var minGroundHeight = 256

    /**
     * Processes the X,Z chunk by finding the lowest point
     *
     * @param chunkPos The coordinate of the chunk
     * @return true to continue processing
     */
    override fun processChunk(chunkPos: ChunkPos): Boolean {
        // Compute the ground height in the chunk
        val groundHeight = heightmap.getLowestHeight(chunkPos)
        minGroundHeight = min(minGroundHeight, groundHeight)
        return true
    }

    /**
     * @return The minimum ground height
     */
    override fun getResult(): Int {
        return minGroundHeight
    }
}