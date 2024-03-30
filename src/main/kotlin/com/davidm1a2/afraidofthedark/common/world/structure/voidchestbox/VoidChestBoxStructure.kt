package com.davidm1a2.afraidofthedark.common.world.structure.voidchestbox

import com.davidm1a2.afraidofthedark.common.constants.ModBiomes
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.LevelHeightAccessor
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.BiomeSource
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.WorldgenRandom
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature
import net.minecraft.world.level.levelgen.feature.StructureFeature
import net.minecraft.world.level.levelgen.feature.StructureFeature.StructureStartFactory
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration
import java.util.*

class VoidChestBoxStructure : AOTDStructure<NoneFeatureConfiguration>("void_chest_box", NoneFeatureConfiguration.CODEC) {
    override fun getWidth(): Int {
        return 48
    }

    override fun getLength(): Int {
        return 48
    }

    override fun getStartFactory(): StructureStartFactory<NoneFeatureConfiguration> {
        return StructureStartFactory { structure, chunkPos, reference, seed ->
            VoidChestBoxStructureStart(structure, chunkPos, reference, seed)
        }
    }

    override fun configured(biome: ResourceKey<Biome>, category: Biome.BiomeCategory): ConfiguredStructureFeature<NoneFeatureConfiguration, out StructureFeature<NoneFeatureConfiguration>>? {
        return if (biome == ModBiomes.VOID_CHEST) {
            configured(NoneFeatureConfiguration.NONE)
        } else {
            null
        }
    }

    override fun configuredFlat(): ConfiguredStructureFeature<NoneFeatureConfiguration, out StructureFeature<NoneFeatureConfiguration>> {
        return configured(NoneFeatureConfiguration.NONE)
    }

    override fun canFitAt(chunkGen: ChunkGenerator, biomeProvider: BiomeSource, random: Random, xPos: Int, zPos: Int, levelHeightAccessor: LevelHeightAccessor): Boolean {
        // no-op, this is only for structures generated via our AOTD quadtree structure mapper
        return false
    }

    override fun isFeatureChunk(
        chunkGenerator: ChunkGenerator,
        biomeProvider: BiomeSource,
        seed: Long,
        random: WorldgenRandom,
        centerChunkPos: ChunkPos,
        biome: Biome,
        featureChunkPos: ChunkPos,
        config: NoneFeatureConfiguration,
        levelHeightAccessor: LevelHeightAccessor
    ): Boolean {
        val xStart = centerChunkPos.x * 16
        val xEnd = xStart + 15
        val amountOverMultipleOf1000 = xEnd % 1000

        return centerChunkPos.z * 16 == 0 && xStart >= 0 && amountOverMultipleOf1000 < 16
    }
}