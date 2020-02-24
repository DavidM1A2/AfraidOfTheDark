package com.davidm1a2.afraidofthedark.common.worldGeneration

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.world.OverworldHeightmap
import com.davidm1a2.afraidofthedark.common.capabilities.world.StructurePlan
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.Structure
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World
import net.minecraftforge.event.terraingen.PopulateChunkEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.*

/**
 * Class used to map out the entire world structures used for structure generation
 *
 * @property registeredStructures Create a random permutation of the structures to test, the first that passes the test will be generated
 * @property random Create a random object from the world seed
 */
class WorldStructurePlanner {
    private lateinit var registeredStructures: MutableList<Structure>
    private lateinit var random: Random

    /**
     * Called whenever a chunk is generated and needs population, we update our terrain generation here
     *
     * @param event The event containing the chunk and world
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onChunkPopulated(event: PopulateChunkEvent.Pre) {
        // Get a reference to the world
        val world = event.world
        // Get the X and Z coordinates of the chunk that was populated
        val chunkX = event.chunkX
        val chunkZ = event.chunkZ

        // If we do not know of our structure list yet grab the list from the registry
        if (!::registeredStructures.isInitialized) {
            // Remove structures that don't generate naturally
            registeredStructures = ModRegistries.STRUCTURE.valuesCollection.toMutableList()
        }

        // If we don't have a random yet init it
        if (!::random.isInitialized) {
            random = Random(world.seed)
        }

        // Generate the structure plan for the chunk
        planStructuresInChunk(world, chunkX, chunkZ)
    }

    /**
     * Called to generate the plan of structures for a chunk
     *
     * @param world  The world to plan structures in, should be server side
     * @param chunkX The X coordinate of the chunk
     * @param chunkZ The Z coordinate of the chunk
     */
    private fun planStructuresInChunk(world: World, chunkX: Int, chunkZ: Int) {
        // If the world is server side, and it's the overworld, we begin planning overworld structures
        if (!world.isRemote && world.provider.dimension == 0) {
            // Grab the structure plan for the world
            val structurePlan = StructurePlan.get(world)
            val chunkPos = ChunkPos(chunkX, chunkZ)

            // If a structure does not yet exist at the position
            if (!structurePlan!!.structureExistsAt(chunkPos)) {
                // Access the structures in random order
                registeredStructures.shuffle(random)

                // Grab the heightmap for the world
                val heightmap = OverworldHeightmap.get(world)

                // Grab the biome provider for the world which we use to test which biomes exist where
                val biomeProvider = world.biomeProvider

                // Compute the 0,0 block pos for this structure
                val chunk0Corner = chunkPos.getBlock(0, 63, 0)

                // Compute the 4 possible positions of this structure with 4 extra slots for randomized versions of that position
                val positions = arrayOfNulls<BlockPos>(8)

                // Try placing each different structure at each possible permutation around with this chunk at the edge of the structure
                for (structure in registeredStructures) {
                    // Here we use a heuristic to test if a structure will fit inside of a chunk, we only test if we place the corners of the chunk
                    // inside the chunkpos instead of all possible positionings of the structure over the chunk

                    // Position the structure in the top right of the chunk
                    val topRight = chunk0Corner.add(15, 0, 15)
                    positions[0] = topRight
                    positions[1] = topRight.add(-random.nextInt(16), 0, -random.nextInt(16))

                    // Position the structure in the top left of the chunk
                    val topLeft = chunk0Corner.add(0 - structure.getXWidth() + 1, 0, 15)
                    positions[2] = topLeft
                    positions[3] = topLeft.add(random.nextInt(16), 0, -random.nextInt(16))

                    // Position the structure in the bottom left of the chunk
                    val bottomLeft = chunk0Corner.add(0 - structure.getXWidth() + 1, 0, 0 - structure.getZLength() + 1)
                    positions[4] = bottomLeft
                    positions[5] = bottomLeft.add(random.nextInt(16), 0, random.nextInt(16))

                    // Position the structure in the bottom right of the chunk
                    val bottomRight = chunk0Corner.add(15, 0, 0 - structure.getZLength() + 1)
                    positions[6] = bottomRight
                    positions[7] = bottomRight.add(-random.nextInt(16), 0, random.nextInt(16))

                    // Each even index contains one extreme corner positioning, and each odd index contains a randomized permutation of that
                    // positioning which we would actually generate at
                    var i = 0
                    while (i < positions.size) {
                        val possiblePos = positions[i]
                        // Test if the structure fits into the structure map at the position (meaning no other structures would overlap this new structure)
                        // and if the structure would fit based on the heightmap
                        if (structurePlan.structureFitsAt(structure, possiblePos!!)) {
                            // Compute the chance that the structure could spawn here
                            val percentChance =
                                structure.computeChanceToGenerateAt(possiblePos, heightmap, biomeProvider)

                            // If our random dice roll succeeds place the structure
                            if (random.nextDouble() < percentChance) {
                                // Grab the randomized position to generate the structure at
                                val posToGenerate = positions[i + 1]

                                // Place the structure into our structure plan
                                structurePlan.placeStructure(
                                    structure,
                                    structure.generateStructureData(world, posToGenerate!!, biomeProvider)
                                )

                                // Generate any chunks that this structure will generate in that are already generated
                                generateExistingChunks(structure, world, posToGenerate)

                                // We planned a structure for this chunk so return out
                                return
                            }
                        }
                        i = i + 2
                    }
                }
            }
        }
    }

    /**
     * When we plan a structure it may overlap existing chunks so we need to generate the structure in those chunks
     *
     * @param structure     The structure to generate
     * @param world         The world to generate in
     * @param posToGenerate The position to generate at
     */
    private fun generateExistingChunks(structure: Structure, world: World, posToGenerate: BlockPos) {
        // Store a reference to the world generator
        val worldGenerator = AfraidOfTheDark.INSTANCE.worldGenerator

        // Compute the bottom left and top right chunk position
        val bottomLeftCorner = ChunkPos(posToGenerate)
        val topRightCorner = ChunkPos(posToGenerate.add(structure.getXWidth(), 0, structure.getZLength()))

        // Iterate over all chunks in the region and test if the world has a given chunk or not yet
        for (chunkX in bottomLeftCorner.x..topRightCorner.x) for (chunkZ in bottomLeftCorner.z..topRightCorner.z) {
            // If the chunk is generated at the coordinates then generate the structure at that position
            if (world.isChunkGeneratedAt(chunkX, chunkZ)) {
                worldGenerator.addChunkToRegenerate(ChunkPos(chunkX, chunkZ))
            }
        }
    }
}