package com.davidm1a2.afraidofthedark.common.world.structure.witchhut

import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.world.structure.base.SchematicStructurePiece
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraft.world.gen.feature.structure.StructureStart
import net.minecraft.world.gen.feature.template.TemplateManager

class WitchHutStructureStart(structure: Structure<*>, chunkX: Int, chunkZ: Int, boundsIn: MutableBoundingBox, referenceIn: Int, seed: Long) :
    StructureStart(structure, chunkX, chunkZ, boundsIn, referenceIn, seed) {

    override fun init(generator: ChunkGenerator<*>, templateManagerIn: TemplateManager, centerChunkX: Int, centerChunkZ: Int, biomeIn: Biome) {
        val xPos = centerChunkX * 16
        val zPos = centerChunkZ * 16
        val yPos = (structure as AOTDStructure<*>).getEdgeHeights(xPos, zPos, generator).average().toInt() - 1

        this.components.add(
            SchematicStructurePiece(
                xPos - ModSchematics.WITCH_HUT.getWidth() / 2,
                yPos,
                zPos - ModSchematics.WITCH_HUT.getLength() / 2,
                rand,
                ModSchematics.WITCH_HUT,
                ModLootTables.WITCH_HUT
            )
        )
        this.recalculateStructureSize()
    }
}