package com.davidm1a2.afraidofthedark.common.feature.structure.voidchestbox

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModBiomes
import com.davidm1a2.afraidofthedark.common.feature.structure.base.AOTDStructure
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.BiomeManager
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.IFeatureConfig
import net.minecraft.world.gen.feature.NoFeatureConfig
import net.minecraft.world.gen.feature.structure.Structure.IStartFactory
import java.util.*

class VoidChestBoxStructure : AOTDStructure<NoFeatureConfig>({ IFeatureConfig.NO_FEATURE_CONFIG }, false) {
    override fun getStructureName(): String {
        return "${Constants.MOD_ID}:void_chest_box"
    }

    override fun getWidth(): Int {
        return 48
    }

    override fun getLength(): Int {
        return 48
    }

    override fun setupStructureIn(biome: Biome) {
        if (biome == ModBiomes.VOID_CHEST) {
            addToBiome(biome, IFeatureConfig.NO_FEATURE_CONFIG)
        }
    }

    override fun getStartFactory(): IStartFactory {
        return IStartFactory { structure, chunkX, chunkZ, mutableBoundingBox, reference, seed ->
            VoidChestBoxStructureStart(structure, chunkX, chunkZ, mutableBoundingBox, reference, seed)
        }
    }

    override fun canFitAt(chunkGen: ChunkGenerator<*>, biomeManager: BiomeManager, random: Random, xPos: Int, zPos: Int): Boolean {
        // no-op, this is only for structures generated via our AOTD quadtree structure mapper
        return false
    }

    override fun canBeGenerated(
        biomeManager: BiomeManager,
        chunkGenerator: ChunkGenerator<*>,
        random: Random,
        centerChunkX: Int,
        centerChunkZ: Int,
        biome: Biome
    ): Boolean {
        val xStart = centerChunkX * 16
        val xEnd = xStart + 15
        val amountOverMultipleOf1000 = xEnd % 1000

        return centerChunkZ * 16 == 0 && xStart >= 0 && amountOverMultipleOf1000 < 16
    }
}