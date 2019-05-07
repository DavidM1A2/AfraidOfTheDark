package com.DavidM1A2.afraidofthedark.common.dimension.nightmare;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.constants.ModBiomes;
import com.DavidM1A2.afraidofthedark.common.constants.ModStructures;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * Chunk generator for the nightmare dimension
 */
public class NightmareChunkGenerator implements IChunkGenerator
{
    // The nightmare world instance
    private final World world;

    /**
     * Constructor caches the nightmare world instance
     *
     * @param world The nightmare world
     */
    public NightmareChunkGenerator(World world)
    {
        this.world = world;
    }

    /**
     * True if structures should be generated in the nightmare, false otherwise
     *
     * @param chunkIn The chunk to test
     * @param x The x position to test
     * @param z The z position to test
     * @return False, we generate our structures manually
     */
    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z)
    {
        return false;
    }

    /**
     * Called to generate a chunk x,z in the nightmare realm
     *
     * @param x The x position of the chunk
     * @param z The z position of the chunk
     * @return The generated chunk
     */
    @Override
    public Chunk generateChunk(int x, int z)
    {
        // Create a new chunk primer to generate blocks
        ChunkPrimer chunkprimer = new ChunkPrimer();

        // Generate the chunk with the primer
        Chunk chunk = new Chunk(this.world, chunkprimer, x, z);

        // Update each biome to be nightmare
        byte[] biome = chunk.getBiomeArray();
        Arrays.fill(biome, (byte) Biome.getIdForBiome(ModBiomes.NIGHTMARE));
        // Generate a sky light map
        chunk.generateSkylightMap();

        return chunk;
    }

    /**
     * Called to populate the x,z chunk, here we just generate the nightmare island
     *
     * @param x The x chunk position
     * @param z The z chunk position
     */
    @Override
    public void populate(int x, int z)
    {
        // Grab some constants
        int islandLength = ModStructures.NIGHTMARE_ISLAND.getZLength();
        int islandWidth = ModStructures.NIGHTMARE_ISLAND.getXWidth();
        int blocksBetweenIslands = AfraidOfTheDark.INSTANCE.getConfigurationHandler().getBlocksBetweenIslands();

        // Compute the z position
        int zPos = z * 16;
        // Ensure z is between [0, len(island)+15]
        if (zPos >= 0 && zPos <= islandLength + 15)
        {
            // Compute the x position
            int xPos = x * 16;
            // Grab the island index
            int islandIndex = xPos / blocksBetweenIslands;
            // Compute the relative x position of the island between 0 and blocksBetweenIslands
            int relativeXPos = xPos % blocksBetweenIslands;
            // Ensure the relative x pos will have blocks inside of it
            if (relativeXPos >= 0 && relativeXPos <= islandWidth + 15)
            {
                // Compute the position the island would generate at
                BlockPos islandPos = new BlockPos(islandIndex * blocksBetweenIslands, 0, 0);
                // Compute data for the structure
                NBTTagCompound data = ModStructures.NIGHTMARE_ISLAND.generateStructureData(this.world, islandPos, this.world.getBiomeProvider());
                // Generate the chunk
                ModStructures.NIGHTMARE_ISLAND.generate(this.world, new ChunkPos(x, z), data);
            }
        }
    }

    /**
     * Get possible creatures at a position from the biome
     *
     * @param creatureType The creature type to add
     * @param pos The position to add it at
     * @return An empty list, this will be a nightmare biome which will return empty list
     */
    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
    {
        return this.world.getBiome(pos).getSpawnableList(creatureType);
    }

    /**
     * No nearest structure, this is only used by vanilla
     *
     * @param worldIn the world reference
     * @param structureName The structure name
     * @param position The position of the player
     * @param findUnexplored If unexplored structures should be returned
     * @return null, no nearest structures
     */
    @Nullable
    @Override
    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored)
    {
        return null;
    }

    /**
     * Regenerates structures at a position, this gets ignored
     *
     * @param chunk The chunk to recreate in
     * @param x The chunk x
     * @param z The chunk z
     */
    @Override
    public void recreateStructures(Chunk chunk, int x, int z)
    {
    }

    /**
     * Tests if a given position is inside a structure, returns false for the nightmare
     *
     * @param worldIn The world to test with
     * @param structureName The structure to test
     * @param pos The position to test
     * @return False, it's not possible to be in a structure here
     */
    @Override
    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos)
    {
        return false;
    }
}
