package com.davidm1a2.afraidofthedark.common.world.structure.voidchest

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.world.structure.base.BooleanConfig
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biomes
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure.IStartFactory
import java.util.*

class VoidChestStructure : AOTDStructure<BooleanConfig>({ BooleanConfig.deserialize(it) }) {
    override fun getStructureName(): String {
        return "${Constants.MOD_ID}:void_chest"
    }

    override fun getWidth(): Int {
        return ModSchematics.VOID_CHEST.getWidth().toInt()
    }

    override fun getLength(): Int {
        return ModSchematics.VOID_CHEST.getLength().toInt()
    }

    override fun setupStructureIn(biome: Biome) {
        addToBiome(biome, BooleanConfig(biome in COMPATIBLE_BIOMES))
    }

    override fun getStartFactory(): IStartFactory {
        return IStartFactory { structure, chunkX, chunkZ, mutableBoundingBox, reference, seed ->
            VoidChestStructureStart(structure, chunkX, chunkZ, mutableBoundingBox, reference, seed)
        }
    }

    override fun hasStartAt(worldIn: World, chunkGen: ChunkGenerator<*>, random: Random, missCount: Int, xPos: Int, zPos: Int): Boolean {
        val isNotSupported = getInteriorConfigEstimate(xPos, zPos, chunkGen).any { !it.supported }
        if (isNotSupported) {
            return false
        }

        // chance = voidChestMultiplier * CHANCE_QUARTIC_COEFFICIENT * missCount^4
        val chance = ModCommonConfiguration.voidChestMultiplier * (CHANCE_QUARTIC_COEFFICIENT * missCount).powOptimized(4)
        if (random.nextDouble() >= chance) {
            return false
        }

        val heights = getEdgeHeights(xPos, zPos, chunkGen, worldIn)
        val maxHeight = heights.maxOrNull()!!
        val minHeight = heights.minOrNull()!!
        if (maxHeight - minHeight > 8) {
            return false
        }
        return true
    }

    companion object {
        // 4th root of 0.00000000000009
        private const val CHANCE_QUARTIC_COEFFICIENT = 0.0005477226

        // A set of compatible biomes
        private val COMPATIBLE_BIOMES = setOf(
            Biomes.SNOWY_BEACH,
            Biomes.SNOWY_TAIGA,
            Biomes.SNOWY_TUNDRA,
            Biomes.ICE_SPIKES
        )
    }
}