package com.davidm1a2.afraidofthedark.common.world.structure.observatory

import com.davidm1a2.afraidofthedark.common.constants.Constants
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

class ObservatoryStructure : AOTDStructure<ObservatoryConfig>() {
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
            addToBiome(biome, ObservatoryConfig(0.03 * ModCommonConfiguration.observatoryMultiplier))
        } else {
            addToBiome(biome, ObservatoryConfig(0.0))
        }
    }

    override fun isEnabledIn(worldIn: IWorld): Boolean {
        return worldIn.dimension.type == DimensionType.OVERWORLD
    }

    override fun hasStartAt(worldIn: IWorld, chunkGen: IChunkGenerator<*>, rand: SharedSeedRandom, centerChunkX: Int, centerChunkZ: Int): Boolean {
        rand.setLargeFeatureSeed(chunkGen.seed, centerChunkX, centerChunkZ)

        val xPos = centerChunkX * 16
        val zPos = centerChunkZ * 16

        val frequency = getInteriorConfigs(xPos, zPos, chunkGen, stepNum = 2).map { it?.frequency ?: 0.0 }.min() ?: 0.0
        if (rand.nextDouble() >= frequency) {
            return false
        }

        val heights = getEdgeHeights(xPos, zPos, worldIn, chunkGen)
        val maxHeight = heights.max()!!
        val minHeight = heights.min()!!
        // If there's more than 3 blocks between the top and bottom block it's an invalid place for an observatory because it's not 'flat' enough
        // If the flat spot is below y 70 it's also not a good spot
        if (maxHeight - minHeight > 3 && minHeight <= 70) {
            return false
        }

        return doesNotCollide(worldIn, chunkGen, rand, centerChunkX, centerChunkZ)
    }

    override fun makeStart(worldIn: IWorld, generator: IChunkGenerator<*>, random: SharedSeedRandom, centerChunkX: Int, centerChunkZ: Int): StructureStart {
        random.setLargeFeatureSeed(generator.seed, centerChunkX, centerChunkZ)

        val xPos = centerChunkX * 16
        val zPos = centerChunkZ * 16
        val centerBiome = generator.biomeProvider.getBiome(BlockPos(xPos, 0, zPos), Biomes.PLAINS)!!

        val yPos = getEdgeHeights(xPos, zPos, worldIn, generator).min()!!
        return ObservatoryStructureStart(worldIn, centerChunkX, yPos - 1, centerChunkZ, centerBiome, random, generator.seed)
    }
}