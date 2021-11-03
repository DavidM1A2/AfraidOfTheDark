package com.davidm1a2.afraidofthedark.common.world.structure.crypt

import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.world.structure.base.MultiplierConfig
import net.minecraft.util.RegistryKey
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.provider.BiomeProvider
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.StructureFeature
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraft.world.gen.feature.structure.Structure.IStartFactory
import java.util.Random

class CryptStructure : AOTDStructure<MultiplierConfig>("crypt", MultiplierConfig.CODEC) {
    override fun getWidth(): Int {
        return ModSchematics.CRYPT.getWidth().toInt()
    }

    override fun getLength(): Int {
        return ModSchematics.CRYPT.getLength().toInt()
    }

    override fun getStartFactory(): IStartFactory<MultiplierConfig> {
        return IStartFactory { structure, chunkX, chunkZ, mutableBoundingBox, reference, seed ->
            CryptStructureStart(structure, chunkX, chunkZ, mutableBoundingBox, reference, seed)
        }
    }

    override fun configured(biome: RegistryKey<Biome>, category: Biome.Category): StructureFeature<MultiplierConfig, out Structure<MultiplierConfig>>? {
        return if (category !in INCOMPATIBLE_BIOME_CATEGORIES) {
            configured(MultiplierConfig(1))
        } else {
            null
        }
    }

    override fun configuredFlat(): StructureFeature<MultiplierConfig, out Structure<MultiplierConfig>> {
        return configured(MISSING_CONFIG)
    }

    override fun canFitAt(chunkGen: ChunkGenerator, biomeProvider: BiomeProvider, random: Random, xPos: Int, zPos: Int): Boolean {
        val biomeMultiplier = getInteriorConfigEstimate(xPos, zPos, biomeProvider, MISSING_CONFIG)
            .map { it.multiplier }
            .minOrNull() ?: 0

        val chance = getOneInNValidChunks(350) * ModCommonConfiguration.cryptMultiplier * biomeMultiplier
        if (random.nextDouble() >= chance) {
            return false
        }

        val heights = getEdgeHeights(xPos, zPos, chunkGen)
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
            Biome.Category.BEACH,
            Biome.Category.EXTREME_HILLS,
            Biome.Category.ICY,
            Biome.Category.NETHER,
            Biome.Category.OCEAN,
            Biome.Category.RIVER,
            Biome.Category.THEEND,
            Biome.Category.NONE,
            Biome.Category.MUSHROOM,
            Biome.Category.SWAMP
        )
    }
}