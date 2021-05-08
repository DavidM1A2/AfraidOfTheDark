package com.davidm1a2.afraidofthedark.common.world.structure.observatory

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.world.structure.base.FrequencyConfig
import net.minecraft.world.IWorld
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure.IStartFactory
import java.util.*

class ObservatoryStructure : AOTDStructure<FrequencyConfig>({ FrequencyConfig.deserialize(it) }) {
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
            addToBiome(biome, FrequencyConfig(0.03 * ModCommonConfiguration.observatoryMultiplier))
        } else {
            addToBiome(biome, FrequencyConfig(0.0))
        }
    }

    override fun getStartFactory(): IStartFactory {
        return IStartFactory { structure, chunkX, chunkZ, biome, mutableBoundingBox, reference, seed ->
            ObservatoryStructureStart(structure, chunkX, chunkZ, biome, mutableBoundingBox, reference, seed)
        }
    }

    override fun hasStartAt(worldIn: IWorld, chunkGen: ChunkGenerator<*>, random: Random, xPos: Int, zPos: Int): Boolean {
        val frequency = getInteriorConfigs(xPos, zPos, chunkGen, stepNum = 2).map { it?.frequency ?: 0.0 }.minOrNull() ?: 0.0
        if (random.nextDouble() >= frequency) {
            return false
        }

        val heights = getEdgeHeights(xPos, zPos, chunkGen, worldIn)
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