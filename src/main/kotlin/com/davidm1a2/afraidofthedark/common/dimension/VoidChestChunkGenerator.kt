package com.davidm1a2.afraidofthedark.common.dimension

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.biome.provider.BiomeProvider
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.settings.DimensionStructuresSettings
import java.util.function.BiFunction

class VoidChestChunkGenerator(biomeProvider: BiomeProvider, settings: DimensionStructuresSettings) : EmptyChunkGenerator(biomeProvider, settings) {
    override fun codec(): Codec<out ChunkGenerator> {
        return CODEC
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