package com.davidm1a2.afraidofthedark.common.world.structure.base.iterator

import com.davidm1a2.afraidofthedark.common.world.structure.base.Structure
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos

/**
 * Chunk iterator that goes over all chunks that a structure occupies
 *
 * @constructor just sets structure and basepos fields
 * @property structure The structure to get interior chunks for
 * @property basePos   The base position the structure is at
 */
class InteriorChunkIterator(private val structure: Structure, private val basePos: BlockPos) : IChunkIterator {
    /**
     * @return A list of chunks to iterate over, will be all chunks a structure will occupy
     */
    override fun getChunks(): List<ChunkPos> {
        // Grab the chunk positions of the two corners of the structure
        val bottomLeftCorner = ChunkPos(basePos)
        val topRightCorner = ChunkPos(basePos.add(structure.getXWidth(), 0, structure.getZLength()))

        // Go over all chunks in the structure and add them to the list.
        val interiorChunks = mutableListOf<ChunkPos>()
        for (chunkX in bottomLeftCorner.x..topRightCorner.x) {
            for (chunkZ in bottomLeftCorner.z..topRightCorner.z) {
                interiorChunks.add(ChunkPos(chunkX, chunkZ))
            }
        }
        return interiorChunks
    }
}