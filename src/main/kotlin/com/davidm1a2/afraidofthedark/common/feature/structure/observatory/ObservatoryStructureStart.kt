package com.davidm1a2.afraidofthedark.common.feature.structure.observatory

import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.feature.structure.AOTDStructureStart
import com.davidm1a2.afraidofthedark.common.feature.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.feature.structure.base.SchematicStructurePiece
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure

class ObservatoryStructureStart(structure: Structure<*>, chunkX: Int, chunkZ: Int, boundsIn: MutableBoundingBox, referenceIn: Int, seed: Long) :
    AOTDStructureStart(structure, chunkX, chunkZ, boundsIn, referenceIn, seed) {

    override fun init(generator: ChunkGenerator<*>, xPos: Int, zPos: Int) {
        val yPos = (structure as AOTDStructure<*>).getEdgeHeights(xPos, zPos, generator).minOrNull()!! - 1

        this.components.add(
            SchematicStructurePiece(
                xPos - ModSchematics.OBSERVATORY.getWidth() / 2,
                yPos,
                zPos - ModSchematics.OBSERVATORY.getLength() / 2,
                rand,
                ModSchematics.OBSERVATORY,
                ModLootTables.OBSERVATORY
            )
        )
        this.recalculateStructureSize()
    }
}