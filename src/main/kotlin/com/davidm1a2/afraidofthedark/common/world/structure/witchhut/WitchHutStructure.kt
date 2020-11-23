package com.davidm1a2.afraidofthedark.common.world.structure.witchhut

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

class WitchHutStructure : AOTDStructure<WitchHutConfig>({ WitchHutConfig.deserialize(it) }) {
    override fun getStructureName(): String {
        return "${Constants.MOD_ID}:witch_hut"
    }

    override fun getWidth(): Int {
        return ModSchematics.WITCH_HUT.getWidth().toInt()
    }

    override fun getLength(): Int {
        return ModSchematics.WITCH_HUT.getLength().toInt()
    }

    override fun setupStructureIn(biome: Biome) {
        if (biome == ModBiomes.EERIE_FOREST) {
            addToBiome(biome, WitchHutConfig(0.03 * ModCommonConfiguration.witchHutMultiplier))
        } else {
            addToBiome(biome, WitchHutConfig(0.0))
        }
    }

    override fun getStartFactory(): IStartFactory {
        return IStartFactory { structure, chunkX, chunkZ, biome, mutableBoundingBox, reference, seed ->
            WitchHutStructureStart(structure, chunkX, chunkZ, biome, mutableBoundingBox, reference, seed)
        }
    }

    override fun hasStartAt(worldIn: IWorld, chunkGen: ChunkGenerator<*>, random: Random, xPos: Int, zPos: Int): Boolean {
        val frequency = getInteriorConfigs(xPos, zPos, chunkGen, stepNum = 2).map { it?.frequency ?: 0.0 }.min() ?: 0.0
        if (random.nextDouble() >= frequency) {
            return false
        }

        val heights = getEdgeHeights(xPos, zPos, chunkGen, worldIn)
        val maxHeight = heights.max()!!
        val minHeight = heights.min()!!
        if (maxHeight - minHeight > 3) {
            return false
        }
        return true
    }
}