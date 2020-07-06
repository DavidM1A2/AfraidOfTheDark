package com.davidm1a2.afraidofthedark.common.capabilities.world

import com.davidm1a2.afraidofthedark.common.world.structure.old.base.Structure
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos

/**
 * Structure plan interface used to plan AOTD structures
 */
interface IStructurePlan {
    /**
     * Returns the structure in the given chunk or null if no such structure exists
     *
     * @param chunkPos The position to test
     * @return A structure if it exists, or null if no structure exists here
     */
    fun getPlacedStructureAt(chunkPos: ChunkPos): PlacedStructure?

    /**
     * Returns an copy of the list of structures present in the world.
     *
     * @return A copy of the structures in the structure plan
     */
    fun getPlacedStructures(): List<PlacedStructure>

    /**
     * Returns true if a structure exists at the given chunk pos or false if it does not
     *
     * @param chunkPos The position to test
     * @return True if a structure exists at the position, or false otherwise
     */
    fun structureExistsAt(chunkPos: ChunkPos): Boolean

    /**
     * Tests if a given structure would fit if it was placed at a given position
     *
     * @param structure The structure to place
     * @param blockPos  The position to place the structure
     * @return True if the structure would fit without overlapping another structure, or false otherwise
     */
    fun structureFitsAt(structure: Structure, blockPos: BlockPos): Boolean

    /**
     * Called to place a given structure with data. This will overwrite any existing structures in the area
     *
     * @param structure The structure to place
     * @param data      Any additional data the structure requires like position
     */
    fun placeStructure(structure: Structure, data: NBTTagCompound)
}