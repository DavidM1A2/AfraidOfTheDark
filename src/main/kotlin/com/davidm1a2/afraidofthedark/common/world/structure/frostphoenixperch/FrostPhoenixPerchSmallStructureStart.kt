package com.davidm1a2.afraidofthedark.common.world.structure.frostphoenixperch

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
import kotlin.math.floor

class FrostPhoenixPerchSmallStructureStart(structure: StructureFeature<BooleanConfig>, chunkPos: ChunkPos, referenceIn: Int, seed: Long) :
    AOTDStructureStart<BooleanConfig>(structure, chunkPos, referenceIn, seed) {

    override fun init(generator: ChunkGenerator, xPos: Int, zPos: Int, levelHeightAccessor: LevelHeightAccessor) {
        val yPos = floor((feature as AOTDStructure<*>).getEdgeHeights(xPos, zPos, generator, levelHeightAccessor).average()).toInt() - 4

        this.pieces.add(
            SchematicStructurePiece(
                xPos - ModSchematics.FROST_PHOENIX_PERCH_SMALL.getWidth() / 2,
                yPos,
                zPos - ModSchematics.FROST_PHOENIX_PERCH_SMALL.getLength() / 2,
                random,
                ModSchematics.FROST_PHOENIX_PERCH_SMALL,
                ModLootTables.FROST_PHOENIX_PERCH_SMALL
            )
        )
        this.createBoundingBox()
    }
}