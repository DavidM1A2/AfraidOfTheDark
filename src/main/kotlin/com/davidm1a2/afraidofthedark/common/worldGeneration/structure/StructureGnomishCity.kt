package com.davidm1a2.afraidofthedark.common.worldGeneration.structure

import com.davidm1a2.afraidofthedark.common.capabilities.world.IHeightmap
import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.worldGeneration.generateSchematic
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.iterator.InteriorChunkIterator
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.processor.IChunkProcessor
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World
import net.minecraft.world.biome.provider.BiomeProvider
import net.minecraftforge.common.util.Constants
import java.util.*
import kotlin.math.max
import kotlin.math.min

/**
 * Gnomish city structure class
 *
 * @constructor just passes down the structure name
 */
class StructureGnomishCity : AOTDStructure("gnomish_city") {
    /**
     * Tests if this structure is valid for the given position
     *
     * @param blockPos      The position that the structure would begin at
     * @param heightmap     The heightmap to use in deciding if the structure will fit at the position
     * @param biomeProvider The provider used to generate the world, use biomeProvider.getBiomes() to get what biomes exist at a position
     * @return true if the structure fits at the position, false otherwise
     */
    override fun computeChanceToGenerateAt(
        blockPos: BlockPos,
        heightmap: IHeightmap,
        biomeProvider: BiomeProvider
    ): Double {
        return processChunks(object : IChunkProcessor<Double> {
            // Compute the minimum and maximum height over all the chunks that the witch hut will cross over
            var minHeight = Int.MAX_VALUE
            var maxHeight = Int.MIN_VALUE

            override fun processChunk(chunkPos: ChunkPos): Boolean {
                // Compute min and max height
                minHeight = min(minHeight, heightmap.getLowestHeight(chunkPos))
                maxHeight = max(maxHeight, heightmap.getHighestHeight(chunkPos))

                // If there's less than a 20 block difference, continue, otherwise exit out
                return maxHeight - minHeight <= 20
            }

            override fun getResult(): Double {
                // .03% chance to generate in any chunks this fits in
                return 0.0003 * ModCommonConfiguration.gnomishCityFrequency
            }

            override fun getDefaultResult(): Double {
                return 0.0
            }
        }, InteriorChunkIterator(this, blockPos))
    }

    /**
     * Generates the structure at a position with an optional argument of chunk position
     *
     * @param world    The world to generate the structure in
     * @param chunkPos Optional chunk position of a chunk to generate in. If supplied all blocks generated must be in this chunk only!
     * @param data     NBT data containing the structure's position
     */
    override fun generate(world: World, chunkPos: ChunkPos, data: NBTTagCompound) {
        // Grab the rooms NBT and generate the 9 * 2 rooms first
        val rooms = data.getList(NBT_ROOMS, Constants.NBT.TAG_COMPOUND)
        for (i in 0 until rooms.size) {
            val room = rooms.getCompound(i)
            // Get the room index and position
            val roomIndex = room.getInt(NBT_ROOM_INDEX)
            val roomPos = NBTUtil.readBlockPos(room.getCompound(NBT_POSITION))

            when {
                // If the index is 0-rooms.length it's a standard room
                roomIndex >= 0 -> {
                    world.generateSchematic(
                        ModSchematics.GNOMISH_CITY_ROOMS[roomIndex],
                        roomPos,
                        chunkPos,
                        ModLootTables.GNOMISH_CITY
                    )
                }
                // If the index is DOWNWARD_INDEX then it is the stairs that lead downwards
                roomIndex == STAIR_DOWNWARD_INDEX -> {
                    world.generateSchematic(
                        ModSchematics.ROOM_STAIR_DOWN,
                        roomPos,
                        chunkPos,
                        ModLootTables.GNOMISH_CITY
                    )
                }
                // If the index is UPWARD_INDEX then it is the stairs that lead upwards
                roomIndex == STAIR_UPWARD_INDEX -> {
                    world.generateSchematic(ModSchematics.ROOM_STAIR_UP, roomPos, chunkPos, ModLootTables.GNOMISH_CITY)
                }
            }
        }

        // Generate the east-west tunnels, this is pretty straight forward. Grab the position and generate the schematic
        val tunnelsEW = data.getList(NBT_TUNNELS_EW, Constants.NBT.TAG_COMPOUND)
        for (i in 0 until tunnelsEW.size) {
            val tunnelEW = tunnelsEW.getCompound(i)
            val tunnelPos = NBTUtil.readBlockPos(tunnelEW.getCompound(NBT_POSITION))
            world.generateSchematic(ModSchematics.TUNNEL_EW, tunnelPos, chunkPos)
        }

        // Generate the north-south tunnels, this is pretty straight forward. Grab the position and generate the schematic
        val tunnelsNS = data.getList(NBT_TUNNELS_NS, Constants.NBT.TAG_COMPOUND)
        for (i in 0 until tunnelsNS.size) {
            val tunnelNS = tunnelsNS.getCompound(i)
            val tunnelPos = NBTUtil.readBlockPos(tunnelNS.getCompound(NBT_POSITION))
            world.generateSchematic(ModSchematics.TUNNEL_NS, tunnelPos, chunkPos)
        }

        // Generate a set of two surface stairs on top of each other, one starting at the gnomish city stairwell
        // and one right above it to ensure the stairs are tall enough
        val surfaceStairs = data.getCompound(NBT_SURFACE_STAIRS)
        val surfaceStairsPos = NBTUtil.readBlockPos(surfaceStairs.getCompound(NBT_POSITION))
        world.generateSchematic(ModSchematics.STAIRWELL, surfaceStairsPos, chunkPos)
        world.generateSchematic(
            ModSchematics.STAIRWELL,
            surfaceStairsPos.add(0, ModSchematics.STAIRWELL.getHeight().toInt(), 0),
            chunkPos
        )

        // Generate enaria's lair at the bottom under the structure
        val enariasLair = data.getCompound(NBT_ENARIAS_LAIR)
        val enariasLairPos = NBTUtil.readBlockPos(enariasLair.getCompound(NBT_POSITION))
        world.generateSchematic(ModSchematics.ENARIA_LAIR, enariasLairPos, chunkPos)
    }

