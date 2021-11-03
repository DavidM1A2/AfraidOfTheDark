package com.davidm1a2.afraidofthedark.common.world.structure.desertoasis

import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.world.structure.base.BooleanConfig
import net.minecraft.util.RegistryKey
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.provider.BiomeProvider
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.StructureFeature
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraft.world.gen.feature.structure.Structure.IStartFactory
import java.util.Random

class DesertOasisStructure : AOTDStructure<BooleanConfig>("desert_oasis", BooleanConfig.CODEC) {
    override fun getWidth(): Int {
        return ModSchematics.DESERT_OASIS.getWidth().toInt()
    }

    override fun getLength(): Int {
        return ModSchematics.DESERT_OASIS.getLength().toInt()
    }

    override fun getStartFactory(): IStartFactory<BooleanConfig> {
        return IStartFactory { structure, chunkX, chunkZ, mutableBoundingBox, reference, seed ->
            DesertOasisStructureStart(structure, chunkX, chunkZ, mutableBoundingBox, reference, seed)
        }
    }

    override fun configured(biome: RegistryKey<Biome>, category: Biome.Category): StructureFeature<BooleanConfig, out Structure<BooleanConfig>>? {
        return if (category in VALID_BIOME_CATEGORIES) {
            configured(BooleanConfig(true))
        } else {
            null
        }
    }

    override fun configuredFlat(): StructureFeature<BooleanConfig, out Structure<BooleanConfig>> {
        return configured(MISSING_CONFIG)
    }

    override fun canFitAt(chunkGen: ChunkGenerator, biomeProvider: BiomeProvider, random: Random, xPos: Int, zPos: Int): Boolean {
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
            Biome.Category.DESERT,
            Biome.Category.RIVER
        )
    }
}