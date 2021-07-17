package com.davidm1a2.afraidofthedark.common.feature.structure.voidchestbox

import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraft.world.gen.feature.structure.StructureStart
import net.minecraft.world.gen.feature.template.TemplateManager

class VoidChestBoxStructureStart(
    structure: Structure<*>,
    chunkX: Int,
    chunkZ: Int,
    boundsIn: MutableBoundingBox,
    referenceIn: Int,
    seed: Long
) :
    StructureStart(structure, chunkX, chunkZ, boundsIn, referenceIn, seed) {

    override fun init(generator: ChunkGenerator<*>, templateManagerIn: TemplateManager, centerChunkX: Int, centerChunkZ: Int, biomeIn: Biome) {
        val startX = chunkPosX * 16
        val endX = startX + 15
        val multipleOf1000 = endX / 1000
        val posX = multipleOf1000 * 1000

        this.components.add(VoidChestBoxStructurePiece(posX))
        this.recalculateStructureSize()
    }
}