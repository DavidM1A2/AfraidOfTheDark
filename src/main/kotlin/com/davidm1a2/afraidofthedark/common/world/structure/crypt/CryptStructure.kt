package com.davidm1a2.afraidofthedark.common.world.structure.crypt

import com.davidm1a2.afraidofthedark.common.capabilities.world.StructureCollisionMap
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
        if (biome.category !in INCOMPATIBLE_BIOMES) {
            if (biome == ModBiomes.EERIE_FOREST) {
                addToBiome(biome, CryptConfig(0.02 * ModCommonConfiguration.cryptMultiplier))
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

    override fun hasStartAt(worldIn: IWorld, chunkGen: IChunkGenerator<*>, rand: Random, centerChunkX: Int, centerChunkZ: Int): Boolean {
        (rand as SharedSeedRandom).setLargeFeatureSeed(chunkGen.seed, centerChunkX, centerChunkZ)

        val xPos = centerChunkX * 16
        val zPos = centerChunkZ * 16

        val realWidth = ModSchematics.CRYPT.getWidth().toInt()
        val realLength = ModSchematics.CRYPT.getLength().toInt()

        val biomes = chunkGen.biomeProvider.getBiomesInSquare(
            xPos,
            zPos,
            max(realWidth, realLength)
        )

        val frequency = biomes.map {
            val cryptConfig = chunkGen.getStructureConfig(it, this) as? CryptConfig
            cryptConfig?.frequency ?: 0.0
        }.max() ?: 0.0

        if (rand.nextDouble() >= frequency) {
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

        val expectedStart = makeStart(worldIn, chunkGen, rand, centerChunkX, centerChunkZ)
        val collisionMap = StructureCollisionMap.get(worldIn)
        synchronized(collisionMap) {
            return if (!collisionMap.isStructureBlocked(expectedStart)) {
                collisionMap.insertStructure(expectedStart)
                true
            } else {
                false
            }
        }
    }

    override fun makeStart(worldIn: IWorld, generator: IChunkGenerator<*>, random: SharedSeedRandom, centerChunkX: Int, centerChunkZ: Int): StructureStart {
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
            Biome.Category.THEEND
        )
    }
}