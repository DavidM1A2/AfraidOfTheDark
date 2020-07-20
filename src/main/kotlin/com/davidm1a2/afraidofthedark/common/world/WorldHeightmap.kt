package com.davidm1a2.afraidofthedark.common.world

import com.google.common.cache.CacheBuilder
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.IWorld
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.ChunkPrimer
import net.minecraft.world.chunk.IChunk
import net.minecraft.world.chunk.UpgradeData
import net.minecraft.world.gen.Heightmap
import net.minecraft.world.gen.IChunkGenerator
import java.util.concurrent.TimeUnit

object WorldHeightmap {
    private val cache = CacheBuilder.newBuilder()
        .expireAfterAccess(10, TimeUnit.MINUTES)
        .build<ChunkPos, Heightmap>()

    fun getHeight(x: Int, z: Int, world: IWorld, chunkGen: IChunkGenerator<*>): Int {
        return getOrLoad(ChunkPos(x shr 4, z shr 4), world, chunkGen).getHeight(x and 4, z and 4)
    }

    private fun getOrLoad(chunkPos: ChunkPos, world: IWorld, chunkGen: IChunkGenerator<*>): Heightmap {
        return cache.get(chunkPos) {
            var chunk: IChunk? = world.chunkProvider.provideChunk(chunkPos.x, chunkPos.z, true, false)
            if (chunk == null) {
                chunk = ChunkPrimer(chunkPos, UpgradeData.EMPTY)
                chunkGen.makeBase(chunk)
                val heightmap = chunk.getHeightmap(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES)
                if (heightmap == null) {
                    chunk.createHeightMap(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES)
                    chunk.getHeightmap(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES)!!
                } else {
                    heightmap
                }
            } else {
                (chunk as Chunk).getHeightmap(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES)
            }
        }
    }
}