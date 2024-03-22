package com.davidm1a2.afraidofthedark.common.world.structure.darkforest

import com.davidm1a2.afraidofthedark.common.constants.ModBiomes
import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.world.structure.base.MultiplierConfig
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.LevelHeightAccessor
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.BiomeSource
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature
import net.minecraft.world.level.levelgen.feature.StructureFeature
import net.minecraft.world.level.levelgen.feature.StructureFeature.StructureStartFactory
import java.util.*

class DarkForestStructure : AOTDStructure<MultiplierConfig>("dark_forest", MultiplierConfig.CODEC) {
    private val width: Int
    private val bedHouseWidth: Int
    private val length: Int
    private val bedHouseLength: Int

    init {
        val widestTree = ModSchematics.DARK_FOREST_TREES.maxOf { it.getWidth() }.toInt()
        val longestTree = ModSchematics.DARK_FOREST_TREES.maxOf { it.getLength() }.toInt()

        bedHouseWidth = ModSchematics.BED_HOUSE.getWidth().toInt()
        bedHouseLength = ModSchematics.BED_HOUSE.getLength().toInt()

        // Width is width(BED_HOUSE) + 2 * width(BIGGEST_TREE)
        width = bedHouseWidth + 2 * widestTree

        // Length is length(BED_HOUSE) + 2 * length(BIGGEST_TREE)
        length = bedHouseLength + 2 * longestTree
    }

    override fun getWidth(): Int {
        return width
    }

    override fun getLength(): Int {
        return length
    }

    override fun getStartFactory(): StructureStartFactory<MultiplierConfig> {
        return StructureStartFactory { structure, chunkPos, reference, seed ->
            DarkForestStructureStart(structure, chunkPos, reference, seed)
        }
    }

    override fun configured(biome: ResourceKey<Biome>, category: Biome.BiomeCategory): ConfiguredStructureFeature<MultiplierConfig, out StructureFeature<MultiplierConfig>>? {
        return when (biome) {
            ModBiomes.EERIE_FOREST -> configured(MultiplierConfig(2))
            in COMPATIBLE_HOUSE_BIOMES -> configured(MultiplierConfig(1))
            else -> null
        }
    }

    override fun configuredFlat(): ConfiguredStructureFeature<MultiplierConfig, out StructureFeature<MultiplierConfig>> {
        return configured(MISSING_CONFIG)
    }

    override fun canFitAt(chunkGen: ChunkGenerator, biomeProvider: BiomeSource, random: Random, xPos: Int, zPos: Int, levelHeightAccessor: LevelHeightAccessor): Boolean {
        val biomeMultiplier = getInteriorConfigEstimate(xPos, zPos, biomeProvider, MISSING_CONFIG)
            .map { it.multiplier }
            .minOrNull() ?: 0

        val chance = getOneInNValidChunks(40) * ModCommonConfiguration.darkForestMultiplier * biomeMultiplier
        if (random.nextDouble() >= chance) {
            return false
        }

        val heights = getEdgeHeights(xPos, zPos, chunkGen, levelHeightAccessor, bedHouseWidth, bedHouseLength)
        val maxHeight = heights.maxOrNull()!!
        val minHeight = heights.minOrNull()!!
        if (maxHeight - minHeight > 8) {
            return false
        }
        return true
    }

    companion object {
        private val MISSING_CONFIG = MultiplierConfig(0)

        // A set of compatible biomes
        private val COMPATIBLE_HOUSE_BIOMES = setOf(
            Biomes.SAVANNA,
            Biomes.SAVANNA_PLATEAU,
            Biomes.PLAINS,
            Biomes.SUNFLOWER_PLAINS
        )
    }
}