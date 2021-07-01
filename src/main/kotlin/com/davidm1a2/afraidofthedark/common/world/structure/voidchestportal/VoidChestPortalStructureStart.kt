package com.davidm1a2.afraidofthedark.common.world.structure.voidchestportal

import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.SchematicStructurePiece
import net.minecraft.util.Direction
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraft.world.gen.feature.structure.StructureStart
import net.minecraft.world.gen.feature.template.TemplateManager

class VoidChestPortalStructureStart(
    structure: Structure<*>,
    chunkX: Int,
    chunkZ: Int,
    biomeIn: Biome,
    boundsIn: MutableBoundingBox,
    referenceIn: Int,
    seed: Long
) :
    StructureStart(structure, chunkX, chunkZ, biomeIn, boundsIn, referenceIn, seed) {

    override fun init(generator: ChunkGenerator<*>, templateManagerIn: TemplateManager, centerChunkX: Int, centerChunkZ: Int, biomeIn: Biome) {
        val startX = chunkPosX * 16
        val endX = startX + 15
        val multipleOf1000 = endX / 1000
        val posX = multipleOf1000 * 1000 + 16

        this.components.add(
            SchematicStructurePiece(
                posX + 4,
                100,
                -2,
                rand,
                ModSchematics.VOID_CHEST_PORTAL,
                facing = Direction.NORTH
            )
        )
        this.recalculateStructureSize()
    }
}