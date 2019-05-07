package com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base;

import com.DavidM1A2.afraidofthedark.common.capabilities.world.OverworldHeightmap;
import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.iterator.IChunkIterator;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.iterator.InteriorChunkIterator;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.processor.IChunkProcessor;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.processor.LowestHeightChunkProcessor;
import com.google.common.collect.ImmutableSet;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;

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
     * Called to process/aggregate data about chunks of the structure if placed at a given position
     *
     * @param chunkProcessor The processor to process the chunk with
     * @param chunkIterator  The iterator used to go over the chunks
     * @param <T>            The type of result to return after processing
     * @return The result of the chunk processor type
     */
    protected <T> T processChunks(IChunkProcessor<T> chunkProcessor, IChunkIterator chunkIterator)
    {
        for (ChunkPos chunkPos : chunkIterator.getChunks())
            // If process chunk returns false then return the default result
            if (!chunkProcessor.processChunk(chunkPos))
            {
                return chunkProcessor.getDefaultResult();
            }
        // All chunks were processed so return the actual result
        return chunkProcessor.getResult();
    }

    /**
     * Approximates the biomes in a chunk by testing the 4 corners and the center
     *
     * @param biomeProvider The biome provider
     * @param chunkX        The chunk's X coordinate
     * @param chunkZ        The chunk's Z coordinate
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

    /**
     * Called to generate a random permutation of the structure. Set the structure's position
     *
     * @param world         The world to generate the structure's data for
     * @param blockPos      The position's x and z coordinates to generate the structure at
     * @param biomeProvider ignored
     * @return The NBTTagCompound containing any data needed for generation. Sent in Structure::generate
     */
    @Override
    public NBTTagCompound generateStructureData(World world, BlockPos blockPos, BiomeProvider biomeProvider)
    {
        NBTTagCompound compound = new NBTTagCompound();

        // By default set the position to be on ground level
        int yPos = this.processChunks(new LowestHeightChunkProcessor(OverworldHeightmap.get(world)), new InteriorChunkIterator(this, blockPos));
        // Update the y coordinate
        blockPos = new BlockPos(blockPos.getX(), yPos, blockPos.getZ());
        // Set the pos tag
        compound.setTag(NBT_POSITION, NBTUtil.createPosTag(blockPos));

        return compound;
    }
}
