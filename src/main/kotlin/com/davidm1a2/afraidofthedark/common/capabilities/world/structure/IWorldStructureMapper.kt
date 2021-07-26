package com.davidm1a2.afraidofthedark.common.capabilities.world.structure

import net.minecraft.util.math.ChunkPos

interface IWorldStructureMapper {
    fun getStructureMapFor(chunkPos: ChunkPos): StructureMap

    fun setStructureMaps(structureMaps: List<Pair<ChunkPos, StructureMap>>)
    fun getStructureMaps(): List<Pair<ChunkPos, StructureMap>>
}