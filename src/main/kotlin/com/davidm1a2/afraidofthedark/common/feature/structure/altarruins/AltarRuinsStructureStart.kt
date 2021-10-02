package com.davidm1a2.afraidofthedark.common.feature.structure.altarruins

import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.feature.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.feature.structure.base.AOTDStructureStart
import com.davidm1a2.afraidofthedark.common.feature.structure.base.MultiplierConfig
import com.davidm1a2.afraidofthedark.common.feature.structure.base.SchematicStructurePiece
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure

class AltarRuinsStructureStart(structure: Structure<MultiplierConfig>, chunkX: Int, chunkZ: Int, boundsIn: MutableBoundingBox, referenceIn: Int, seed: Long) :
    AOTDStructureStart<MultiplierConfig>(structure, chunkX, chunkZ, boundsIn, referenceIn, seed) {

    override fun init(generator: ChunkGenerator, xPos: Int, zPos: Int) {
        val yPos = (feature as AOTDStructure<*>).getEdgeHeights(xPos, zPos, generator).minOrNull()!! - 1

        this.pieces.add(
            SchematicStructurePiece(
                xPos - ModSchematics.ALTAR_RUINS.getWidth() / 2,
                yPos,
                zPos - ModSchematics.ALTAR_RUINS.getLength() / 2,
                random,
                ModSchematics.ALTAR_RUINS
            )
        )
        this.calculateBoundingBox()
    }
}