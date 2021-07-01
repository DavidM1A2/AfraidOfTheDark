package com.davidm1a2.afraidofthedark.common.world.structure.desertoasis

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.world.structure.base.BooleanConfig
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure.IStartFactory
import java.util.*

class DesertOasisStructure : AOTDStructure<BooleanConfig>({ BooleanConfig.deserialize(it) }) {
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
            addToBiome(biome, BooleanConfig(true))
        } else {
            addToBiome(biome, BooleanConfig(false))
        }
    }

    override fun getStartFactory(): IStartFactory {
        return IStartFactory { structure, chunkX, chunkZ, biome, mutableBoundingBox, reference, seed ->
            DesertOasisStructureStart(structure, chunkX, chunkZ, biome, mutableBoundingBox, reference, seed)
        }
    }

    override fun hasStartAt(worldIn: World, chunkGen: ChunkGenerator<*>, random: Random, missCount: Int, xPos: Int, zPos: Int): Boolean {
        val numValidTiles = getInteriorConfigEstimate(xPos, zPos, chunkGen).count { it.supported }
        // 66% desert tiles required (there's 9 checked, so 6+ must be valid)
        if (numValidTiles < 6) {
            return false
        }

        // chance = desertOasisMultiplier * CHANCE_QUARTIC_COEFFICIENT * missCount^4
        val chance = ModCommonConfiguration.desertOasisMultiplier * (CHANCE_QUARTIC_COEFFICIENT * missCount).powOptimized(4)
        if (random.nextDouble() >= chance) {
            return false
        }
        return true
    }

    companion object {
        // 4th root of 0.0000000000000005
        private const val CHANCE_QUARTIC_COEFFICIENT = 0.0001495349

        private val VALID_BIOME_CATEGORIES = setOf(
            Biome.Category.DESERT,
            Biome.Category.RIVER
        )
    }
}