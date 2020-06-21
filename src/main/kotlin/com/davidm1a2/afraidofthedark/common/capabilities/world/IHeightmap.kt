package com.davidm1a2.afraidofthedark.common.capabilities.world

import net.minecraft.util.math.ChunkPos

/**
 * Utility interface to be implemented by any heightmaps
 */
interface IHeightmap {
    /**
     * Tests if we know the height of a given chunk position and quadrant
     *
     * @param chunkPos The chunk to test
     * @return True if we know the height of this position, false otherwise
     */
    fun heightKnown(chunkPos: ChunkPos): Boolean

    /**
     * Sets the height of a given chunk
     *
     * @param chunkPos The position of the chunk
     * @param low      The lowest height of that chunk
     * @param high     The highest height of that chunk
     */
    fun setHeight(chunkPos: ChunkPos, low: Int, high: Int)

    /**
     * Gets the lowest height of a given chunk
     *
     * @param chunkPos The position of the chunk
     * @return The low height of that position
     */
    fun getLowestHeight(chunkPos: ChunkPos): Int

    /**
     * Gets the highest height of a given chunk
     *
     * @param chunkPos The position of the chunk
     * @return The high height of that position
     */
    fun getHighestHeight(chunkPos: ChunkPos): Int
}