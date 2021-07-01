package com.davidm1a2.afraidofthedark.common.capabilities.world.structureCollisionMap

import net.minecraft.world.gen.feature.structure.StructureStart

/**
 * Capability interface to store a structure collision map for a world
 */
interface IWorldStructureCollisionMap {
    /**
     * Inserts a structure into the world structure collision map
     *
     * @param start The structure start to insert into the collision map
     */
    fun insertStructure(start: StructureStart)

    /**
     * Tests if a structure intersects with another previously inserted structure.
     *
     * @param start The start to test
     * @return True if the parameter is blocked by another one, false otherwise
     */
    fun isStructureBlocked(start: StructureStart): Boolean

    /**
     * Returns the bounding box tree. Used only in serialization. We really shouldn't have this in this interface, but there's no better way as far as I know...
     *
     * @return The bounding box tree
     */
    fun getBoundingBoxTree(): BoundingBoxTree
}