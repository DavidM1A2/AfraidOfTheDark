package com.davidm1a2.afraidofthedark.common.world.structure.voidchestbox

import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.util.registry.DynamicRegistries
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.NoFeatureConfig
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraft.world.gen.feature.structure.StructureStart
import net.minecraft.world.gen.feature.template.TemplateManager

class VoidChestBoxStructureStart(
    structure: Structure<NoFeatureConfig>,
    chunkX: Int,
    chunkZ: Int,
    boundsIn: MutableBoundingBox,
    referenceIn: Int,
    seed: Long
) : StructureStart<NoFeatureConfig>(structure, chunkX, chunkZ, boundsIn, referenceIn, seed) {
    override fun generatePieces(
        dynamicRegistries: DynamicRegistries,
        generator: ChunkGenerator,
        templateManagerIn: TemplateManager,
        centerChunkX: Int,
        centerChunkZ: Int,
        biomeIn: Biome,
        config: NoFeatureConfig
    ) {
        val startX = chunkX * 16
        val endX = startX + 15
        val multipleOf1000 = endX / 1000
        val posX = multipleOf1000 * 1000

        this.pieces.add(VoidChestBoxStructurePiece(posX))
        this.calculateBoundingBox()
    }
}