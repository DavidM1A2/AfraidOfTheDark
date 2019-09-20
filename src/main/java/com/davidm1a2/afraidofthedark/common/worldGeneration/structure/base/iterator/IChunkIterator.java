package com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.iterator;

import net.minecraft.util.math.ChunkPos;

import java.util.List;

/**
 * Used to get a list of chunks to iterate over
 */
public interface IChunkIterator
{
    /**
     * @return A list of chunks to iterate over
     */
    List<ChunkPos> getChunks();
}
