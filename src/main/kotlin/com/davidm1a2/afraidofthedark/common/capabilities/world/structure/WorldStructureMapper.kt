package com.davidm1a2.afraidofthedark.common.capabilities.world.structure

import net.minecraft.util.math.ChunkPos

class WorldStructureMapper : IWorldStructureMapper {
    private val structureMaps = mutableMapOf<ChunkPos, StructureMap>()

    override fun getStructureMapFor(chunkPos: ChunkPos): StructureMap {
        val gridPos = StructureGridSize.SIZE_256.toAbsoluteGridPos(chunkPos)
        return structureMaps.computeIfAbsent(gridPos) { StructureMap() }
    }

    override fun setStructureMaps(structureMaps: List<Pair<ChunkPos, StructureMap>>) {
        this.structureMaps.clear()
        structureMaps.forEach {
            this.structureMaps[it.first] = it.second
        }
    }

    override fun getStructureMaps(): List<Pair<ChunkPos, StructureMap>> {
        return structureMaps.toList()
    }
}