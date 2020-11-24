package com.davidm1a2.afraidofthedark.common.world.structure.voidchest

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

class VoidChestStructureStart(structure: Structure<*>, chunkX: Int, chunkZ: Int, biomeIn: Biome, boundsIn: MutableBoundingBox, referenceIn: Int, seed: Long) :
    StructureStart(structure, chunkX, chunkZ, biomeIn, boundsIn, referenceIn, seed) {

    override fun init(generator: ChunkGenerator<*>, templateManagerIn: TemplateManager, centerChunkX: Int, centerChunkZ: Int, biomeIn: Biome) {
        val xPos = centerChunkX * 16
        val zPos = centerChunkZ * 16
        val yPos = (structure as AOTDStructure<*>).getEdgeHeights(xPos, zPos, generator).min()!!

        this.components.add(
            SchematicStructurePiece(
                xPos - ModSchematics.VOID_CHEST.getWidth() / 2,
                yPos - 7,
                zPos - ModSchematics.VOID_CHEST.getLength() / 2,
                rand,
                ModSchematics.VOID_CHEST,
                ModLootTables.VOID_CHEST
            )
        )
        this.recalculateStructureSize()
    }
}