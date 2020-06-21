package com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.processor

import com.davidm1a2.afraidofthedark.common.capabilities.world.IHeightmap
import net.minecraft.util.math.ChunkPos
import kotlin.math.round

/**
 * Utility processor for finding the average ground height within a region
 *
 * @constructor initializes the heightmap field
 * @param heightmap The heightmap to use
 * @property heightSum The average ground height
 */
class AverageHeightChunkProcessor(private val heightmap: IHeightmap) : IChunkProcessor<Int> {
    private var heightSum = 0.0
    private var datapoints = 0.0

    /**
     * Processes the X,Z chunk by finding the lowest point
     *
     * @param chunkPos The coordinate of the chunk
     * @return true to continue processing
     */
    override fun processChunk(chunkPos: ChunkPos): Boolean {
        // Compute the ground height in the chunk
        heightSum = heightSum + (heightmap.getLowestHeight(chunkPos) + heightmap.getHighestHeight(chunkPos)) / 2
        datapoints++
        return true
    }

    /**
     * @return The minimum ground height
     */
    override fun getResult(): Int {
        return round(heightSum / datapoints).toInt()
    }
}