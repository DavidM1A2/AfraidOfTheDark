package com.davidm1a2.afraidofthedark.common.world.structure.darkforest

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModBiomes
import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.world.structure.base.MultiplierConfig
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biomes
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure.IStartFactory
import java.util.*

class DarkForestStructure : AOTDStructure<MultiplierConfig>({ MultiplierConfig.deserialize(it) }) {
    private val width: Int
    private val bedHouseWidth: Int
    private val length: Int
    private val bedHouseLength: Int

    init {
        val widestTree = ModSchematics.DARK_FOREST_TREES.maxOf { it.getWidth() }.toInt()
        val longestTree = ModSchematics.DARK_FOREST_TREES.maxOf { it.getLength() }.toInt()

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
        when (biome) {
            ModBiomes.EERIE_FOREST -> addToBiome(biome, MultiplierConfig(2))
            in COMPATIBLE_HOUSE_BIOMES -> addToBiome(biome, MultiplierConfig(1))
            else -> addToBiome(biome, MultiplierConfig(0))
        }
    }

    override fun getStartFactory(): IStartFactory {
        return IStartFactory { structure, chunkX, chunkZ, biome, mutableBoundingBox, reference, seed ->
            DarkForestStructureStart(structure, chunkX, chunkZ, biome, mutableBoundingBox, reference, seed)
        }
    }

    override fun hasStartAt(worldIn: World, chunkGen: ChunkGenerator<*>, random: Random, missCount: Int, xPos: Int, zPos: Int): Boolean {
        val biomeMultiplier = getInteriorConfigEstimate(xPos, zPos, chunkGen).map { it.multiplier }.minOrNull()!!
        // chance = darkForestMultiplier * biomeMultiplier * CHANCE_QUARTIC_COEFFICIENT * missCount^4
        val chance = ModCommonConfiguration.darkForestMultiplier * biomeMultiplier * (CHANCE_QUARTIC_COEFFICIENT * missCount).powOptimized(4)
        if (random.nextDouble() >= chance) {
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
        // 4th root of 0.000000000000001
        private const val CHANCE_QUARTIC_COEFFICIENT = 0.0001778279

        // A set of compatible biomes
        private val COMPATIBLE_HOUSE_BIOMES = setOf(
            Biomes.SAVANNA,
            Biomes.SAVANNA_PLATEAU,
            Biomes.PLAINS,
            Biomes.SUNFLOWER_PLAINS
        )
    }
}