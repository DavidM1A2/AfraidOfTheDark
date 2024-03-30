package com.davidm1a2.afraidofthedark.common.world.structure.observatory

import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructureStart
import com.davidm1a2.afraidofthedark.common.world.structure.base.BooleanConfig
import com.davidm1a2.afraidofthedark.common.world.structure.base.SchematicStructurePiece
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.LevelHeightAccessor
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.feature.StructureFeature

class ObservatoryStructureStart(structure: StructureFeature<BooleanConfig>, chunkPos: ChunkPos, referenceIn: Int, seed: Long) :
    AOTDStructureStart<BooleanConfig>(structure, chunkPos, referenceIn, seed) {

    override fun init(generator: ChunkGenerator, xPos: Int, zPos: Int, levelHeightAccessor: LevelHeightAccessor) {
        val yPos = (feature as AOTDStructure<*>).getEdgeHeights(xPos, zPos, generator, levelHeightAccessor).minOrNull()!! - 1

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
        this.createBoundingBox()
    }
}