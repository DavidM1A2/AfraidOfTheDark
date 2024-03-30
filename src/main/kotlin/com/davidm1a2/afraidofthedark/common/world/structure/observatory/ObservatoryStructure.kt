package com.davidm1a2.afraidofthedark.common.world.structure.observatory

import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.world.structure.base.BooleanConfig
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.LevelHeightAccessor
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.BiomeSource
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature
import net.minecraft.world.level.levelgen.feature.StructureFeature
import net.minecraft.world.level.levelgen.feature.StructureFeature.StructureStartFactory
import java.util.*

class ObservatoryStructure : AOTDStructure<BooleanConfig>("observatory", BooleanConfig.CODEC) {
    override fun getWidth(): Int {
        return ModSchematics.OBSERVATORY.getWidth().toInt()
    }

    override fun getLength(): Int {
        return ModSchematics.OBSERVATORY.getLength().toInt()
    }

    override fun getStartFactory(): StructureStartFactory<BooleanConfig> {
        return StructureStartFactory { structure, chunkPos, reference, seed ->
            ObservatoryStructureStart(structure, chunkPos, reference, seed)
        }
    }

    override fun configured(biome: ResourceKey<Biome>, category: Biome.BiomeCategory): ConfiguredStructureFeature<BooleanConfig, out StructureFeature<BooleanConfig>>? {
        return if (category == Biome.BiomeCategory.EXTREME_HILLS) {
            configured(BooleanConfig(true))
        } else {
            null
        }
    }

    override fun configuredFlat(): ConfiguredStructureFeature<BooleanConfig, out StructureFeature<BooleanConfig>> {
        return configured(BooleanConfig(false))
    }

    override fun canFitAt(chunkGen: ChunkGenerator, biomeProvider: BiomeSource, random: Random, xPos: Int, zPos: Int, levelHeightAccessor: LevelHeightAccessor): Boolean {
        val isNotSupported = getInteriorConfigEstimate(xPos, zPos, biomeProvider, MISSING_CONFIG).any { !it.supported }
        if (isNotSupported) {
            return false
        }

        val chance = getOneInNValidChunks(50) * ModCommonConfiguration.observatoryMultiplier
        if (random.nextDouble() >= chance) {
            return false
        }

        val heights = getEdgeHeights(xPos, zPos, chunkGen, levelHeightAccessor)
        val maxHeight = heights.maxOrNull()!!
        val minHeight = heights.minOrNull()!!
        // If there's more than 3 blocks between the top and bottom block it's an invalid place for an observatory because it's not 'flat' enough
        // If the flat spot is below y 70 it's also not a good spot
        if (maxHeight - minHeight > 3 && minHeight <= 70) {
            return false
        }
        return true
    }

    companion object {
        private val MISSING_CONFIG = BooleanConfig(false)
    }
}