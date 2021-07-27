package com.davidm1a2.afraidofthedark.common.feature.structure.witchhut

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModBiomes
import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.feature.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.feature.structure.base.MultiplierConfig
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.BiomeManager
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure.IStartFactory
import java.util.*

class WitchHutStructure : AOTDStructure<MultiplierConfig>({ MultiplierConfig.deserialize(it) }) {
    override fun getStructureName(): String {
        return "${Constants.MOD_ID}:witch_hut"
    }

    override fun getWidth(): Int {
        return ModSchematics.WITCH_HUT.getWidth().toInt()
    }

    override fun getLength(): Int {
        return ModSchematics.WITCH_HUT.getLength().toInt()
    }

    override fun setupStructureIn(biome: Biome) {
        if (biome == ModBiomes.EERIE_FOREST) {
            addToBiome(biome, MultiplierConfig(1))
        } else {
            addToBiome(biome, MultiplierConfig(0))
        }
    }

    override fun getStartFactory(): IStartFactory {
        return IStartFactory { structure, chunkX, chunkZ, mutableBoundingBox, reference, seed ->
            WitchHutStructureStart(structure, chunkX, chunkZ, mutableBoundingBox, reference, seed)
        }
    }

    override fun canFitAt(chunkGen: ChunkGenerator<*>, biomeManager: BiomeManager, random: Random, xPos: Int, zPos: Int): Boolean {
        val biomeMultiplier = getInteriorConfigEstimate(xPos, zPos, chunkGen, biomeManager).map { it.multiplier }.minOrNull() ?: 0
        if (random.nextDouble() >= getOneInNChunksChance(100) * ModCommonConfiguration.witchHutMultiplier * biomeMultiplier) {
            return false
        }

        val heights = getEdgeHeights(xPos, zPos, chunkGen)
        val maxHeight = heights.maxOrNull()!!
        val minHeight = heights.minOrNull()!!
        if (maxHeight - minHeight > 3) {
            return false
        }
        return true
    }
}