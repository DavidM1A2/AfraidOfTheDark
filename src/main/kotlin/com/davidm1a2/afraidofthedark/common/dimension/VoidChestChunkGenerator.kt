package com.davidm1a2.afraidofthedark.common.dimension

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.Blockreader
import net.minecraft.world.IBlockReader
import net.minecraft.world.IWorld
import net.minecraft.world.biome.provider.BiomeProvider
import net.minecraft.world.chunk.IChunk
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.Heightmap
import net.minecraft.world.gen.WorldGenRegion
import net.minecraft.world.gen.feature.structure.StructureManager
import net.minecraft.world.gen.settings.DimensionStructuresSettings
import java.util.function.BiFunction

class VoidChestChunkGenerator(biomeProvider: BiomeProvider, settings: DimensionStructuresSettings) : ChunkGenerator(biomeProvider, settings) {
    override fun codec(): Codec<out ChunkGenerator> {
        return CODEC
    }

    override fun withSeed(seed: Long): ChunkGenerator {
        return this
    }

    override fun buildSurfaceAndBedrock(worldGenRegion: WorldGenRegion, chunk: IChunk) {
    }

    override fun fillFromNoise(world: IWorld, structureManager: StructureManager, chunk: IChunk) {
    }

    override fun getBaseHeight(x: Int, z: Int, heightmap: Heightmap.Type): Int {
        return 0
    }

    override fun getBaseColumn(x: Int, z: Int): IBlockReader {
        return Blockreader(emptyArray())
    }

    companion object {
        val CODEC: Codec<VoidChestChunkGenerator> = RecordCodecBuilder.create {
            it.group(
                BiomeProvider.CODEC.fieldOf("biome_source").forGetter(ChunkGenerator::getBiomeSource),
                DimensionStructuresSettings.CODEC.fieldOf("settings").forGetter(ChunkGenerator::getSettings)
            ).apply(it, it.stable(BiFunction { biomeProvider, settings -> VoidChestChunkGenerator(biomeProvider, settings) }))
        }
    }
}