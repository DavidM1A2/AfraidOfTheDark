package com.davidm1a2.afraidofthedark.common.worldGeneration

import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World

/**
 * Relights a chunk in a world, usually takes 10-400ms to execute
 *
 * @param chunkPos The chunk to relight
 */
fun World.relightChunk(chunkPos: ChunkPos) {
    val chunk = this.getChunkFromChunkCoords(chunkPos.x, chunkPos.z)
    chunk.generateSkylightMap()
    chunk.checkLight()
    chunk.markDirty()
}