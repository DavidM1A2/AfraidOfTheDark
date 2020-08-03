package com.davidm1a2.afraidofthedark.common.world.structure.nightmareisland

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModBiomes
import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import net.minecraft.util.SharedSeedRandom
import net.minecraft.world.IWorld
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.IChunkGenerator
import net.minecraft.world.gen.feature.structure.StructureStart

class NightmareIslandStructure : AOTDStructure<NightmareIslandConfig>() {
    override fun getStructureName(): String {
        return "${Constants.MOD_ID}:nightmare_island"
    }

    override fun getWidth(): Int {
        return ModSchematics.NIGHTMARE_ISLAND.getWidth().toInt()
    }

    override fun getLength(): Int {
        return ModSchematics.NIGHTMARE_ISLAND.getLength().toInt()
    }

    override fun setupStructureIn(biome: Biome) {
        if (biome == ModBiomes.NIGHTMARE) {
            addToBiome(biome, NightmareIslandConfig())
        }
    }

    override fun isEnabledIn(worldIn: IWorld): Boolean {
        return worldIn.dimension.type == ModDimensions.NIGHTMARE_TYPE
    }

    override fun hasStartAt(worldIn: IWorld, chunkGen: IChunkGenerator<*>, rand: SharedSeedRandom, centerChunkX: Int, centerChunkZ: Int): Boolean {
        val xStart = centerChunkX * 16
        val xEnd = xStart + 15
        val amountOverMultipleOf1000 = xEnd % 1000

        return centerChunkZ == 0 && xStart >= 0 && amountOverMultipleOf1000 < 16
    }

    override fun makeStart(worldIn: IWorld, generator: IChunkGenerator<*>, random: SharedSeedRandom, centerChunkX: Int, centerChunkZ: Int): StructureStart {
        return NightmareIslandStructureStart(worldIn, centerChunkX, ModBiomes.VOID_CHEST, random, generator.seed)
    }
}