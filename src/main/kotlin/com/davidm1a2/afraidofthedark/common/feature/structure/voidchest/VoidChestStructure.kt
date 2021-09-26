package com.davidm1a2.afraidofthedark.common.feature.structure.voidchest

import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.feature.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.feature.structure.base.BooleanConfig
import net.minecraft.util.RegistryKey
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biomes
import net.minecraft.world.biome.provider.BiomeProvider
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.StructureFeature
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraft.world.gen.feature.structure.Structure.IStartFactory
import java.util.Random

class VoidChestStructure : AOTDStructure<BooleanConfig>("void_chest", BooleanConfig.CODEC) {
    override fun getWidth(): Int {
        return ModSchematics.VOID_CHEST.getWidth().toInt()
    }

    override fun getLength(): Int {
        return ModSchematics.VOID_CHEST.getLength().toInt()
    }

    override fun getStartFactory(): IStartFactory<BooleanConfig> {
        return IStartFactory { structure, chunkX, chunkZ, mutableBoundingBox, reference, seed ->
            VoidChestStructureStart(structure, chunkX, chunkZ, mutableBoundingBox, reference, seed)
        }
    }

    override fun configured(biome: RegistryKey<Biome>, category: Biome.Category): StructureFeature<BooleanConfig, out Structure<BooleanConfig>>? {
        return if (biome in COMPATIBLE_BIOMES) {
            configured(BooleanConfig(true))
        } else {
            null
        }
    }

    override fun configuredFlat(): StructureFeature<BooleanConfig, out Structure<BooleanConfig>> {
        return configured(BooleanConfig(false))
    }

    override fun canFitAt(chunkGen: ChunkGenerator, biomeProvider: BiomeProvider, random: Random, xPos: Int, zPos: Int): Boolean {
        val chance = getOneInNValidChunks(100) * ModCommonConfiguration.voidChestMultiplier
        if (random.nextDouble() >= chance) {
            return false
        }

        val isNotSupported = getInteriorConfigEstimate(xPos, zPos, biomeProvider, MISSING_CONFIG).any { !it.supported }
        if (isNotSupported) {
            return false
        }

        val heights = getEdgeHeights(xPos, zPos, chunkGen)
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