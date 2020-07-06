package com.davidm1a2.afraidofthedark.common.world.structure.old.base.iterator

import net.minecraft.util.math.ChunkPos

/**
 * Used to get a list of chunks to iterate over
 */
interface IChunkIterator {
    /**
     * @return A list of chunks to iterate over
     */
    fun getChunks(): List<ChunkPos>
}