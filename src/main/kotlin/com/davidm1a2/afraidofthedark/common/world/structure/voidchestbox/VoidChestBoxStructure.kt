package com.davidm1a2.afraidofthedark.common.world.structure.voidchestbox

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModBiomes
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import net.minecraft.world.IWorld
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure.IStartFactory
import java.util.*

class VoidChestBoxStructure : AOTDStructure<VoidChestBoxConfig>({ VoidChestBoxConfig.deserialize() }, false) {
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

    override fun getStartFactory(): IStartFactory {
        return IStartFactory { structure, chunkX, chunkZ, biome, mutableBoundingBox, reference, seed ->
            VoidChestBoxStructureStart(structure, chunkX, chunkZ, biome, mutableBoundingBox, reference, seed)
        }
    }

    override fun hasStartAt(worldIn: IWorld, chunkGen: ChunkGenerator<*>, random: Random, xPos: Int, zPos: Int): Boolean {
        val xStart = xPos
        val xEnd = xStart + 15
        val amountOverMultipleOf1000 = xEnd % 1000

        return zPos == 0 && xStart >= 0 && amountOverMultipleOf1000 < 16
    }
}