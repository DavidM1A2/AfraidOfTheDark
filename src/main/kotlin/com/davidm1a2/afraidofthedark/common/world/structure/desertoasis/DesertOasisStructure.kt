package com.davidm1a2.afraidofthedark.common.world.structure.desertoasis

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

class DesertOasisStructure : AOTDStructure<BooleanConfig>("desert_oasis", BooleanConfig.CODEC) {
    override fun getWidth(): Int {
        return ModSchematics.DESERT_OASIS.getWidth().toInt()
    }

    override fun getLength(): Int {
        return ModSchematics.DESERT_OASIS.getLength().toInt()
    }

    override fun getStartFactory(): StructureStartFactory<BooleanConfig> {
        return StructureStartFactory { structure, chunkPos, reference, seed ->
            DesertOasisStructureStart(structure, chunkPos, reference, seed)
        }
    }

    override fun configured(biome: ResourceKey<Biome>, category: Biome.BiomeCategory): ConfiguredStructureFeature<BooleanConfig, out StructureFeature<BooleanConfig>>? {
        return if (category in VALID_BIOME_CATEGORIES) {
            configured(BooleanConfig(true))
        } else {
            null
        }
    }

    override fun configuredFlat(): ConfiguredStructureFeature<BooleanConfig, out StructureFeature<BooleanConfig>> {
        return configured(MISSING_CONFIG)
    }

    override fun canFitAt(chunkGen: ChunkGenerator, biomeProvider: BiomeSource, random: Random, xPos: Int, zPos: Int, levelHeightAccessor: LevelHeightAccessor): Boolean {
        val numValidTiles = getInteriorConfigEstimate(xPos, zPos, biomeProvider, MISSING_CONFIG)
            .count { it.supported }

        // 66% desert tiles required (there's 9 checked, so 6+ must be valid)
        if (numValidTiles < 6) {
            return false
        }

        val chance = getOneInNValidChunks(200) * ModCommonConfiguration.desertOasisMultiplier
        if (random.nextDouble() >= chance) {
            return false
        }
        return true
    }

    companion object {
        private val MISSING_CONFIG = BooleanConfig(false)

        private val VALID_BIOME_CATEGORIES = setOf(
            Biome.BiomeCategory.DESERT,
            Biome.BiomeCategory.RIVER
        )
    }
}