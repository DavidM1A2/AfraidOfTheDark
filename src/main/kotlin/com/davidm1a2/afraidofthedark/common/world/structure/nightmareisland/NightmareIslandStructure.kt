package com.davidm1a2.afraidofthedark.common.world.structure.nightmareisland

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModBiomes
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import net.minecraft.world.IWorld
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure.IStartFactory
import java.util.*
import kotlin.math.round

class NightmareIslandStructure : AOTDStructure<NightmareIslandConfig>({ NightmareIslandConfig.deserialize() }, false) {
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

    override fun getStartFactory(): IStartFactory {
        return IStartFactory { structure, chunkX, chunkZ, biome, mutableBoundingBox, reference, seed ->
            NightmareIslandStructureStart(structure, chunkX, chunkZ, biome, mutableBoundingBox, reference, seed)
        }
    }

    override fun hasStartAt(worldIn: IWorld, chunkGen: ChunkGenerator<*>, random: Random, xPos: Int, zPos: Int): Boolean {
        val xStart = xPos
        val xEnd = xStart + 15
        val amountOverMultipleOf1000 = xEnd % 1000

        val halfWidth = getWidth() / 2
        val halfLengthToClosest16 = (16 * round((getLength().toDouble() / 2) / 16)).toInt()

        return zPos == halfLengthToClosest16 && amountOverMultipleOf1000 >= halfWidth && amountOverMultipleOf1000 < halfWidth + 16
    }
}