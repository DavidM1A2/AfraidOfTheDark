package com.davidm1a2.afraidofthedark.common.feature.structure.observatory

import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.feature.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.feature.structure.base.AOTDStructureStart
import com.davidm1a2.afraidofthedark.common.feature.structure.base.BooleanConfig
import com.davidm1a2.afraidofthedark.common.feature.structure.base.SchematicStructurePiece
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure

class ObservatoryStructureStart(structure: Structure<BooleanConfig>, chunkX: Int, chunkZ: Int, boundsIn: MutableBoundingBox, referenceIn: Int, seed: Long) :
    AOTDStructureStart<BooleanConfig>(structure, chunkX, chunkZ, boundsIn, referenceIn, seed) {

    override fun init(generator: ChunkGenerator, xPos: Int, zPos: Int) {
        val yPos = (feature as AOTDStructure<*>).getEdgeHeights(xPos, zPos, generator).minOrNull()!! - 1

        this.pieces.add(
            SchematicStructurePiece(
                xPos - ModSchematics.OBSERVATORY.getWidth() / 2,
                yPos,
                zPos - ModSchematics.OBSERVATORY.getLength() / 2,
                random,
                ModSchematics.OBSERVATORY,
                ModLootTables.OBSERVATORY
            )
        )
        this.calculateBoundingBox()
    }
}