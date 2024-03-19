package com.davidm1a2.afraidofthedark.common.capabilities.world.structure

import net.minecraft.world.level.ChunkPos

class WorldStructureMapper : IWorldStructureMapper {
    private val structureMaps = mutableMapOf<StructureGridPos, StructureMap>()

    override fun getStructureMapFor(chunkPos: ChunkPos): StructureMap {
        val gridPos = StructureGridSize.LARGEST_GRID_SIZE.toAbsoluteGridPos(chunkPos)
        return structureMaps.computeIfAbsent(gridPos) { StructureMap() }
    }

    override fun setStructureMaps(structureMaps: List<Pair<StructureGridPos, StructureMap>>) {
        this.structureMaps.clear()
        structureMaps.forEach {
            this.structureMaps[it.first] = it.second
        }
    }

    override fun getStructureMaps(): List<Pair<StructureGridPos, StructureMap>> {
        return structureMaps.toList()
    }
}