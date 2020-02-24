package com.davidm1a2.afraidofthedark.common.dimension.voidChest

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModBiomes
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.worldGeneration.schematic.SchematicGenerator
import net.minecraft.entity.EnumCreatureType
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biome.SpawnListEntry
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.ChunkPrimer
import net.minecraft.world.gen.IChunkGenerator

/**
 * Chunk generator for the void chest dimension, generates cubes of barrier blocks and portal
 *
 * @param world The world that the chunk generator is for
 */
class VoidChestChunkGenerator(private val world: World) : IChunkGenerator {
    /**
     * Generates a chunk at the x, z position
     *
     * @param x The X chunkpos
     * @param z The Z chunkpos
     * @return A new chunk generated at the given x,z chunk position
     */
    override fun generateChunk(x: Int, z: Int): Chunk {
        // A chunk primer generates base blocks, here we don't do anything with it so it's blank
        val chunkprimer = ChunkPrimer()
        // The chunk to return
        val chunk = Chunk(world, chunkprimer, x, z)
        // Grab the array of biomes in this chunk and update all biomes to be the 'void chest' biome
        val biomes = chunk.biomeArray
        // Grab the void chest biome id
        val voidChestBiomeId = Biome.getIdForBiome(ModBiomes.VOID_CHEST).toByte()
        // Update all biomes to be void chest
        biomes.fill(voidChestBiomeId)
        return chunk
    }

    /**
     * Populates a chunk, here we test if [x,z] is one of our trigger chunks and if so generate a void chest box for the player
     *
     * @param x The x coordinate of the chunk
     * @param z The z coordinate of the chunk
     */
    override fun populate(x: Int, z: Int) {
        // The blocks between islands
        val blocksBetweenIslands = AfraidOfTheDark.INSTANCE.configurationHandler.blocksBetweenIslands
        // The X position in blockpos not chunkpos
        val xPos = x * 16
        // The barrier block state
        val barrierDefaultState = Blocks.BARRIER.defaultState
        // If we've hit one of the critical chunks trigger a void chest generation
        if (xPos % blocksBetweenIslands == 0 && z == 0) {
            for (i in 0..48) {
                for (j in 0..48) {
                    // Create the floor
                    world.setBlockState(BlockPos(xPos + i, 100, z + j), barrierDefaultState)
                    // Create the roof
                    world.setBlockState(BlockPos(xPos + i, 100 + 48, z + j), barrierDefaultState)
                    // Create the left wall
                    world.setBlockState(BlockPos(xPos + 0, 100 + i, z + j), barrierDefaultState)
                    // Create the right wall
                    world.setBlockState(BlockPos(xPos + 48, 100 + i, z + j), barrierDefaultState)
                    // Create the front wall
                    world.setBlockState(BlockPos(xPos + i, 100 + j, z + 0), barrierDefaultState)
                    // Create the back wall
                    world.setBlockState(BlockPos(xPos + i, 100 + j, z + 48), barrierDefaultState)
                }
            }

            // Generate the portal
            SchematicGenerator.generateSchematic(ModSchematics.VOID_CHEST_PORTAL, world, BlockPos(xPos + 20, 100, -2))
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
    override fun generateStructures(chunkIn: Chunk, x: Int, z: Int): Boolean {
        return false
    }

    /**
     * Returns the possible creates that our dimension can spawn at a position, here we just refer to our VoidChest biome
     *
     * @param creatureType The creature to create
     * @param pos          The position to spawn at
     * @return The list of valid creatures, should be empty for the void chest
     */
    override fun getPossibleCreatures(creatureType: EnumCreatureType, pos: BlockPos): List<SpawnListEntry> {
        return world.getBiome(pos).getSpawnableList(creatureType)
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
    override fun getNearestStructurePos(
        worldIn: World,
        structureName: String,
        position: BlockPos,
        findUnexplored: Boolean
    ): BlockPos? {
        return null
    }

    /**
     * Ignored, there's no structures in the void chest dimension
     *
     * @param chunkIn ignored
     * @param x       ignored
     * @param z       ignored
     */
    override fun recreateStructures(chunkIn: Chunk, x: Int, z: Int) {
    }

    /**
     * True if a position is inside a structure, false otherwise
     *
     * @param worldIn       ignored
     * @param structureName ignored
     * @param pos           ignored
     * @return false, there's no structures in the void chest dimension
     */
    override fun isInsideStructure(worldIn: World, structureName: String, pos: BlockPos): Boolean {
        return false
    }
}