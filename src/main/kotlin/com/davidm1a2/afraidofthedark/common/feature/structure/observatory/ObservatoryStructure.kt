package com.davidm1a2.afraidofthedark.common.feature.structure.observatory

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.feature.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.feature.structure.base.BooleanConfig
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.BiomeManager
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure.IStartFactory
import java.util.*

class ObservatoryStructure : AOTDStructure<BooleanConfig>({ BooleanConfig.deserialize(it) }) {
    override fun getStructureName(): String {
        return "${Constants.MOD_ID}:observatory"
    }

    override fun getWidth(): Int {
        return ModSchematics.OBSERVATORY.getWidth().toInt()
    }

    override fun getLength(): Int {
        return ModSchematics.OBSERVATORY.getLength().toInt()
    }

    override fun setupStructureIn(biome: Biome) {
        if (biome.category == Biome.Category.EXTREME_HILLS) {
            addToBiome(biome, BooleanConfig(true))
        } else {
            addToBiome(biome, BooleanConfig(false))
        }
    }

    override fun getStartFactory(): IStartFactory {
        return IStartFactory { structure, chunkX, chunkZ, mutableBoundingBox, reference, seed ->
            ObservatoryStructureStart(structure, chunkX, chunkZ, mutableBoundingBox, reference, seed)
        }
    }

    override fun canFitAt(chunkGen: ChunkGenerator<*>, biomeManager: BiomeManager, random: Random, xPos: Int, zPos: Int): Boolean {
        val isNotSupported = getInteriorConfigEstimate(xPos, zPos, chunkGen, biomeManager).any { !it.supported }
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
}