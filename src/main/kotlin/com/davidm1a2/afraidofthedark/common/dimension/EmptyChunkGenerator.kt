package com.davidm1a2.afraidofthedark.common.dimension

import net.minecraft.server.level.WorldGenRegion
import net.minecraft.world.level.LevelHeightAccessor
import net.minecraft.world.level.NoiseColumn
import net.minecraft.world.level.StructureFeatureManager
import net.minecraft.world.level.biome.BiomeSource
import net.minecraft.world.level.chunk.ChunkAccess
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.StructureSettings
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

abstract class EmptyChunkGenerator(biomeProvider: BiomeSource, settings: StructureSettings) : ChunkGenerator(biomeProvider, settings) {
    override fun withSeed(seed: Long): ChunkGenerator {
        return this
    }

    override fun buildSurfaceAndBedrock(worldGenRegion: WorldGenRegion, chunk: ChunkAccess) {
    }

    override fun fillFromNoise(executor: Executor, structureFeatureManager: StructureFeatureManager, chunk: ChunkAccess): CompletableFuture<ChunkAccess> {
        return CompletableFuture.completedFuture(chunk)
    }

    override fun getBaseHeight(x: Int, z: Int, type: Heightmap.Types, heightAccessor: LevelHeightAccessor): Int {
        return 0
    }

    override fun getBaseColumn(x: Int, z: Int, heightAccessor: LevelHeightAccessor): NoiseColumn {
        return NoiseColumn(0, emptyArray())
    }
}