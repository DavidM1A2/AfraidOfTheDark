package com.davidm1a2.afraidofthedark.common.world.structure.darkforest

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModBiomes
import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import net.minecraft.world.IWorld
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biomes
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure.IStartFactory
import java.util.*

class DarkForestStructure : AOTDStructure<DarkForestConfig>({ DarkForestConfig.deserialize(it) }) {
    private val width: Int
    private val bedHouseWidth: Int
    private val length: Int
    private val bedHouseLength: Int

    init {
        val widestTree = ModSchematics.DARK_FOREST_TREES.map { it.getWidth() }.maxOrNull()!!.toInt()
        val longestTree = ModSchematics.DARK_FOREST_TREES.map { it.getLength() }.maxOrNull()!!.toInt()

        bedHouseWidth = ModSchematics.BED_HOUSE.getWidth().toInt()
        bedHouseLength = ModSchematics.BED_HOUSE.getLength().toInt()

        // Width is width(BED_HOUSE) + 2 * width(BIGGEST_TREE)
        width = bedHouseWidth + 2 * widestTree

        // Length is length(BED_HOUSE) + 2 * length(BIGGEST_TREE)
        length = bedHouseLength + 2 * longestTree
    }

    override fun getStructureName(): String {
        return "${Constants.MOD_ID}:dark_forest"
    }

    override fun getWidth(): Int {
        return width
    }

    override fun getLength(): Int {
        return length
    }

    override fun setupStructureIn(biome: Biome) {
        if (biome in COMPATIBLE_HOUSE_BIOMES) {
            addToBiome(biome, DarkForestConfig(0.002 * ModCommonConfiguration.darkForestMultiplier))
        } else {
            addToBiome(biome, DarkForestConfig(0.0))
        }
    }

    override fun getStartFactory(): IStartFactory {
        return IStartFactory { structure, chunkX, chunkZ, biome, mutableBoundingBox, reference, seed ->
            DarkForestStructureStart(structure, chunkX, chunkZ, biome, mutableBoundingBox, reference, seed)
        }
    }

    override fun hasStartAt(worldIn: IWorld, chunkGen: ChunkGenerator<*>, random: Random, xPos: Int, zPos: Int): Boolean {
        val frequency = getInteriorConfigs(xPos, zPos, chunkGen, bedHouseWidth, bedHouseLength, stepNum = 2)
            .map { it?.frequency ?: 0.0 }
            .minOrNull() ?: 0.0
        if (random.nextDouble() >= frequency) {
            return false
        }

        val heights = getEdgeHeights(xPos, zPos, chunkGen, worldIn, bedHouseWidth, bedHouseLength)
        val maxHeight = heights.maxOrNull()!!
        val minHeight = heights.minOrNull()!!
        if (maxHeight - minHeight > 8) {
            return false
        }
        return true
    }

    companion object {
        // A set of compatible biomes
        private val COMPATIBLE_HOUSE_BIOMES = setOf(
            Biomes.SAVANNA,
            Biomes.SAVANNA_PLATEAU,
            Biomes.PLAINS,
            Biomes.SUNFLOWER_PLAINS,
            ModBiomes.EERIE_FOREST
        )
    }
}