package com.davidm1a2.afraidofthedark.common.capabilities.world.structure

import net.minecraft.util.math.ChunkPos

data class StructureGridPos(val x: Int, val z: Int, val size: StructureGridSize) {
    fun getStartCornerChunk(): ChunkPos {
        return ChunkPos(x * size.chunkSize, z * size.chunkSize)
    }
}