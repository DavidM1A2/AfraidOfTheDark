package com.davidm1a2.afraidofthedark.common.feature.structure.desertoasis

import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.feature.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.feature.structure.base.BooleanConfig
import net.minecraft.world.biome.provider.BiomeProvider
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure.IStartFactory
import java.util.*

class DesertOasisStructure : AOTDStructure<BooleanConfig>("desert_oasis", BooleanConfig.CODEC) {
    override fun getWidth(): Int {
        return ModSchematics.DESERT_OASIS.getWidth().toInt()
    }

    override fun getLength(): Int {
        return ModSchematics.DESERT_OASIS.getLength().toInt()
    }

    override fun getStartFactory(): IStartFactory<BooleanConfig> {
        return IStartFactory { structure, chunkX, chunkZ, mutableBoundingBox, reference, seed ->
            DesertOasisStructureStart(structure, chunkX, chunkZ, mutableBoundingBox, reference, seed)
        }
    }

    override fun canFitAt(chunkGen: ChunkGenerator, biomeProvider: BiomeProvider, random: Random, xPos: Int, zPos: Int): Boolean {
        val numValidTiles = getInteriorConfigEstimate(xPos, zPos, biomeProvider).count { it.supported }
        // 66% desert tiles required (there's 9 checked, so 6+ must be valid)
        if (numValidTiles < 6) {
            return false
        }

        val chance = getOneInNValidChunks(200) * ModCommonConfiguration.desertOasisMultiplier
        if (random.nextDouble() >= chance) {
            return false
        }
        return true
    }
}