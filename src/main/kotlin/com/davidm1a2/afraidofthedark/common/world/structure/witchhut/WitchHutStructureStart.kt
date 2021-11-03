package com.davidm1a2.afraidofthedark.common.world.structure.witchhut

import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructureStart
import com.davidm1a2.afraidofthedark.common.world.structure.base.MultiplierConfig
import com.davidm1a2.afraidofthedark.common.world.structure.base.SchematicStructurePiece
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure

class WitchHutStructureStart(structure: Structure<MultiplierConfig>, chunkX: Int, chunkZ: Int, boundsIn: MutableBoundingBox, referenceIn: Int, seed: Long) :
    AOTDStructureStart<MultiplierConfig>(structure, chunkX, chunkZ, boundsIn, referenceIn, seed) {
    override fun init(generator: ChunkGenerator, xPos: Int, zPos: Int) {
        val yPos = (feature as AOTDStructure<*>).getEdgeHeights(xPos, zPos, generator).average().toInt() - 1

        this.pieces.add(
            SchematicStructurePiece(
                xPos - ModSchematics.WITCH_HUT.getWidth() / 2,
                yPos,
                zPos - ModSchematics.WITCH_HUT.getLength() / 2,
                random,
                ModSchematics.WITCH_HUT,
                ModLootTables.WITCH_HUT
            )
        )
        this.calculateBoundingBox()
    }
}