package com.davidm1a2.afraidofthedark.common.feature.structure.witchhut

import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.feature.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.feature.structure.base.MultiplierConfig
import net.minecraft.world.biome.provider.BiomeProvider
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure.IStartFactory
import java.util.*

class WitchHutStructure : AOTDStructure<MultiplierConfig>("witch_hut", MultiplierConfig.CODEC) {
    override fun getWidth(): Int {
        return ModSchematics.WITCH_HUT.getWidth().toInt()
    }

    override fun getLength(): Int {
        return ModSchematics.WITCH_HUT.getLength().toInt()
    }

    override fun getStartFactory(): IStartFactory<MultiplierConfig> {
        return IStartFactory { structure, chunkX, chunkZ, mutableBoundingBox, reference, seed ->
            WitchHutStructureStart(structure, chunkX, chunkZ, mutableBoundingBox, reference, seed)
        }
    }

    override fun canFitAt(chunkGen: ChunkGenerator, biomeProvider: BiomeProvider, random: Random, xPos: Int, zPos: Int): Boolean {
        val biomeMultiplier = getInteriorConfigEstimate(xPos, zPos, biomeProvider, MISSING_CONFIG)
            .map { it.multiplier }
            .minOrNull() ?: 0

        val chance = getOneInNValidChunks(50) * ModCommonConfiguration.witchHutMultiplier * biomeMultiplier
        if (random.nextDouble() >= chance) {
            return false
        }

        val heights = getEdgeHeights(xPos, zPos, chunkGen)
        val maxHeight = heights.maxOrNull()!!
        val minHeight = heights.minOrNull()!!
        if (maxHeight - minHeight > 3) {
            return false
        }
        return true
    }

    companion object {
        private val MISSING_CONFIG = MultiplierConfig(0)
    }
}