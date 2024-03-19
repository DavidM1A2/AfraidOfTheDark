package com.davidm1a2.afraidofthedark.common.dimension

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.biome.BiomeSource
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.StructureSettings
import java.util.function.BiFunction

class NightmareChunkGenerator(biomeProvider: BiomeSource, settings: StructureSettings) : EmptyChunkGenerator(biomeProvider, settings) {
    override fun codec(): Codec<out ChunkGenerator> {
        return CODEC
    }

    companion object {
        val CODEC: Codec<NightmareChunkGenerator> = RecordCodecBuilder.create {
            it.group(
                BiomeSource.CODEC.fieldOf("biome_source").forGetter(ChunkGenerator::getBiomeSource),
                StructureSettings.CODEC.fieldOf("settings").forGetter(ChunkGenerator::getSettings)
            ).apply(it, it.stable(BiFunction { biomeProvider, settings -> NightmareChunkGenerator(biomeProvider, settings) }))
        }
    }
}