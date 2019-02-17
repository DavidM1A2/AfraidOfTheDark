package com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base;

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
     * @param chunkX The X coordinate of the chunk
     * @param chunkZ The Z coordinate of the chunk
     * @return true to continue or false to stop processing and return
     */
    boolean processChunk(int chunkX, int chunkZ);

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
