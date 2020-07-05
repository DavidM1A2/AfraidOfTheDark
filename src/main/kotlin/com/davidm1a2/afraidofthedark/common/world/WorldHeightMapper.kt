package com.davidm1a2.afraidofthedark.common.world

import com.davidm1a2.afraidofthedark.common.capabilities.world.OverworldHeightmap
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.IWorld
import net.minecraft.world.WorldServer
import net.minecraft.world.chunk.ChunkPrimer
import net.minecraft.world.chunk.UpgradeData
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.gen.Heightmap
import net.minecraftforge.event.terraingen.ChunkGeneratorEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import kotlin.math.max
import kotlin.math.min

/**
 * Class used to map out the entire world height used for terrain generation
 *
 * @property chunkPredictionRange The range at which we predict chunk height values
 */
class WorldHeightMapper {
    private var chunkPredictionRange = -1

    /**
     * Called whenever a chunk is generated and needs population, we update our terrain height map here
     *
     * @param event The event containing the chunk and world
     */
    @SubscribeEvent(priority = EventPriority.HIGH)
    fun onChunkPopulated(event: ChunkGeneratorEvent.ReplaceBiomeBlocks) {
        /*
        // Get a reference to the world
        val world = event.world

        // Get the X and Z coordinates of the chunk that was populated
        val chunkX = event.chunk.pos.x
        val chunkZ = event.chunk.pos.z

        // Initialize the chunk prediction range once based on the largest structure size
        if (chunkPredictionRange == -1) {
            // Go over each structure and find the largest
            for (structure in ModRegistries.STRUCTURE) {
                // Compute the maximum number of blocks a structure can cover, add 15 to adjust for if the structure is placed in the corner of a chunk
                val maxXBlocksCovered = structure.getXWidth() + 15
                val maxZBlocksCovered = structure.getZLength() + 15

                // The max blocks covered is the max of x blocks and max of z blocks
                val maxBlocksCovered = max(maxXBlocksCovered, maxZBlocksCovered)
                // To compute the number of chunks covered divide by 16 blocks per chunk ceiled
                val maxChunksCovered = ceil(maxBlocksCovered / 16.0).toInt()
                // The new prediction range is the max of the old range and the current structure size
                chunkPredictionRange = max(chunkPredictionRange, maxChunksCovered)
            }
        }

        // Generate the height map for the chunk
        heightMapChunk(world, chunkX, chunkZ, chunkPredictionRange)

         */
    }

    /**
     * Called to generate the height map for a chunk X and Z +/- chunkPredictionRange from the center
     *
     * @param world                The world to generate for, should be the overworld and server side
     * @param chunkX               The X coordinate of the chunk
     * @param chunkZ               The Z coordinate of the chunk
     * @param chunkPredictionRange The prediction range to go out
     */
    private fun heightMapChunk(world: IWorld, chunkX: Int, chunkZ: Int, chunkPredictionRange: Int) {
        // If the world is server side, and it's the overworld, we begin heightmap creation
        if (!world.isRemote && world is WorldServer && world.dimension.type == DimensionType.OVERWORLD) {
            // Grab a reference to the chunk provider
            val chunkProvider = world.chunkProvider

            // Allocate one chunk primer to predict chunks
            // ChunkPrimer chunkPrimer = new ChunkPrimer();

            // Grab data for the world object
            val heightmap = OverworldHeightmap.get(world)

            // Iterate from chunkX - 3 to chunkX + 3
            for (x in chunkX - chunkPredictionRange..chunkX + chunkPredictionRange) {
                // Iterate from chunkZ - 3 to chunkZ + 3
                for (z in chunkZ - chunkPredictionRange..chunkZ + chunkPredictionRange) {
                    // Predict the height of that chunk
                    val chunkPos = ChunkPos(x, z)

                    // Test first if the chunk has already been generated. We can just test 4,4 because if 4,4 is not present
                    // The rest will not be either
                    if (!heightmap.heightKnown(chunkPos)) {
                        // Let our world generator set the blocks inside the chunk. This is much faster than actually generating the chunk!
                        //chunkGenerator.setBlocksInChunk(x, z, chunkPrimer);
                        val chunk = ChunkPrimer(ChunkPos(x, z), UpgradeData.EMPTY)
                        chunkProvider.chunkGenerator.makeBase(ChunkPrimer(ChunkPos(x, z), UpgradeData.EMPTY))

                        // chunkGenerator.replaceBiomeBlocks(x, z, chunkPrimer, world.getBiomeProvider().getBiomes(null, x * 16, z * 16, 16, 16));
                        // Now use this chunk information to predict height values of chunks that have not yet been generated
                        // Instead of heightmapping the entire chunk we only do 4 points to get an idea if the chunk is flat or not. This
                        // Lets us make fairly accurate predictions if the chunk is flat or not. We pick the 4 corners of the chunk
                        // Use z-1 because there's a bug with findGroundBlockIdx
                        val chunkHeights = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE)!!
                        val corner1Height = chunkHeights.getHeight(0, 0)
                        val corner2Height = chunkHeights.getHeight(15, 0)
                        val corner3Height = chunkHeights.getHeight(0, 15)
                        val corner4Height = chunkHeights.getHeight(15, 15)

                        // Find the min and max of the 4 values
                        val minHeight = min(min(corner1Height, corner2Height), min(corner3Height, corner4Height))
                        val maxHeight = max(max(corner1Height, corner2Height), max(corner3Height, corner4Height))

                        // Set the height
                        heightmap.setHeight(chunkPos, minHeight, maxHeight)
                    }
                }
            }
        }
    }
}