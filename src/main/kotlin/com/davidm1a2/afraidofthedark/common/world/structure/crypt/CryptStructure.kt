package com.davidm1a2.afraidofthedark.common.world.structure.crypt

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModBiomes
import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.world.structure.base.MultiplierConfig
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure.IStartFactory
import java.util.*

class CryptStructure : AOTDStructure<MultiplierConfig>({ MultiplierConfig.deserialize(it) }) {
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
                addToBiome(biome, MultiplierConfig(10))
            } else {
                addToBiome(biome, MultiplierConfig(1))
            }
        } else {
            addToBiome(biome, MultiplierConfig(0))
        }
    }

    override fun getStartFactory(): IStartFactory {
        return IStartFactory { structure, chunkX, chunkZ, biome, mutableBoundingBox, reference, seed ->
            CryptStructureStart(structure, chunkX, chunkZ, biome, mutableBoundingBox, reference, seed)
        }
    }

    override fun hasStartAt(worldIn: World, chunkGen: ChunkGenerator<*>, random: Random, missCount: Int, xPos: Int, zPos: Int): Boolean {
        val biomeMultiplier = getInteriorConfigEstimate(xPos, zPos, chunkGen).map { it.multiplier }.minOrNull() ?: 0
        // chance = cryptMultiplier * biomeMultiplier * CHANCE_QUARTIC_COEFFICIENT * missCount^4
        val chance = ModCommonConfiguration.cryptMultiplier * biomeMultiplier * (CHANCE_QUARTIC_COEFFICIENT * missCount).powOptimized(4)
        if (random.nextDouble() >= chance) {
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
        // 4th root of 0.00000000000003
        private const val CHANCE_QUARTIC_COEFFICIENT = 0.0004161791
        private val INCOMPATIBLE_BIOMES = setOf(
            Biome.Category.BEACH,
            Biome.Category.EXTREME_HILLS,
            Biome.Category.ICY,
            Biome.Category.NETHER,
            Biome.Category.OCEAN,
            Biome.Category.RIVER,
            Biome.Category.THEEND,
            Biome.Category.NONE,
            Biome.Category.MUSHROOM,
            Biome.Category.SWAMP
        )
    }
}