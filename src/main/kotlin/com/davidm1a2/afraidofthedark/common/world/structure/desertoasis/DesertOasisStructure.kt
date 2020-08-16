package com.davidm1a2.afraidofthedark.common.world.structure.desertoasis

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
import kotlin.math.max
import kotlin.math.roundToInt

class DesertOasisStructure : AOTDStructure<DesertOasisConfig>() {
    override fun getStructureName(): String {
        return "${Constants.MOD_ID}:desert_oasis"
    }

    override fun getWidth(): Int {
        return ModSchematics.DESERT_OASIS.getWidth().toInt()
    }

    override fun getLength(): Int {
        return ModSchematics.DESERT_OASIS.getLength().toInt()
    }

    override fun setupStructureIn(biome: Biome) {
        if (biome.category in VALID_BIOME_CATEGORIES) {
            addToBiome(biome, DesertOasisConfig(true))
        } else {
            addToBiome(biome, DesertOasisConfig(false))
        }
    }

    override fun isEnabledIn(worldIn: IWorld): Boolean {
        return worldIn.dimension.type == DimensionType.OVERWORLD
    }

    override fun hasStartAt(worldIn: IWorld, chunkGen: IChunkGenerator<*>, rand: SharedSeedRandom, centerChunkX: Int, centerChunkZ: Int): Boolean {
        rand.setLargeFeatureSeed(chunkGen.seed, centerChunkX, centerChunkZ)

        val xPos = centerChunkX * 16
        val zPos = centerChunkZ * 16

        val frequency = getInteriorConfigs(xPos, zPos, chunkGen, stepNum = 7).groupingBy { it!!.supported }.eachCount()
        val numValid = frequency[true] ?: 0
        val numInvalid = max(frequency[false] ?: 1, 1)
        val percentDesertTiles = numValid / (numValid + numInvalid).toDouble()

        // 70% valid tiles required, .5% chance to spawn
        if (percentDesertTiles < 0.7) {
            return false
        }
        if (rand.nextDouble() >= 0.005 * ModCommonConfiguration.desertOasisMultiplier) {
            return false
        }

        return doesNotCollide(worldIn, chunkGen, rand, centerChunkX, centerChunkZ)
    }

    override fun makeStart(worldIn: IWorld, generator: IChunkGenerator<*>, random: SharedSeedRandom, centerChunkX: Int, centerChunkZ: Int): StructureStart {
        random.setLargeFeatureSeed(generator.seed, centerChunkX, centerChunkZ)

        val xPos = centerChunkX * 16
        val zPos = centerChunkZ * 16
        val centerBiome = generator.biomeProvider.getBiome(BlockPos(xPos, 0, zPos), Biomes.PLAINS)!!

        val yPos = getEdgeHeights(xPos, zPos, worldIn, generator).average().roundToInt()
        return DesertOasisStructureStart(worldIn, centerChunkX, (yPos - 18).coerceIn(0..255), centerChunkZ, centerBiome, random, generator.seed)
    }

    companion object {
        private val VALID_BIOME_CATEGORIES = setOf(
            Biome.Category.DESERT,
            Biome.Category.RIVER
        )
    }
}