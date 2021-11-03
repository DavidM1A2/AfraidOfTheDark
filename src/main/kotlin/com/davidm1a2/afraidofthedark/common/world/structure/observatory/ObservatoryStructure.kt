package com.davidm1a2.afraidofthedark.common.world.structure.observatory

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

class ObservatoryStructure : AOTDStructure<BooleanConfig>("observatory", BooleanConfig.CODEC) {
    override fun getWidth(): Int {
        return ModSchematics.OBSERVATORY.getWidth().toInt()
    }

    override fun getLength(): Int {
        return ModSchematics.OBSERVATORY.getLength().toInt()
    }

    override fun getStartFactory(): IStartFactory<BooleanConfig> {
        return IStartFactory { structure, chunkX, chunkZ, mutableBoundingBox, reference, seed ->
            ObservatoryStructureStart(structure, chunkX, chunkZ, mutableBoundingBox, reference, seed)
        }
    }

    override fun configured(biome: RegistryKey<Biome>, category: Biome.Category): StructureFeature<BooleanConfig, out Structure<BooleanConfig>>? {
        return if (category == Biome.Category.EXTREME_HILLS) {
            configured(BooleanConfig(true))
        } else {
            null
        }
    }

    override fun configuredFlat(): StructureFeature<BooleanConfig, out Structure<BooleanConfig>> {
        return configured(BooleanConfig(false))
    }

    override fun canFitAt(chunkGen: ChunkGenerator, biomeProvider: BiomeProvider, random: Random, xPos: Int, zPos: Int): Boolean {
        val isNotSupported = getInteriorConfigEstimate(xPos, zPos, biomeProvider, MISSING_CONFIG).any { !it.supported }
        if (isNotSupported) {
            return false
        }

        val chance = getOneInNValidChunks(50) * ModCommonConfiguration.observatoryMultiplier
        if (random.nextDouble() >= chance) {
            return false
        }

        val heights = getEdgeHeights(xPos, zPos, chunkGen)
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