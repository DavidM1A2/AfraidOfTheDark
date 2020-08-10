package com.davidm1a2.afraidofthedark.common.world.structure.darkforest

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModBiomes
import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import net.minecraft.init.Biomes
import net.minecraft.util.SharedSeedRandom
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.biome.Biome
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.gen.IChunkGenerator
import net.minecraft.world.gen.feature.structure.StructureStart

class DarkForestStructure : AOTDStructure<DarkForestConfig>() {
    private val width: Int
    private val bedHouseWidth: Int
    private val length: Int
    private val bedHouseLength: Int

    init {
        val widestTree = ModSchematics.DARK_FOREST_TREES.map { it.getWidth() }.max()!!.toInt()
        val longestTree = ModSchematics.DARK_FOREST_TREES.map { it.getLength() }.max()!!.toInt()

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

    override fun isEnabledIn(worldIn: IWorld): Boolean {
        return worldIn.dimension.type == DimensionType.OVERWORLD
    }

    override fun hasStartAt(worldIn: IWorld, chunkGen: IChunkGenerator<*>, rand: SharedSeedRandom, centerChunkX: Int, centerChunkZ: Int): Boolean {
        rand.setLargeFeatureSeed(chunkGen.seed, centerChunkX, centerChunkZ)

        val xPos = centerChunkX * 16
        val zPos = centerChunkZ * 16

        val frequency = getInteriorConfigs(xPos, zPos, chunkGen, bedHouseWidth, bedHouseLength)
            .map { it?.frequency ?: 0.0 }
            .min() ?: 0.0
        if (rand.nextDouble() >= frequency) {
            return false
        }

        val heights = getEdgeHeights(xPos, zPos, worldIn, chunkGen, bedHouseWidth, bedHouseLength)
        val maxHeight = heights.max()!!
        val minHeight = heights.min()!!
        if (maxHeight - minHeight > 8) {
            return false
        }

        return doesNotCollide(worldIn, chunkGen, rand, centerChunkX, centerChunkZ)
    }

    override fun makeStart(worldIn: IWorld, generator: IChunkGenerator<*>, random: SharedSeedRandom, centerChunkX: Int, centerChunkZ: Int): StructureStart {
        random.setLargeFeatureSeed(generator.seed, centerChunkX, centerChunkZ)

        val xPos = centerChunkX * 16
        val zPos = centerChunkZ * 16
        val centerBiome = generator.biomeProvider.getBiome(BlockPos(xPos, 0, zPos), Biomes.PLAINS)!!

        val yPos = getEdgeHeights(xPos, zPos, worldIn, generator, bedHouseWidth, bedHouseLength).min()!!
        return DarkForestStructureStart(worldIn, generator, centerChunkX, yPos - 1, centerChunkZ, centerBiome, random, generator.seed, width, length)
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