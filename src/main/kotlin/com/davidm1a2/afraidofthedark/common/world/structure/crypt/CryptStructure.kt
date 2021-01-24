package com.davidm1a2.afraidofthedark.common.world.structure.crypt

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModBiomes
import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import net.minecraft.world.IWorld
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure.IStartFactory
import java.util.*

class CryptStructure : AOTDStructure<CryptConfig>({ CryptConfig.deserialize(it) }) {
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

    override fun getStartFactory(): IStartFactory {
        return IStartFactory { structure, chunkX, chunkZ, biome, mutableBoundingBox, reference, seed ->
            CryptStructureStart(structure, chunkX, chunkZ, biome, mutableBoundingBox, reference, seed)
        }
    }

    override fun hasStartAt(worldIn: IWorld, chunkGen: ChunkGenerator<*>, random: Random, xPos: Int, zPos: Int): Boolean {
        val frequency = getInteriorConfigs(xPos, zPos, chunkGen, stepNum = 4).map { it?.frequency ?: 0.0 }.maxOrNull() ?: 0.0
        if (random.nextDouble() >= frequency) {
            return false
        }

        val heights = getEdgeHeights(xPos, zPos, chunkGen, worldIn)
        val maxHeight = heights.maxOrNull()!!
        val minHeight = heights.minOrNull()!!
        if (maxHeight - minHeight > 5) {
            return false
        }
        return true
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