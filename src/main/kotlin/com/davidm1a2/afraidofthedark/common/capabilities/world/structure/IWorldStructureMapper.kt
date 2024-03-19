package com.davidm1a2.afraidofthedark.common.capabilities.world.structure

import net.minecraft.world.level.ChunkPos

interface IWorldStructureMapper {
    fun getStructureMapFor(chunkPos: ChunkPos): StructureMap

    fun setStructureMaps(structureMaps: List<Pair<StructureGridPos, StructureMap>>)
    fun getStructureMaps(): List<Pair<StructureGridPos, StructureMap>>
}