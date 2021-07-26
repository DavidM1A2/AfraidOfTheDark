package com.davidm1a2.afraidofthedark.common.capabilities.world.structure

import net.minecraft.util.math.ChunkPos

enum class StructureGridSize(val blockSize: Int, val nextSizeDown: StructureGridSize?) {
    SIZE_16(16, null),
    SIZE_32(32, SIZE_16),
    SIZE_64(64, SIZE_32),
    SIZE_128(128, SIZE_64),
    SIZE_256(256, SIZE_128);

    val chunkSize = blockSize / 16

    fun toAbsoluteGridPos(chunkPos: ChunkPos): ChunkPos {
        val xChunkPos = if (chunkPos.x < 0) {
            chunkPos.x - chunkSize + 1
        } else {
            chunkPos.x
        }

        val zChunkPos = if (chunkPos.z < 0) {
            chunkPos.z - chunkSize + 1
        } else {
            chunkPos.z
        }

        return ChunkPos(xChunkPos / chunkSize, zChunkPos / chunkSize)
    }

    fun toRelativeGridPos(chunkPos: ChunkPos): ChunkPos {
        val absolute = toAbsoluteGridPos(chunkPos)
        val maxChunkSize = LARGEST_GRID_SIZE.chunkSize / chunkSize
        return ChunkPos(Math.floorMod(absolute.x, maxChunkSize), Math.floorMod(absolute.z, maxChunkSize))
    }

    companion object {
        val LARGEST_GRID_SIZE = values().maxByOrNull { it.blockSize }!!
    }
}