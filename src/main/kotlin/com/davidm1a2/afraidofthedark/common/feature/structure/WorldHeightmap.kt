package com.davidm1a2.afraidofthedark.common.feature.structure

import com.google.common.cache.CacheBuilder
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.palette.UpgradeData
import net.minecraft.world.IWorld
import net.minecraft.world.chunk.ChunkPrimer
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.Heightmap
import net.minecraft.world.gen.WorldGenRegion
import net.minecraft.world.server.ServerWorld
import java.util.*
import java.util.concurrent.TimeUnit

object WorldHeightmap {
    private val cache = CacheBuilder.newBuilder()
        .expireAfterAccess(2, TimeUnit.MINUTES)
        .build<ChunkPos, Heightmap>()

    fun getHeight(x: Int, z: Int, world: IWorld, chunkGen: ChunkGenerator<*>): Int {
        return getOrLoad(ChunkPos(x shr 4, z shr 4), world, chunkGen).getHeight(x and 15, z and 15)
    }

    private fun getOrLoad(chunkPos: ChunkPos, world: IWorld, chunkGen: ChunkGenerator<*>): Heightmap {
        return cache.get(chunkPos) {
            val chunk = ChunkPrimer(chunkPos, UpgradeData.EMPTY)
            chunkGen.generateBiomes(chunk)
            chunkGen.makeBase(world, chunk)
            chunkGen.generateSurface(WorldGenRegion(world as ServerWorld, listOf(chunk)), chunk)
            Heightmap.updateChunkHeightmaps(chunk, EnumSet.of(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES))
            chunk.getHeightmap(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES)
        }
    }
}