    /**
     * Called to generate a random permutation of the structure. Set the structure's position
     *
     * @param world         The world to generate the structure's data for
     * @param blockPos      The position's x and z coordinates to generate the structure at
     * @param biomeProvider ignored
     * @return The NBTTagCompound containing any data needed for generation. Sent in Structure::generate
     */
    override fun generateStructureData(world: World, blockPos: BlockPos, biomeProvider: BiomeProvider): NBTTagCompound {
        // Create an nbt compound to return
        @Suppress("NAME_SHADOWING")
        var blockPos = blockPos
        val compound = NBTTagCompound()
        // Set the block pos to be at y=20 where all gnomish cities spawn at
        blockPos = BlockPos(blockPos.x, 5, blockPos.z)
        // Set the pos tag
        compound.setTag(NBT_POSITION, NBTUtil.writeBlockPos(blockPos))

        ///
        /// Compute what rooms will appear in each of the 18 spots
        ///

        // 3 special rooms that are stairwells
        val stairSurfaceTo1: Int
        var stairs1To2: Int
        val stairs2ToEnaria: Int

        // Compute the stairs from surface to level 1, can be a random room
        stairSurfaceTo1 = world.rand.nextInt(9)

        // We must place enaria's stairs here, unfortunately it's the only spot big enough to fit without
        // breaking the bounding box of the 3x3 rooms
        stairs2ToEnaria = 7

        // Compute the stairs from level 1 to level 2, can't be in the same spot as stairs from 'surface -> 1' or '2 -> enaria'
        do {
            stairs1To2 = world.rand.nextInt(9)
        } while (stairSurfaceTo1 == stairs1To2 || stairs2ToEnaria == stairs1To2)

        // Create a queue of rooms where each room is represented twice.
        val roomIndicesQueue = LinkedList<Int>()
        // Populate the list with the indices 0-length twice
        ModSchematics.GNOMISH_CITY_ROOMS.indices
            .flatMap { listOf(it, it) }
            .forEach { roomIndicesQueue.add(it) }
        // Shuffle the list so it's random
        roomIndicesQueue.shuffle(world.rand)

        // Generate the room NBTs
        val rooms = NBTTagList()
        // floor 0 (bottom) or 1 (upper)
        for (floor in 0..1) {
            // Rooms in the x direction
            for (xIndex in 0..2) {
                // Rooms in the z direction
                for (zIndex in 0..2) {
                    // Grab the current room index
                    val currentRoom = xIndex + zIndex * 3

                    // Create an NBT for this room
                    val room = NBTTagCompound()

                    // The room will always occur at the same position
                    room.setTag(
                        NBT_POSITION,
                        NBTUtil.writeBlockPos(blockPos.add(xIndex * 50, floor * 15 + 15, zIndex * 50))
                    )

                    // If the room is the stair from surface to level 1 and the floor is upper (1) generate the stair room and stairwell
                    if (currentRoom == stairSurfaceTo1 && floor == 1) {
                        room.setInt(NBT_ROOM_INDEX, STAIR_UPWARD_INDEX)
                        // Create an NBT for the surface stairs that are used to walk down into the gnomish city
                        val surfaceStairs = NBTTagCompound()
                        surfaceStairs.setTag(
                            NBT_POSITION,
                            NBTUtil.writeBlockPos(blockPos.add(xIndex * 50 + 13, floor * 15 + 30, zIndex * 50 + 13))
                        )
                        compound.setTag(NBT_SURFACE_STAIRS, surfaceStairs)
                    }
                    // If the room is the stair from level 1 to 2 and the floor is upper (1) generate the stair room
                    else if (currentRoom == stairs1To2 && floor == 1) {
                        room.setInt(NBT_ROOM_INDEX, STAIR_DOWNWARD_INDEX)
                    }
                    // If the room is the stair from level 1 to 2 and the floor is lower (0) generate the stair room
                    else if (currentRoom == stairs1To2 && floor == 0) {
                        room.setInt(NBT_ROOM_INDEX, STAIR_UPWARD_INDEX)
                    }
                    // If the room is the stair from level 2 to enaria and the floor is lower (0) generate the stair room
                    else if (currentRoom == stairs2ToEnaria && floor == 0) {
                        room.setInt(NBT_ROOM_INDEX, STAIR_DOWNWARD_INDEX)

                        // Create the enaria lair too to fit under this room
                        val enariaRoom = NBTTagCompound()
                        enariaRoom.setTag(
                            NBT_POSITION,
                            NBTUtil.writeBlockPos(blockPos.add(xIndex * 50 - 14, floor * 15, zIndex * 50 - 73))
                        )
                        compound.setTag(NBT_ENARIAS_LAIR, enariaRoom)
                    }
                    // Generate a non-stair room, pop it off of the queue so we don't get it again
                    else {
                        room.setInt(NBT_ROOM_INDEX, roomIndicesQueue.pop()!!)
                    }

                    // Append the room tag
                    rooms.add(room)
                }
            }
        }

        // Append the rooms to the NBT
        compound.setTag(NBT_ROOMS, rooms)

        // Create tunnels inbetween rooms, 6 are east to west, 6 are north to south
        val tunnelsEW = NBTTagList()
        val tunnelsNS = NBTTagList()

        // Create tunnels for each floor
        for (floor in 0..1) {
            // One loop that iterates 3 times for the axis (x or z) that contains 3 tunnels
            for (i in 0..2) {
                // One loop that iterates 2 times for the axis (x or z) that contains 2 tunnels
                for (j in 0..1) {
                    // Create an east-west tunnel for these parameters
                    val tunnelEW = NBTTagCompound()
                    tunnelEW.setTag(
                        NBT_POSITION,
                        NBTUtil.writeBlockPos(blockPos.add(i * 50 + 13, floor * 15 + 15 + 7, j * 50 + 32))
                    )
                    tunnelsEW.add(tunnelEW)

                    // Create a north-south tunnel for these parameters
                    val tunnelNS = NBTTagCompound()
                    tunnelNS.setTag(
                        NBT_POSITION,
                        NBTUtil.writeBlockPos(blockPos.add(j * 50 + 32, floor * 15 + 15 + 7, i * 50 + 13))
                    )
                    tunnelsNS.add(tunnelNS)
                }
            }
        }

        // Append the tunnels
        compound.setTag(NBT_TUNNELS_EW, tunnelsEW)
        compound.setTag(NBT_TUNNELS_NS, tunnelsNS)

        return compound
    }

