package com.davidm1a2.afraidofthedark.common.world.structure.voidchest

import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructureStart
import com.davidm1a2.afraidofthedark.common.world.structure.base.BooleanConfig
import com.davidm1a2.afraidofthedark.common.world.structure.base.SchematicStructurePiece
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure

class VoidChestStructureStart(structure: Structure<BooleanConfig>, chunkX: Int, chunkZ: Int, boundsIn: MutableBoundingBox, referenceIn: Int, seed: Long) :
    AOTDStructureStart<BooleanConfig>(structure, chunkX, chunkZ, boundsIn, referenceIn, seed) {

    override fun init(generator: ChunkGenerator, xPos: Int, zPos: Int) {
        val yPos = (feature as AOTDStructure<*>).getEdgeHeights(xPos, zPos, generator).minOrNull()!!

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
        this.calculateBoundingBox()
    }
}