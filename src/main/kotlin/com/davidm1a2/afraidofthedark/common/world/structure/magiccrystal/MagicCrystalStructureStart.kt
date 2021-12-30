package com.davidm1a2.afraidofthedark.common.world.structure.magiccrystal

import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructureStart
import com.davidm1a2.afraidofthedark.common.world.structure.base.MultiplierConfig
import com.davidm1a2.afraidofthedark.common.world.structure.base.SchematicStructurePiece
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure

class MagicCrystalStructureStart(structure: Structure<MultiplierConfig>, chunkX: Int, chunkZ: Int, boundsIn: MutableBoundingBox, referenceIn: Int, seed: Long) :
    AOTDStructureStart<MultiplierConfig>(structure, chunkX, chunkZ, boundsIn, referenceIn, seed) {

    override fun init(generator: ChunkGenerator, xPos: Int, zPos: Int) {
        val yPos = (feature as AOTDStructure<*>).getEdgeHeights(xPos, zPos, generator).minOrNull()!!

        this.pieces.add(
            SchematicStructurePiece(
                xPos - ModSchematics.MAGIC_CRYSTAL.getWidth() / 2,
                yPos,
                zPos - ModSchematics.MAGIC_CRYSTAL.getLength() / 2,
                random,
                ModSchematics.MAGIC_CRYSTAL
            )
        )
        this.calculateBoundingBox()
    }
}