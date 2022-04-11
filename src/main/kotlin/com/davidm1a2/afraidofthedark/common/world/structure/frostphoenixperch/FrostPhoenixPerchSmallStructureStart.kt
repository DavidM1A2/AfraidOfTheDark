package com.davidm1a2.afraidofthedark.common.world.structure.frostphoenixperch

import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructureStart
import com.davidm1a2.afraidofthedark.common.world.structure.base.BooleanConfig
import com.davidm1a2.afraidofthedark.common.world.structure.base.SchematicStructurePiece
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure
import kotlin.math.floor

class FrostPhoenixPerchSmallStructureStart(structure: Structure<BooleanConfig>, chunkX: Int, chunkZ: Int, boundsIn: MutableBoundingBox, referenceIn: Int, seed: Long) :
    AOTDStructureStart<BooleanConfig>(structure, chunkX, chunkZ, boundsIn, referenceIn, seed) {

    override fun init(generator: ChunkGenerator, xPos: Int, zPos: Int) {
        val yPos = floor((feature as AOTDStructure<*>).getEdgeHeights(xPos, zPos, generator).average()).toInt() - 4

        this.pieces.add(
            SchematicStructurePiece(
                xPos - ModSchematics.FROST_PHOENIX_PERCH_SMALL.getWidth() / 2,
                yPos,
                zPos - ModSchematics.FROST_PHOENIX_PERCH_SMALL.getLength() / 2,
                random,
                ModSchematics.FROST_PHOENIX_PERCH_SMALL
            )
        )
        this.calculateBoundingBox()
    }
}