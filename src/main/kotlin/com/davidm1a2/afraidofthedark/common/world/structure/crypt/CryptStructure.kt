package com.davidm1a2.afraidofthedark.common.world.structure.crypt

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModBiomes
import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.WorldHeightmap
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import net.minecraft.init.Biomes
import net.minecraft.util.SharedSeedRandom
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.biome.Biome
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.gen.IChunkGenerator
import net.minecraft.world.gen.feature.structure.StructureStart
import kotlin.math.roundToInt

class CryptStructure : AOTDStructure<CryptConfig>() {
    override fun getStructureName(): String {
        return "${Constants.MOD_ID}:crypt"
    }

    override fun getWidth(): Int {
        return ModSchematics.CRYPT.getWidth().toInt()
    }

    override fun getLength(): Int {
        return ModSchematics.CRYPT.getLength().toInt()
    }

    override fun setupStructureIn(biome: Biome) {
        if (biome.category !in INCOMPATIBLE_BIOMES) {
            if (biome == ModBiomes.EERIE_FOREST) {
                addToBiome(biome, CryptConfig(0.015 * ModCommonConfiguration.cryptMultiplier))
            } else {
                addToBiome(biome, CryptConfig(0.002 * ModCommonConfiguration.cryptMultiplier))
            }
        } else {
            addToBiome(biome, CryptConfig(0.0))
        }
    }

    override fun isEnabledIn(worldIn: IWorld): Boolean {
        return worldIn.dimension.type == DimensionType.OVERWORLD
    }

    override fun hasStartAt(worldIn: IWorld, chunkGen: IChunkGenerator<*>, rand: SharedSeedRandom, centerChunkX: Int, centerChunkZ: Int): Boolean {
        rand.setLargeFeatureSeed(chunkGen.seed, centerChunkX, centerChunkZ)

        val xPos = centerChunkX * 16
        val zPos = centerChunkZ * 16

        val frequency = getInteriorConfigs(xPos, zPos, chunkGen, stepNum = 4).map { it?.frequency ?: 0.0 }.max() ?: 0.0
        if (rand.nextDouble() >= frequency) {
            return false
        }

        val heights = getEdgeHeights(xPos, zPos, worldIn, chunkGen)
        val maxHeight = heights.max()!!
        val minHeight = heights.min()!!
        if (maxHeight - minHeight > 5) {
            return false
        }

        return doesNotCollide(worldIn, chunkGen, rand, centerChunkX, centerChunkZ)
    }

    override fun makeStart(worldIn: IWorld, generator: IChunkGenerator<*>, random: SharedSeedRandom, centerChunkX: Int, centerChunkZ: Int): StructureStart {
        random.setLargeFeatureSeed(generator.seed, centerChunkX, centerChunkZ)

        val xPos = centerChunkX * 16
        val zPos = centerChunkZ * 16
        val centerBiome = generator.biomeProvider.getBiome(BlockPos(xPos + 8, 0, zPos + 8), Biomes.PLAINS)!!

        // The height of the structure = average of the 4 center corner's height
        val centerCorner1Height = WorldHeightmap.getHeight(xPos - 3, zPos - 3, worldIn, generator)
        val centerCorner2Height = WorldHeightmap.getHeight(xPos + 3, zPos - 3, worldIn, generator)
        val centerCorner3Height = WorldHeightmap.getHeight(xPos - 3, zPos + 3, worldIn, generator)
        val centerCorner4Height = WorldHeightmap.getHeight(xPos + 3, zPos + 3, worldIn, generator)
        val yPos = ((centerCorner1Height + centerCorner2Height + centerCorner3Height + centerCorner4Height) / 4.0).roundToInt()

        // Set the schematic height to be underground + 3 blocks+, ensure it isn't below bedrock
        val adjustedY = (yPos - ModSchematics.CRYPT.getHeight() + 3).coerceAtLeast(1)
        return CryptStructureStart(worldIn, centerChunkX, adjustedY, centerChunkZ, centerBiome, random, generator.seed)
    }

    companion object {
        private val INCOMPATIBLE_BIOMES = setOf(
            Biome.Category.BEACH,
            Biome.Category.EXTREME_HILLS,
            Biome.Category.ICY,
            Biome.Category.NETHER,
            Biome.Category.OCEAN,
            Biome.Category.RIVER,
            Biome.Category.THEEND,
            Biome.Category.NONE,
            Biome.Category.MUSHROOM
        )
    }
}