package com.davidm1a2.afraidofthedark.common.world.structure.voidchest

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

class VoidChestStructure : AOTDStructure<VoidChestConfig>() {
    override fun getStructureName(): String {
        return "${Constants.MOD_ID}:void_chest"
    }

    override fun getWidth(): Int {
        return ModSchematics.VOID_CHEST.getWidth().toInt()
    }

    override fun getLength(): Int {
        return ModSchematics.VOID_CHEST.getLength().toInt()
    }

    override fun setupStructureIn(biome: Biome) {
        if (biome in COMPATIBLE_BIOMES) {
            addToBiome(biome, VoidChestConfig(0.006 * ModCommonConfiguration.voidChestMultiplier))
        } else {
            addToBiome(biome, VoidChestConfig(0.0))
        }
    }

    override fun isEnabledIn(worldIn: IWorld): Boolean {
        return worldIn.dimension.type == DimensionType.OVERWORLD
    }

    override fun hasStartAt(worldIn: IWorld, chunkGen: IChunkGenerator<*>, rand: SharedSeedRandom, centerChunkX: Int, centerChunkZ: Int): Boolean {
        rand.setLargeFeatureSeed(chunkGen.seed, centerChunkX, centerChunkZ)

        val xPos = centerChunkX * 16
        val zPos = centerChunkZ * 16

        val frequency = getInteriorConfigs(xPos, zPos, chunkGen).map { it?.frequency ?: 0.0 }.max() ?: 0.0
        if (rand.nextDouble() >= frequency) {
            return false
        }

        val heights = getEdgeHeights(xPos, zPos, worldIn, chunkGen)
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

        val yPos = getEdgeHeights(xPos, zPos, worldIn, generator).min()!!
        return VoidChestStructureStart(worldIn, centerChunkX, yPos - 7, centerChunkZ, centerBiome, random, generator.seed)
    }

    companion object {
        // A set of compatible biomes
        private val COMPATIBLE_BIOMES = setOf(
            Biomes.SNOWY_BEACH,
            Biomes.SNOWY_TAIGA,
            Biomes.SNOWY_TUNDRA,
            Biomes.ICE_SPIKES
        )
    }
}