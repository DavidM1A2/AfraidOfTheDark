package com.davidm1a2.afraidofthedark.common.feature.structure.base

import com.davidm1a2.afraidofthedark.common.capabilities.getStructureMapper
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.util.registry.DynamicRegistries
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.IFeatureConfig
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraft.world.gen.feature.structure.StructureStart
import net.minecraft.world.gen.feature.template.TemplateManager
import net.minecraftforge.fml.server.ServerLifecycleHooks

abstract class AOTDStructureStart<T : IFeatureConfig>(
    structure: Structure<T>,
    chunkX: Int,
    chunkZ: Int,
    boundsIn: MutableBoundingBox,
    referenceIn: Int,
    seed: Long
) : StructureStart<T>(structure, chunkX, chunkZ, boundsIn, referenceIn, seed) {
    override fun generatePieces(
        dynamicRegistries: DynamicRegistries,
        generator: ChunkGenerator,
        templateManager: TemplateManager,
        chunkX: Int,
        chunkZ: Int,
        biome: Biome,
        config: T
    ) {
        val world = ServerLifecycleHooks.getCurrentServer()?.allLevels?.find { it.chunkSource.generator === generator }
            ?: throw IllegalStateException("Could not determine which world chunk generator $generator belongs to")
        val chunkPos = ChunkPos(chunkX, chunkZ)
        val structureMapper = world.getStructureMapper()
        val position = synchronized(structureMapper) {
            structureMapper.getStructureMapFor(chunkPos).getStructureCenterIn(chunkPos, feature)!!
        }
        init(generator, position.x, position.z)
    }

    abstract fun init(generator: ChunkGenerator, xPos: Int, zPos: Int)
}