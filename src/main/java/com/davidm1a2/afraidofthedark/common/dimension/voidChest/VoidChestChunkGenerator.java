package com.davidm1a2.afraidofthedark.common.dimension.voidChest;

import com.davidm1a2.afraidofthedark.AfraidOfTheDark;
import com.davidm1a2.afraidofthedark.common.constants.ModBiomes;
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics;
import com.davidm1a2.afraidofthedark.common.worldGeneration.schematic.SchematicGenerator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * Chunk generator for the void chest dimension, generates cubes of barrier blocks and portal
 */
public class VoidChestChunkGenerator implements IChunkGenerator
{
    // The world that the chunk generator is for
    private final World world;

    /**
     * Constructor initializes the world reference
     *
     * @param world The world that this generator is for
     */
    public VoidChestChunkGenerator(World world)
    {
        this.world = world;
    }

    /**
     * Generates a chunk at the x, z position
     *
     * @param x The X chunkpos
     * @param z The Z chunkpos
     * @return A new chunk generated at the given x,z chunk position
     */
    @Override
    public Chunk generateChunk(int x, int z)
    {
        // A chunk primer generates base blocks, here we don't do anything with it so it's blank
        ChunkPrimer chunkprimer = new ChunkPrimer();

        // The chunk to return
        Chunk chunk = new Chunk(this.world, chunkprimer, x, z);

        // Grab the array of biomes in this chunk and update all biomes to be the 'void chest' biome
        byte[] biomes = chunk.getBiomeArray();
        // Grab the void chest biome id
        byte voidChestBiomeId = (byte) Biome.getIdForBiome(ModBiomes.VOID_CHEST);
        // Update all biomes to be void chest
        Arrays.fill(biomes, voidChestBiomeId);

        return chunk;
    }

    /**
     * Populates a chunk, here we test if [x,z] is one of our trigger chunks and if so generate a void chest box for the player
     *
     * @param x The x coordinate of the chunk
     * @param z The z coordinate of the chunk
     */
    @Override
    public void populate(int x, int z)
    {
        // The blocks between islands
        int blocksBetweenIslands = AfraidOfTheDark.INSTANCE.getConfigurationHandler().getBlocksBetweenIslands();
        // The X position in blockpos not chunkpos
        int xPos = x * 16;
        // The barrier block state
        IBlockState barrierDefaultState = Blocks.BARRIER.getDefaultState();
        // If we've hit one of the critical chunks trigger a void chest generation
        if (xPos % blocksBetweenIslands == 0 && z == 0)
        {
            for (int i = 0; i < 49; i++)
                for (int j = 0; j < 49; j++)
                {
                    // Create the floor
                    world.setBlockState(new BlockPos(xPos + i, 100, z + j), barrierDefaultState);
                    // Create the roof
                    world.setBlockState(new BlockPos(xPos + i, 100 + 48, z + j), barrierDefaultState);
                    // Create the left wall
                    world.setBlockState(new BlockPos(xPos + 0, 100 + i, z + j), barrierDefaultState);
                    // Create the right wall
                    world.setBlockState(new BlockPos(xPos + 48, 100 + i, z + j), barrierDefaultState);
                    // Create the front wall
                    world.setBlockState(new BlockPos(xPos + i, 100 + j, z + 0), barrierDefaultState);
                    // Create the back wall
                    world.setBlockState(new BlockPos(xPos + i, 100 + j, z + 48), barrierDefaultState);
                }
            // Generate the portal
            SchematicGenerator.generateSchematic(ModSchematics.VOID_CHEST_PORTAL, this.world, new BlockPos(xPos + 20, 100, -2));
        }
    }

    /**
     * Called to generate structures in the chunk, ignored for the void chest dimension
     *
     * @param chunkIn ignored
     * @param x       ignored
     * @param z       ignored
     * @return false since nothing generated
     */
    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z)
    {
        return false;
    }

    /**
     * Returns the possible creates that our dimension can spawn at a position, here we just refer to our VoidChest biome
     *
     * @param creatureType The creature to create
     * @param pos          The position to spawn at
     * @return The list of valid creatures, should be empty for the void chest
     */
    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
    {
        return this.world.getBiome(pos).getSpawnableList(creatureType);
    }

    /**
     * Gets the nearest structure to a position
     *
     * @param worldIn        ignored
     * @param structureName  ignored
     * @param position       ignored
     * @param findUnexplored ignored
     * @return null, no structures exist in the void chest dimension
     */
    @Nullable
    @Override
    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored)
    {
        return null;
    }

    /**
     * Ignored, there's no structures in the void chest dimension
     *
     * @param chunkIn ignored
     * @param x       ignored
     * @param z       ignored
     */
    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z)
    {
    }

    /**
     * True if a position is inside a structure, false otherwise
     *
     * @param worldIn       ignored
     * @param structureName ignored
     * @param pos           ignored
     * @return false, there's no structures in the void chest dimension
     */
    @Override
    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos)
    {
        return false;
    }
}
