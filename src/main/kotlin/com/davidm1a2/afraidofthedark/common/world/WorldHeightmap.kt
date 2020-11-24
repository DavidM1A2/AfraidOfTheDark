package com.davidm1a2.afraidofthedark.common.world

import com.google.common.cache.CacheBuilder
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.IWorld
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.ChunkPrimer
import net.minecraft.world.chunk.IChunk
import net.minecraft.world.chunk.UpgradeData
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.Heightmap
import java.util.*
import java.util.concurrent.TimeUnit

object WorldHeightmap {
    private val cache = CacheBuilder.newBuilder()
        .expireAfterAccess(10, TimeUnit.MINUTES)
        .build<ChunkPos, Heightmap>()

    fun getHeight(x: Int, z: Int, world: IWorld, chunkGen: ChunkGenerator<*>): Int {
        return getOrLoad(ChunkPos(x shr 4, z shr 4), world, chunkGen).getHeight(x and 15, z and 15)
    }

    private fun getOrLoad(chunkPos: ChunkPos, world: IWorld, chunkGen: ChunkGenerator<*>): Heightmap {
        return cache.get(chunkPos) {
            var chunk: IChunk? = world.chunkProvider.getChunk(chunkPos.x, chunkPos.z, false)
            if (chunk == null) {
                chunk = ChunkPrimer(chunkPos, UpgradeData.EMPTY)
                chunkGen.generateBiomes(chunk)
                chunkGen.makeBase(world, chunk)
                chunkGen.generateSurface(chunk)
                Heightmap.func_222690_a(chunk, EnumSet.of(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES))
                // getHeightmap() = func_217303_b()
                chunk.func_217303_b(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES)
            } else {
                // getHeightmap() = func_217303_b()
                (chunk as Chunk).func_217303_b(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES)
            }
        }
    }
}