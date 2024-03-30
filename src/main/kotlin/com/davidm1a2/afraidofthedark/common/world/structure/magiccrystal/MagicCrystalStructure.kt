package com.davidm1a2.afraidofthedark.common.world.structure.magiccrystal

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

class MagicCrystalStructure : AOTDStructure<MultiplierConfig>("magic_crystal", MultiplierConfig.CODEC) {
    override fun getWidth(): Int {
        return ModSchematics.MAGIC_CRYSTAL.getWidth().toInt()
    }

    override fun getLength(): Int {
        return ModSchematics.MAGIC_CRYSTAL.getLength().toInt()
    }

    override fun getStartFactory(): StructureStartFactory<MultiplierConfig> {
        return StructureStartFactory { structure, chunkPos, reference, seed ->
            MagicCrystalStructureStart(structure, chunkPos, reference, seed)
        }
    }

    override fun configured(biome: ResourceKey<Biome>, category: Biome.BiomeCategory): ConfiguredStructureFeature<MultiplierConfig, out StructureFeature<MultiplierConfig>>? {
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
        val chance = getOneInNValidChunks(400) * ModCommonConfiguration.magicCrystalMultiplier
        if (random.nextDouble() >= chance) {
            return false
        }

        val heights = getEdgeHeights(xPos, zPos, chunkGen, levelHeightAccessor)
        val maxHeight = heights.maxOrNull()!!
        val minHeight = heights.minOrNull()!!
        if (maxHeight - minHeight > 2) {
            return false
        }
        return true
    }

    companion object {
        private val MISSING_CONFIG = MultiplierConfig(0)
        private val INCOMPATIBLE_BIOME_CATEGORIES = setOf(
            Biome.BiomeCategory.NETHER,
            Biome.BiomeCategory.OCEAN,
            Biome.BiomeCategory.RIVER,
            Biome.BiomeCategory.THEEND,
            Biome.BiomeCategory.NONE
        )
    }
}