package com.davidm1a2.afraidofthedark.common.capabilities.chunk

import net.minecraft.util.math.ChunkPos

enum class StructureGridSize(val size: Int) {
    SIZE_256(256),
    SIZE_128(128),
    SIZE_64(64),
    SIZE_32(32),
    SIZE_16(16);

    fun toGridPos(chunkPos: ChunkPos): ChunkPos {
        val gridChunkSize = this.size / 16
        val xChunkPos = if (chunkPos.x < 0) {
            chunkPos.x - gridChunkSize + 1
        } else {
            chunkPos.x
        }

        val zChunkPos = if (chunkPos.z < 0) {
            chunkPos.z - gridChunkSize + 1
        } else {
            chunkPos.z
        }

        return ChunkPos(xChunkPos / gridChunkSize, zChunkPos / gridChunkSize)
    }
}