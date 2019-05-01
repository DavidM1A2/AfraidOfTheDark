package com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.iterator;

import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.Structure;
import com.google.common.collect.Lists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.List;

/**
 * Chunk iterator that goes over all chunks that a structure occupies
 */
public class InteriorChunkIterator implements IChunkIterator
{
    // The structure to get interior chunks for
    private final Structure structure;
    // The base position the structure is at
    private final BlockPos basePos;

    /**
     * Constructor just sets structure and basepos fields
     *
     * @param structure The structure to get interior chunks for
     * @param basePos The base position the structure is at
     */
    public InteriorChunkIterator(Structure structure, BlockPos basePos)
    {
        this.structure = structure;
        this.basePos = basePos;
    }

    /**
     * @return A list of chunks to iterate over, will be all chunks a structure will occupy
     */
    @Override
    public List<ChunkPos> getChunks()
    {
        // Grab the chunk positions of the two corners of the structure
        ChunkPos bottomLeftCorner = new ChunkPos(this.basePos);
        ChunkPos topRightCorner = new ChunkPos(this.basePos.add(this.structure.getXWidth(), 0, this.structure.getZLength()));

        // Go over all chunks in the structure and add them to the list.
        List<ChunkPos> interiorChunks = Lists.newArrayList();
        for (int chunkX = bottomLeftCorner.x; chunkX <= topRightCorner.x; chunkX++)
            for (int chunkZ = bottomLeftCorner.z; chunkZ <= topRightCorner.z; chunkZ++)
                interiorChunks.add(new ChunkPos(chunkX, chunkZ));
        return interiorChunks;
    }
}
