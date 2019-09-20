package com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.processor;

import net.minecraft.util.math.ChunkPos;

/**
 * Interface used by a structure to process interior chunks
 *
 * @param <T> The return value type after processing
 */
public interface IChunkProcessor<T>
{
    /**
     * Processes the X,Z chunk and returns true to continue or false to stop processing and return
     *
     * @param chunkPos The coordinate of the chunk
     * @return true to continue or false to stop processing and return
     */
    boolean processChunk(ChunkPos chunkPos);

    /**
     * @return The result of the chunk processing
     */
    T getResult();

    /**
     * @return The default result if process chunk returns false. Not used if processChunk never returns false so default return null
     */
    default T getDefaultResult()
    {
        return null;
    }
}