    /**
     * @return The width of the structure in blocks
     */
    override fun getXWidth(): Int {
        // Room width is the same for all schematics
        val roomWidth = ModSchematics.ROOM_CAVE.getWidth().toInt()
        val tunnelWidth = ModSchematics.TUNNEL_NS.getWidth().toInt()
        // 3 rooms + 2 tunnels - 4 connector blocks
        return roomWidth * 3 + tunnelWidth * 2 - 4
    }

    /**
     * @return The length of the structure in blocks
     */
    override fun getZLength(): Int {
        // Room length is the same for all schematics
        val roomLength = ModSchematics.ROOM_CAVE.getLength().toInt()
        val tunnelLength = ModSchematics.TUNNEL_EW.getLength().toInt()
        // 3 rooms + 2 tunnels - 4 connector blocks
        return roomLength * 3 + tunnelLength * 2 - 4
    }

    companion object {
        // A list of NBT tag compound keys
        private const val NBT_ROOM_INDEX = "room_index"
        private const val NBT_ENARIAS_LAIR = "enarias_lair"
        private const val NBT_ROOMS = "rooms"
        private const val NBT_SURFACE_STAIRS = "surface_stairs"
        private const val NBT_TUNNELS_NS = "tunnels_ns"
        private const val NBT_TUNNELS_EW = "tunnels_ew"

        // Downward index is for going down floors, upward is for doing the opposite
        private const val STAIR_DOWNWARD_INDEX = -1
        private const val STAIR_UPWARD_INDEX = -2
    }
}