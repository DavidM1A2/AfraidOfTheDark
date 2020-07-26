package com.davidm1a2.afraidofthedark.common.world.structure.witchhut

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
import kotlin.math.min

class WitchHutStructure : AOTDStructure<WitchHutConfig>() {
    override fun getStructureName(): String {
        return "${Constants.MOD_ID}:witch_hut"
    }

    override fun getSize(): Int {
        return (max(ModSchematics.WITCH_HUT.getWidth().toInt(), ModSchematics.WITCH_HUT.getLength().toInt()) + 15) / 16
    }

    override fun setupStructureIn(biome: Biome) {
        if (biome == ModBiomes.EERIE_FOREST) {
            addToBiome(biome, WitchHutConfig(0.04 * ModCommonConfiguration.witchHutMultiplier))
        } else {
            addToBiome(biome, WitchHutConfig(0.0))
        }
    }

    override fun isEnabledIn(worldIn: IWorld): Boolean {
        return worldIn.dimension.type == DimensionType.OVERWORLD
    }

    override fun hasStartAt(worldIn: IWorld, chunkGen: IChunkGenerator<*>, rand: Random, centerChunkX: Int, centerChunkZ: Int): Boolean {
        (rand as SharedSeedRandom).setLargeFeatureSeed(chunkGen.seed, centerChunkX, centerChunkZ)

        val xPos = centerChunkX * 16
        val zPos = centerChunkZ * 16

        val realWidth = ModSchematics.WITCH_HUT.getWidth().toInt()
        val realLength = ModSchematics.WITCH_HUT.getLength().toInt()

        val biomes = chunkGen.biomeProvider.getBiomesInSquare(
            xPos,
            zPos,
            max(realWidth, realLength)
        )

        val frequency = biomes.map {
            val witchHutConfig = chunkGen.getStructureConfig(it, this) as? WitchHutConfig
            witchHutConfig?.frequency ?: 0.0
        }.min() ?: 0.0

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

        if (maxHeight - minHeight > 3) {
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

    override fun makeStart(worldIn: IWorld, generator: IChunkGenerator<*>, random: SharedSeedRandom, x: Int, z: Int): StructureStart {
        val xPos = x * 16
        val zPos = z * 16
        val centerBiome = generator.biomeProvider.getBiome(BlockPos(xPos + 8, 0, zPos + 8), Biomes.PLAINS)!!

        val realWidth = ModSchematics.WITCH_HUT.getWidth().toInt()
        val realLength = ModSchematics.WITCH_HUT.getLength().toInt()

        val centerCorner1Height = WorldHeightmap.getHeight(xPos - realWidth / 2, zPos - realLength / 2, worldIn, generator)
        val centerCorner2Height = WorldHeightmap.getHeight(xPos + realWidth / 2, zPos - realLength / 2, worldIn, generator)
        val centerCorner3Height = WorldHeightmap.getHeight(xPos - realWidth / 2, zPos + realLength / 2, worldIn, generator)
        val centerCorner4Height = WorldHeightmap.getHeight(xPos + realWidth / 2, zPos + realLength / 2, worldIn, generator)
        val yPos = min(min(centerCorner1Height, centerCorner2Height), min(centerCorner3Height, centerCorner4Height))

        return WitchHutStructureStart(worldIn, x, yPos - 1, z, centerBiome, random, generator.seed)
    }
}