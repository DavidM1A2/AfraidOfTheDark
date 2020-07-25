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
import java.util.*
import kotlin.math.max
import kotlin.math.roundToInt

class CryptStructure : AOTDStructure<CryptConfig>() {
    public override fun getStructureName(): String {
        return "${Constants.MOD_ID}:crypt"
    }

    override fun getSize(): Int {
        return (max(ModSchematics.CRYPT.getWidth().toInt(), ModSchematics.CRYPT.getLength().toInt()) + 15) / 16
    }

    override fun setupStructureIn(biome: Biome) {
        if (biome !in INCOMPATIBLE_BIOMES) {
            if (biome == ModBiomes.EERIE_FOREST) {
                addToBiome(biome, CryptConfig(0.02 * ModCommonConfiguration.cryptMultiplier))
            } else {
                addToBiome(biome, CryptConfig(0.002 * ModCommonConfiguration.cryptMultiplier))
            }
        }
    }

    override fun isEnabledIn(worldIn: IWorld): Boolean {
        return worldIn.dimension.type == DimensionType.OVERWORLD
    }

    override fun hasStartAt(worldIn: IWorld, chunkGen: IChunkGenerator<*>, rand: Random, centerChunkX: Int, centerChunkZ: Int): Boolean {
        (rand as SharedSeedRandom).setLargeFeatureSeed(chunkGen.seed, centerChunkX, centerChunkZ)

        val xPos = centerChunkX shl 4
        val zPos = centerChunkZ shl 4

        val realWidth = ModSchematics.CRYPT.getWidth().toInt()
        val realLength = ModSchematics.CRYPT.getLength().toInt()

        val biomes = chunkGen.biomeProvider.getBiomes(
            xPos - realWidth / 2,
            zPos - realLength / 2,
            realWidth,
            realLength
        ).toSet()

        val frequency = biomes.map {
            if (!chunkGen.hasStructure(it, this)) {
                return false
            } else {
                val cryptConfig = chunkGen.getStructureConfig(it, this) as? CryptConfig
                cryptConfig?.frequency ?: 0.0
            }
        }.max() ?: 0.0

        if (frequency == 0.0) {
            return false
        }

        val corner1Height = WorldHeightmap.getHeight(xPos - realWidth / 2, zPos - realLength / 2, worldIn, chunkGen)
        val corner2Height = WorldHeightmap.getHeight(xPos + realWidth / 2, zPos - realLength / 2, worldIn, chunkGen)
        val corner3Height = WorldHeightmap.getHeight(xPos - realWidth / 2, zPos + realLength / 2, worldIn, chunkGen)
        val corner4Height = WorldHeightmap.getHeight(xPos + realWidth / 2, zPos + realLength / 2, worldIn, chunkGen)
        val centerHeight = WorldHeightmap.getHeight(xPos, zPos, worldIn, chunkGen)
        val heights = arrayOf(corner1Height, corner2Height, corner3Height, corner4Height, centerHeight)

        val maxHeight = heights.max()!!
        val minHeight = heights.min()!!

        if (maxHeight - minHeight > 5) {
            return false
        }

        return rand.nextDouble() < frequency
    }

    override fun makeStart(worldIn: IWorld, generator: IChunkGenerator<*>, random: SharedSeedRandom, centerChunkX: Int, centerChunkZ: Int): StructureStart {
        val xPos = centerChunkX shl 4
        val zPos = centerChunkZ shl 4
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
            Biomes.OCEAN,
            Biomes.MOUNTAINS,
            Biomes.SWAMP,
            Biomes.RIVER,
            Biomes.THE_END,
            Biomes.FROZEN_OCEAN,
            Biomes.FROZEN_RIVER,
            Biomes.SNOWY_MOUNTAINS,
            Biomes.BEACH,
            Biomes.MOUNTAIN_EDGE,
            Biomes.DEEP_OCEAN,
            Biomes.STONE_SHORE,
            Biomes.SNOWY_BEACH,
            Biomes.END_MIDLANDS,
            Biomes.END_HIGHLANDS,
            Biomes.END_BARRENS,
            Biomes.WARM_OCEAN,
            Biomes.LUKEWARM_OCEAN,
            Biomes.COLD_OCEAN,
            Biomes.DEEP_WARM_OCEAN,
            Biomes.DEEP_LUKEWARM_OCEAN,
            Biomes.DEEP_COLD_OCEAN,
            Biomes.DEEP_FROZEN_OCEAN,
            Biomes.THE_VOID,
            Biomes.DESERT_LAKES,
            Biomes.GRAVELLY_MOUNTAINS,
            Biomes.TAIGA_MOUNTAINS,
            Biomes.ICE_SPIKES,
            Biomes.SNOWY_TAIGA_MOUNTAINS,
            Biomes.MODIFIED_GRAVELLY_MOUNTAINS
        )
    }
}