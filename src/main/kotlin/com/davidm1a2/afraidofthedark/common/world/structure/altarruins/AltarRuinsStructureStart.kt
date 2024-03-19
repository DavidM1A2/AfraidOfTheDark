package com.davidm1a2.afraidofthedark.common.world.structure.altarruins

import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructureStart
import com.davidm1a2.afraidofthedark.common.world.structure.base.MultiplierConfig
import com.davidm1a2.afraidofthedark.common.world.structure.base.SchematicStructurePiece
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.LevelHeightAccessor
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.feature.StructureFeature

class AltarRuinsStructureStart(structure: StructureFeature<MultiplierConfig>, chunkPos: ChunkPos, referenceIn: Int, seed: Long) :
    AOTDStructureStart<MultiplierConfig>(structure, chunkPos, referenceIn, seed) {

    override fun init(generator: ChunkGenerator, xPos: Int, zPos: Int, levelHeightAccessor: LevelHeightAccessor) {
        val yPos = (feature as AOTDStructure<*>).getEdgeHeights(xPos, zPos, generator, levelHeightAccessor).minOrNull()!! - 1

        this.pieces.add(
            SchematicStructurePiece(
                xPos - ModSchematics.ALTAR_RUINS.getWidth() / 2,
                yPos,
                zPos - ModSchematics.ALTAR_RUINS.getLength() / 2,
                random,
                ModSchematics.ALTAR_RUINS
            )
        )
        this.createBoundingBox()
    }
}