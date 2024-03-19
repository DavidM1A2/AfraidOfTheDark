package com.davidm1a2.afraidofthedark.common.world.structure.base

import com.davidm1a2.afraidofthedark.common.capabilities.getStructureMapper
import net.minecraft.core.RegistryAccess
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.LevelHeightAccessor
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.feature.StructureFeature
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.structure.StructureStart
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks

abstract class AOTDStructureStart<T : FeatureConfiguration>(
    structure: StructureFeature<T>,
    chunkPos: ChunkPos,
    referenceIn: Int,
    seed: Long
) : StructureStart<T>(structure, chunkPos, referenceIn, seed) {

    override fun generatePieces(
        dynamicRegistries: RegistryAccess,
        generator: ChunkGenerator,
        templateManager: StructureManager,
        chunkPos: ChunkPos,
        biome: Biome,
        config: T,
        levelHeightAccessor: LevelHeightAccessor
    ) {
        val world = ServerLifecycleHooks.getCurrentServer()?.allLevels?.find { it.chunkSource.generator === generator }
            ?: throw IllegalStateException("Could not determine which world chunk generator $generator belongs to")
        val structureMapper = world.getStructureMapper()
        val position = synchronized(structureMapper) {
            structureMapper.getStructureMapFor(chunkPos).getStructureCenterIn(chunkPos, feature)!!
        }
        init(generator, position.x, position.z)
    }

    abstract fun init(generator: ChunkGenerator, xPos: Int, zPos: Int)
}