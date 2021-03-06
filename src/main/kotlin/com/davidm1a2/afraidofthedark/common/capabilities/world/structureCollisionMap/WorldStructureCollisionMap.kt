package com.davidm1a2.afraidofthedark.common.capabilities.world.structureCollisionMap

import net.minecraft.world.gen.feature.structure.StructureStart

class WorldStructureCollisionMap : IWorldStructureCollisionMap {
    private val collisionMap = BoundingBoxTree()

    override fun insertStructure(start: StructureStart) {
        collisionMap.insert(start.boundingBox)
    }

    override fun isStructureBlocked(start: StructureStart): Boolean {
        return collisionMap.intersects(start.boundingBox)
    }

    override fun getBoundingBoxTree(): BoundingBoxTree {
        return collisionMap
    }
}