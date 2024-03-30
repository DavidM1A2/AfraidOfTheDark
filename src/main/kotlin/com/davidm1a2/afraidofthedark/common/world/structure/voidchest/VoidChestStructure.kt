package com.davidm1a2.afraidofthedark.common.world.structure.voidchest

import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.world.structure.base.BooleanConfig
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

class VoidChestStructure : AOTDStructure<BooleanConfig>("void_chest", BooleanConfig.CODEC) {
    override fun getWidth(): Int {
        return ModSchematics.VOID_CHEST.getWidth().toInt()
    }

    override fun getLength(): Int {
        return ModSchematics.VOID_CHEST.getLength().toInt()
    }

    override fun getStartFactory(): StructureStartFactory<BooleanConfig> {
        return StructureStartFactory { structure, chunkPos, reference, seed ->
            VoidChestStructureStart(structure, chunkPos, reference, seed)
        }
    }

    override fun configured(biome: ResourceKey<Biome>, category: Biome.BiomeCategory): ConfiguredStructureFeature<BooleanConfig, out StructureFeature<BooleanConfig>>? {
        return if (biome in COMPATIBLE_BIOMES) {
            configured(BooleanConfig(true))
        } else {
            null
        }
    }

    override fun configuredFlat(): ConfiguredStructureFeature<BooleanConfig, out StructureFeature<BooleanConfig>> {
        return configured(BooleanConfig(false))
    }

    override fun canFitAt(chunkGen: ChunkGenerator, biomeProvider: BiomeSource, random: Random, xPos: Int, zPos: Int, levelHeightAccessor: LevelHeightAccessor): Boolean {
        val chance = getOneInNValidChunks(150) * ModCommonConfiguration.voidChestMultiplier
        if (random.nextDouble() >= chance) {
            return false
        }

        val isNotSupported = getInteriorConfigEstimate(xPos, zPos, biomeProvider, MISSING_CONFIG).any { !it.supported }
        if (isNotSupported) {
            return false
        }

        val heights = getEdgeHeights(xPos, zPos, chunkGen, levelHeightAccessor)
        val maxHeight = heights.maxOrNull()!!
        val minHeight = heights.minOrNull()!!
        if (maxHeight - minHeight > 8) {
            return false
        }
        return true
    }

    companion object {
        private val MISSING_CONFIG = BooleanConfig(false)

        // A set of compatible biomes
        private val COMPATIBLE_BIOMES = setOf(
            Biomes.SNOWY_BEACH,
            Biomes.SNOWY_TAIGA,
            Biomes.SNOWY_TUNDRA,
            Biomes.ICE_SPIKES
        )
    }
}