package com.davidm1a2.afraidofthedark.common.capabilities.world;

import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.Structure;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.List;

/**
 * Structure plan interface used to plan AOTD structures
 */
public interface IStructurePlan
{
    /**
     * Returns the structure in the given chunk or null if no such structure exists
     *
     * @param chunkPos The position to test
     * @return A structure if it exists, or null if no structure exists here
     */
    PlacedStructure getPlacedStructureAt(ChunkPos chunkPos);

    /**
     * Returns an copy of the list of structures present in the world.
     *
     * @return A copy of the structures in the structure plan
     */
    List<PlacedStructure> getPlacedStructures();

    /**
     * Returns true if a structure exists at the given chunk pos or false if it does not
     *
     * @param chunkPos The position to test
     * @return True if a structure exists at the position, or false otherwise
     */
    boolean structureExistsAt(ChunkPos chunkPos);

    /**
     * Tests if a given structure would fit if it was placed at a given position
     *
     * @param structure The structure to place
     * @param blockPos  The position to place the structure
     * @return True if the structure would fit without overlapping another structure, or false otherwise
     */
    boolean structureFitsAt(Structure structure, BlockPos blockPos);

    /**
     * Called to place a given structure with data. This will overwrite any existing structures in the area
     *
     * @param structure The structure to place
     * @param data      Any additional data the structure requires like position
     */
    void placeStructure(Structure structure, NBTTagCompound data);
}
