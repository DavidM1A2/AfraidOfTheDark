package com.davidm1a2.afraidofthedark.common.capabilities.world.structure

import net.minecraft.world.level.ChunkPos

enum class StructureGridSize(val blockSize: Int, val nextSizeDown: StructureGridSize?) {
    SIZE_16(16, null),
    SIZE_32(32, SIZE_16),
    SIZE_64(64, SIZE_32),
    SIZE_128(128, SIZE_64),
    SIZE_256(256, SIZE_128);

    val chunkSize = blockSize / 16

    fun toAbsoluteGridPos(chunkPos: ChunkPos): StructureGridPos {
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

        return StructureGridPos(xChunkPos / chunkSize, zChunkPos / chunkSize, this)
    }

    fun toRelativeGridPos(chunkPos: ChunkPos): StructureGridPos {
        val absoluteGridPos = toAbsoluteGridPos(chunkPos)
        val maxChunkSize = LARGEST_GRID_SIZE.chunkSize / chunkSize
        return StructureGridPos(Math.floorMod(absoluteGridPos.x, maxChunkSize), Math.floorMod(absoluteGridPos.z, maxChunkSize), this)
    }

    companion object {
        val LARGEST_GRID_SIZE = values().maxByOrNull { it.blockSize }!!
    }
}