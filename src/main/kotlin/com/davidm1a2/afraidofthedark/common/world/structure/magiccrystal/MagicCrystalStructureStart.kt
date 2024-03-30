package com.davidm1a2.afraidofthedark.common.world.structure.magiccrystal

import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructureStart
import com.davidm1a2.afraidofthedark.common.world.structure.base.MultiplierConfig
import com.davidm1a2.afraidofthedark.common.world.structure.base.SchematicStructurePiece
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.LevelHeightAccessor
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.feature.StructureFeature

class MagicCrystalStructureStart(structure: StructureFeature<MultiplierConfig>, chunkPos: ChunkPos, referenceIn: Int, seed: Long) :
    AOTDStructureStart<MultiplierConfig>(structure, chunkPos, referenceIn, seed) {

    override fun init(generator: ChunkGenerator, xPos: Int, zPos: Int, levelHeightAccessor: LevelHeightAccessor) {
        val yPos = (feature as AOTDStructure<*>).getEdgeHeights(xPos, zPos, generator, levelHeightAccessor).minOrNull()!!

        this.pieces.add(
            SchematicStructurePiece(
                xPos - ModSchematics.MAGIC_CRYSTAL.getWidth() / 2,
                yPos,
                zPos - ModSchematics.MAGIC_CRYSTAL.getLength() / 2,
                random,
                ModSchematics.MAGIC_CRYSTAL
            )
        )
        this.createBoundingBox()
    }
}