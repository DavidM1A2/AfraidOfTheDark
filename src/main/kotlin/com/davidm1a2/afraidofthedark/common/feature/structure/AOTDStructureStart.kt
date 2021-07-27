package com.davidm1a2.afraidofthedark.common.feature.structure

import com.davidm1a2.afraidofthedark.common.capabilities.getStructureMapper
import com.davidm1a2.afraidofthedark.common.feature.structure.base.getWorld
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraft.world.gen.feature.structure.StructureStart
import net.minecraft.world.gen.feature.template.TemplateManager

abstract class AOTDStructureStart(structure: Structure<*>, chunkX: Int, chunkZ: Int, boundsIn: MutableBoundingBox, referenceIn: Int, seed: Long) :
    StructureStart(structure, chunkX, chunkZ, boundsIn, referenceIn, seed) {
    override fun init(generator: ChunkGenerator<*>, templateManager: TemplateManager, chunkX: Int, chunkZ: Int, biome: Biome) {
        val world = generator.getWorld()
        val chunkPos = ChunkPos(chunkX, chunkZ)
        val structureMapper = world.getStructureMapper()
        val position = synchronized(structureMapper) {
            structureMapper.getStructureMapFor(chunkPos).getStructureCenterIn(chunkPos, structure)!!
        }
        init(generator, position.x, position.z)
    }

    abstract fun init(generator: ChunkGenerator<*>, xPos: Int, zPos: Int)
}