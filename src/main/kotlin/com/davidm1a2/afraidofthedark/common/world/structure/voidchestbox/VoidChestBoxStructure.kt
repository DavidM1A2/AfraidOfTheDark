package com.davidm1a2.afraidofthedark.common.world.structure.voidchestbox

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModBiomes
import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import net.minecraft.util.SharedSeedRandom
import net.minecraft.world.IWorld
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.IChunkGenerator
import net.minecraft.world.gen.feature.structure.StructureStart

class VoidChestBoxStructure : AOTDStructure<VoidChestBoxConfig>() {
    override fun getStructureName(): String {
        return "${Constants.MOD_ID}:void_chest_box"
    }

    override fun getWidth(): Int {
        return 48
    }

    override fun getLength(): Int {
        return 48
    }

    override fun setupStructureIn(biome: Biome) {
        if (biome == ModBiomes.VOID_CHEST) {
            addToBiome(biome, VoidChestBoxConfig())
        }
    }

    override fun isEnabledIn(worldIn: IWorld): Boolean {
        return worldIn.dimension.type == ModDimensions.VOID_CHEST_TYPE
    }

    override fun hasStartAt(worldIn: IWorld, chunkGen: IChunkGenerator<*>, rand: SharedSeedRandom, centerChunkX: Int, centerChunkZ: Int): Boolean {
        val xStart = centerChunkX * 16
        val xEnd = xStart + 15
        val amountOverMultipleOf1000 = xEnd % 1000

        return centerChunkZ == 0 && xStart >= 0 && amountOverMultipleOf1000 < 16
    }

    override fun makeStart(worldIn: IWorld, generator: IChunkGenerator<*>, random: SharedSeedRandom, centerChunkX: Int, centerChunkZ: Int): StructureStart {
        return VoidChestBoxStructureStart(worldIn, centerChunkX, ModBiomes.VOID_CHEST, random, generator.seed)
    }
}