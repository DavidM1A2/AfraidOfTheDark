package com.davidm1a2.afraidofthedark.common.capabilities.world.structure

import com.davidm1a2.afraidofthedark.common.capabilities.chunk.StructureGridSize
import net.minecraft.util.math.ChunkPos

class WorldMasterChunkMap : IWorldMasterChunkMap {
    private val masterChunkMap = mutableMapOf<ChunkPos, ChunkPos>()

    override fun getMasterChunkFor(chunkPos: ChunkPos): ChunkPos {
        val gridPos = StructureGridSize.SIZE_256.toGridPos(chunkPos)
        val masterChunkPos = masterChunkMap[gridPos]
        return if (masterChunkPos != null) {
            masterChunkPos
        } else {
            masterChunkMap[gridPos] = chunkPos
            chunkPos
        }
    }

    override fun setMasterChunks(masterChunks: List<ChunkPos>) {
        masterChunkMap.clear()
        masterChunks.forEach {
            masterChunkMap[StructureGridSize.SIZE_256.toGridPos(it)] = it
        }
    }

    override fun getMasterChunks(): List<ChunkPos> {
        return masterChunkMap.values.toList()
    }
}