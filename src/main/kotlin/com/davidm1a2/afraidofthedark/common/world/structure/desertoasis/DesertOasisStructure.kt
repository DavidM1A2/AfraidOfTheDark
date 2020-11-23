package com.davidm1a2.afraidofthedark.common.world.structure.desertoasis

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import net.minecraft.world.IWorld
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure.IStartFactory
import java.util.*
import kotlin.math.max

class DesertOasisStructure : AOTDStructure<DesertOasisConfig>({ DesertOasisConfig.deserialize(it) }) {
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

    override fun getStartFactory(): IStartFactory {
        return IStartFactory { structure, chunkX, chunkZ, biome, mutableBoundingBox, reference, seed ->
            DesertOasisStructureStart(structure, chunkX, chunkZ, biome, mutableBoundingBox, reference, seed)
        }
    }

    override fun hasStartAt(worldIn: IWorld, chunkGen: ChunkGenerator<*>, random: Random, xPos: Int, zPos: Int): Boolean {
        val frequency = getInteriorConfigs(xPos, zPos, chunkGen, stepNum = 16).groupingBy { it!!.supported }.eachCount()
        val numValid = frequency[true] ?: 0
        val numInvalid = max(frequency[false] ?: 1, 1)
        val percentDesertTiles = numValid / (numValid + numInvalid).toDouble()

        // 70% valid tiles required, .5% chance to spawn
        if (percentDesertTiles < 0.7) {
            return false
        }
        if (random.nextDouble() >= 0.005 * ModCommonConfiguration.desertOasisMultiplier) {
            return false
        }
        return true
    }

    companion object {
        private val VALID_BIOME_CATEGORIES = setOf(
            Biome.Category.DESERT,
            Biome.Category.RIVER
        )
    }
}