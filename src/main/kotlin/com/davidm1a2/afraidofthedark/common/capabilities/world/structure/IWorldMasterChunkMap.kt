package com.davidm1a2.afraidofthedark.common.capabilities.world.structure

import net.minecraft.util.math.ChunkPos

interface IWorldMasterChunkMap {
    fun getMasterChunkFor(chunkPos: ChunkPos): ChunkPos

    fun setMasterChunks(masterChunks: List<ChunkPos>)
    fun getMasterChunks(): List<ChunkPos>
}