package com.davidm1a2.afraidofthedark.common.dimension.nightmare

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModBiomes
import com.davidm1a2.afraidofthedark.common.constants.ModStructures
import net.minecraft.entity.EnumCreatureType
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biome.SpawnListEntry
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.ChunkPrimer
import net.minecraft.world.gen.IChunkGenerator

/**
 * Chunk generator for the nightmare dimension
 *
 * @param world The nightmare world instance
 */
class NightmareChunkGenerator(private val world: World) : IChunkGenerator
{
    /**
     * True if structures should be generated in the nightmare, false otherwise
     *
     * @param chunkIn The chunk to test
     * @param x       The x position to test
     * @param z       The z position to test
     * @return False, we generate our structures manually
     */
    override fun generateStructures(chunkIn: Chunk, x: Int, z: Int): Boolean
    {
        return false
    }

    /**
     * Called to generate a chunk x,z in the nightmare realm
     *
     * @param x The x position of the chunk
     * @param z The z position of the chunk
     * @return The generated chunk
     */
    override fun generateChunk(x: Int, z: Int): Chunk
    {
        // Create a new chunk primer to generate blocks
        val chunkprimer = ChunkPrimer()
        // Generate the chunk with the primer
        val chunk = Chunk(world, chunkprimer, x, z)
        // Update each biome to be nightmare
        val biome = chunk.biomeArray
        biome.fill(Biome.getIdForBiome(ModBiomes.NIGHTMARE).toByte())
        // Generate a sky light map
        chunk.generateSkylightMap()
        return chunk
    }

    /**
     * Called to populate the x,z chunk, here we just generate the nightmare island
     *
     * @param x The x chunk position
     * @param z The z chunk position
     */
    override fun populate(x: Int, z: Int)
    {
        // Grab some constants
        val islandWidth = ModStructures.NIGHTMARE_ISLAND.getXWidth()
        val islandLength = ModStructures.NIGHTMARE_ISLAND.getZLength()
        val blocksBetweenIslands = AfraidOfTheDark.INSTANCE.configurationHandler.blocksBetweenIslands
        // Compute the z position
        val zPos = z * 16
        // Ensure z is between [0, len(island)+15]
        if (zPos >= 0 && zPos <= islandLength + 15)
        {
            // Compute the x position
            val xPos = x * 16
            // Grab the island index
            val islandIndex = xPos / blocksBetweenIslands
            // Compute the relative x position of the island between 0 and blocksBetweenIslands
            val relativeXPos = xPos % blocksBetweenIslands
            // Ensure the relative x pos will have blocks inside of it
            if (relativeXPos >= 0 && relativeXPos <= islandWidth + 15)
            {
                // Compute the position the island would generate at
                val islandPos = BlockPos(islandIndex * blocksBetweenIslands, 0, 0)
                // Compute data for the structure
                val data = ModStructures.NIGHTMARE_ISLAND.generateStructureData(world, islandPos, world.biomeProvider)
                // Generate the chunk
                ModStructures.NIGHTMARE_ISLAND.generate(world, ChunkPos(x, z), data)
            }
        }
    }

    /**
     * Get possible creatures at a position from the biome
     *
     * @param creatureType The creature type to add
     * @param pos          The position to add it at
     * @return An empty list, this will be a nightmare biome which will return empty list
     */
    override fun getPossibleCreatures(creatureType: EnumCreatureType, pos: BlockPos): List<SpawnListEntry>
    {
        return world.getBiome(pos).getSpawnableList(creatureType)
    }

    /**
     * No nearest structure, this is only used by vanilla
     *
     * @param worldIn        the world reference
     * @param structureName  The structure name
     * @param position       The position of the player
     * @param findUnexplored If unexplored structures should be returned
     * @return null, no nearest structures
     */
    override fun getNearestStructurePos(worldIn: World, structureName: String, position: BlockPos, findUnexplored: Boolean): BlockPos?
    {
        return null
    }

    /**
     * Regenerates structures at a position, this gets ignored
     *
     * @param chunk The chunk to recreate in
     * @param x     The chunk x
     * @param z     The chunk z
     */
    override fun recreateStructures(chunk: Chunk, x: Int, z: Int)
    {
    }

    /**
     * Tests if a given position is inside a structure, returns false for the nightmare
     *
     * @param worldIn       The world to test with
     * @param structureName The structure to test
     * @param pos           The position to test
     * @return False, it's not possible to be in a structure here
     */
    override fun isInsideStructure(worldIn: World, structureName: String, pos: BlockPos): Boolean
    {
        return false
    }
}