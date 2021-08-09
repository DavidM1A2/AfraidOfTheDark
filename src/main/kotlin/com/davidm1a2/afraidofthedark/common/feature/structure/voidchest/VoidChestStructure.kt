package com.davidm1a2.afraidofthedark.common.feature.structure.voidchest

import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.feature.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.feature.structure.base.BooleanConfig
import net.minecraft.world.biome.provider.BiomeProvider
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure.IStartFactory
import java.util.*

class VoidChestStructure : AOTDStructure<BooleanConfig>("void_chest", BooleanConfig.CODEC) {
    override fun getWidth(): Int {
        return ModSchematics.VOID_CHEST.getWidth().toInt()
    }

    override fun getLength(): Int {
        return ModSchematics.VOID_CHEST.getLength().toInt()
    }

    override fun getStartFactory(): IStartFactory<BooleanConfig> {
        return IStartFactory { structure, chunkX, chunkZ, mutableBoundingBox, reference, seed ->
            VoidChestStructureStart(structure, chunkX, chunkZ, mutableBoundingBox, reference, seed)
        }
    }

    override fun canFitAt(chunkGen: ChunkGenerator, biomeProvider: BiomeProvider, random: Random, xPos: Int, zPos: Int): Boolean {
        val chance = getOneInNValidChunks(100) * ModCommonConfiguration.voidChestMultiplier
        if (random.nextDouble() >= chance) {
            return false
        }

        val isNotSupported = getInteriorConfigEstimate(xPos, zPos, biomeProvider).any { !it.supported }
        if (isNotSupported) {
            return false
        }

        val heights = getEdgeHeights(xPos, zPos, chunkGen)
        val maxHeight = heights.maxOrNull()!!
        val minHeight = heights.minOrNull()!!
        if (maxHeight - minHeight > 8) {
            return false
        }
        return true
    }
}