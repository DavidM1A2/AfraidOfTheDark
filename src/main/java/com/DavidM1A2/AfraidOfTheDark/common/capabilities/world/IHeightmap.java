package com.DavidM1A2.afraidofthedark.common.capabilities.world;

import net.minecraft.util.math.ChunkPos;

/**
 * Utility interface to be implemented by any heightmaps
 */
public interface IHeightmap
{
    /**
     * Tests if we know the height of a given chunk position and quadrant
     *
     * @param chunkPos The chunk to test
     * @return True if we know the height of this position, false otherwise
     */
    boolean heightKnown(ChunkPos chunkPos);

    /**
     * Sets the height of a given chunk
     *
     * @param chunkPos The position of the chunk
     * @param low      The lowest height of that chunk
     * @param high     The highest height of that chunk
     */
    void setHeight(ChunkPos chunkPos, int low, int high);

    /**
     * Gets the lowest height of a given chunk
     *
     * @param chunkPos The position of the chunk
     * @return The low height of that position
     */
    int getLowestHeight(ChunkPos chunkPos);

    /**
     * Gets the highest height of a given chunk
     *
     * @param chunkPos The position of the chunk
     * @return The high height of that position
     */
    int getHighestHeight(ChunkPos chunkPos);
}
