package com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.google.common.collect.ImmutableSet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Base class for all AOTD structures
 */
public abstract class AOTDStructure extends Structure
{
    /**
     * Structure constructor uses AOTD as the prefix for the registry name
     *
     * @param baseName The name of the structure
     */
    public AOTDStructure(String baseName)
    {
        super();
        this.setRegistryName(Constants.MOD_ID + ":" + baseName);
    }

    /**
     * Called to process all interior chunks of the structure if placed at a given position
     *
     * @param chunkProcessor The processor to process the chunk with
     * @param basePos        The position of the structure
     * @param <T>            The type of result to return after processing
     * @return The result of the chunk processor type
     */
    protected <T> T processInteriorChunks(IChunkProcessor<T> chunkProcessor, BlockPos basePos)
    {
        // Grab the chunk positions of the two corners of the structure
        ChunkPos bottomLeftCorner = new ChunkPos(basePos);
        ChunkPos topRightCorner = new ChunkPos(basePos.add(this.getXWidth(), 0, this.getZLength()));

        // For each chunk inside the structure call process chunk
        for (int chunkX = bottomLeftCorner.x; chunkX <= topRightCorner.x; chunkX++)
            for (int chunkZ = bottomLeftCorner.z; chunkZ <= topRightCorner.z; chunkZ++)
                // If process chunk returns false then return the default result
                if (!chunkProcessor.processChunk(chunkX, chunkZ))
                    return chunkProcessor.getDefaultResult();
        // All chunks were processed so return the actual result
        return chunkProcessor.getResult();
    }

    /**
     * Approximates the biomes in a chunk by testing the 4 corners and the center
     *
     * @param biomeProvider The biome provider
     * @param chunkX The chunk's X coordinate
     * @param chunkZ The chunk's Z coordinate
     * @return A set of biomes found at the 4 corners and center of the chunk
     */
    protected Set<Biome> approximateBiomesInChunk(BiomeProvider biomeProvider, int chunkX, int chunkZ)
    {
        Biome[] temp = new Biome[1];
        return ImmutableSet.of(
                biomeProvider.getBiomes(temp, chunkX * 16 + 0, chunkZ * 16 + 0, 1, 1)[0],
                biomeProvider.getBiomes(temp, chunkX * 16 + 8, chunkZ * 16 + 8, 1, 1)[0],
                biomeProvider.getBiomes(temp, chunkX * 16 + 15, chunkZ * 16 + 0, 1, 1)[0],
                biomeProvider.getBiomes(temp, chunkX * 16 + 0, chunkZ * 16 + 15, 1, 1)[0],
                biomeProvider.getBiomes(temp, chunkX * 16 + 15, chunkZ * 16 + 15, 1, 1)[0]
        );
    }
}
