package com.davidm1a2.afraidofthedark.common.world.structure.voidchest

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

class VoidChestStructureStart(structure: StructureFeature<BooleanConfig>, chunkPos: ChunkPos, referenceIn: Int, seed: Long) :
    AOTDStructureStart<BooleanConfig>(structure, chunkPos, referenceIn, seed) {

    override fun init(generator: ChunkGenerator, xPos: Int, zPos: Int, levelHeightAccessor: LevelHeightAccessor) {
        val yPos = (feature as AOTDStructure<*>).getEdgeHeights(xPos, zPos, generator, levelHeightAccessor).minOrNull()!!

        this.pieces.add(
            SchematicStructurePiece(
                xPos - ModSchematics.VOID_CHEST.getWidth() / 2,
                yPos - 7,
                zPos - ModSchematics.VOID_CHEST.getLength() / 2,
                random,
                ModSchematics.VOID_CHEST,
                ModLootTables.VOID_CHEST
            )
        )
        this.createBoundingBox()
    }
}