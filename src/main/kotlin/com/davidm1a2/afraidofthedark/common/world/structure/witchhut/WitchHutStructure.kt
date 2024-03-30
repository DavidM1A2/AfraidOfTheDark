package com.davidm1a2.afraidofthedark.common.world.structure.witchhut

import com.davidm1a2.afraidofthedark.common.constants.ModBiomes
import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.world.structure.base.MultiplierConfig
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.LevelHeightAccessor
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.BiomeSource
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature
import net.minecraft.world.level.levelgen.feature.StructureFeature
import net.minecraft.world.level.levelgen.feature.StructureFeature.StructureStartFactory
import java.util.*

class WitchHutStructure : AOTDStructure<MultiplierConfig>("witch_hut", MultiplierConfig.CODEC) {
    override fun getWidth(): Int {
        return ModSchematics.WITCH_HUT.getWidth().toInt()
    }

    override fun getLength(): Int {
        return ModSchematics.WITCH_HUT.getLength().toInt()
    }

    override fun getStartFactory(): StructureStartFactory<MultiplierConfig> {
        return StructureStartFactory { structure, chunkPos, reference, seed ->
            WitchHutStructureStart(structure, chunkPos, reference, seed)
        }
    }

    override fun configured(biome: ResourceKey<Biome>, category: Biome.BiomeCategory): ConfiguredStructureFeature<MultiplierConfig, out StructureFeature<MultiplierConfig>>? {
        return if (biome == ModBiomes.EERIE_FOREST) {
            configured(MultiplierConfig(1))
        } else {
            null
        }
    }

    override fun configuredFlat(): ConfiguredStructureFeature<MultiplierConfig, out StructureFeature<MultiplierConfig>> {
        return configured(MultiplierConfig(0))
    }

    override fun canFitAt(chunkGen: ChunkGenerator, biomeProvider: BiomeSource, random: Random, xPos: Int, zPos: Int, levelHeightAccessor: LevelHeightAccessor): Boolean {
        val biomeMultiplier = getInteriorConfigEstimate(xPos, zPos, biomeProvider, MISSING_CONFIG)
            .map { it.multiplier }
            .minOrNull() ?: 0

        val chance = getOneInNValidChunks(50) * ModCommonConfiguration.witchHutMultiplier * biomeMultiplier
        if (random.nextDouble() >= chance) {
            return false
        }

        val heights = getEdgeHeights(xPos, zPos, chunkGen, levelHeightAccessor)
        val maxHeight = heights.maxOrNull()!!
        val minHeight = heights.minOrNull()!!
        if (maxHeight - minHeight > 3) {
            return false
        }
        return true
    }

    companion object {
        private val MISSING_CONFIG = MultiplierConfig(0)
    }
}