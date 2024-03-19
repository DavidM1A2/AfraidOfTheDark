package com.davidm1a2.afraidofthedark.common.world.structure.crypt

import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.world.structure.base.MultiplierConfig
import net.minecraft.core.MappedRegistry
import net.minecraft.world.level.LevelHeightAccessor
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.BiomeSource
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature
import net.minecraft.world.level.levelgen.feature.StructureFeature
import net.minecraft.world.level.levelgen.feature.StructureFeature.StructureStartFactory
import java.util.Random

class CryptStructure : AOTDStructure<MultiplierConfig>("crypt", MultiplierConfig.CODEC) {
    override fun getWidth(): Int {
        return ModSchematics.CRYPT.getWidth().toInt()
    }

    override fun getLength(): Int {
        return ModSchematics.CRYPT.getLength().toInt()
    }

    override fun getStartFactory(): StructureStartFactory<MultiplierConfig> {
        return StructureStartFactory { structure, chunkPos, reference, seed ->
            CryptStructureStart(structure, chunkPos, reference, seed)
        }
    }

    override fun configured(biome: MappedRegistry.RegistryEntry<Biome>, category: Biome.BiomeCategory): ConfiguredStructureFeature<MultiplierConfig, out StructureFeature<MultiplierConfig>>? {
        return if (category !in INCOMPATIBLE_BIOME_CATEGORIES) {
            configured(MultiplierConfig(1))
        } else {
            null
        }
    }

    override fun configuredFlat(): ConfiguredStructureFeature<MultiplierConfig, out StructureFeature<MultiplierConfig>> {
        return configured(MISSING_CONFIG)
    }

    override fun canFitAt(chunkGen: ChunkGenerator, biomeProvider: BiomeSource, random: Random, xPos: Int, zPos: Int, levelHeightAccessor: LevelHeightAccessor): Boolean {
        val biomeMultiplier = getInteriorConfigEstimate(xPos, zPos, biomeProvider, MISSING_CONFIG)
            .map { it.multiplier }
            .minOrNull() ?: 0

        val chance = getOneInNValidChunks(350) * ModCommonConfiguration.cryptMultiplier * biomeMultiplier
        if (random.nextDouble() >= chance) {
            return false
        }

        val heights = getEdgeHeights(xPos, zPos, chunkGen, levelHeightAccessor)
        val maxHeight = heights.maxOrNull()!!
        val minHeight = heights.minOrNull()!!
        if (maxHeight - minHeight > 5) {
            return false
        }
        return true
    }

    companion object {
        private val MISSING_CONFIG = MultiplierConfig(0)
        private val INCOMPATIBLE_BIOME_CATEGORIES = setOf(
            Biome.BiomeCategory.BEACH,
            Biome.BiomeCategory.EXTREME_HILLS,
            Biome.BiomeCategory.ICY,
            Biome.BiomeCategory.NETHER,
            Biome.BiomeCategory.OCEAN,
            Biome.BiomeCategory.RIVER,
            Biome.BiomeCategory.THEEND,
            Biome.BiomeCategory.NONE,
            Biome.BiomeCategory.MUSHROOM,
            Biome.BiomeCategory.SWAMP
        )
    }
}