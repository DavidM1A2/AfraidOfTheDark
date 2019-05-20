package com.DavidM1A2.afraidofthedark.common.worldGeneration.structure;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.capabilities.world.IHeightmap;
import com.DavidM1A2.afraidofthedark.common.constants.ModLootTables;
import com.DavidM1A2.afraidofthedark.common.constants.ModSchematics;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.schematic.SchematicGenerator;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.AOTDStructure;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.iterator.InteriorChunkIterator;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.processor.IChunkProcessor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraftforge.common.util.Constants;

import java.util.Collections;
import java.util.LinkedList;
import java.util.stream.IntStream;

/**
 * Gnomish city structure class
 */
public class StructureGnomishCity extends AOTDStructure
{
    // A list of NBT tag compound keys
    private static final String NBT_ROOM_INDEX = "room_index";
    private static final String NBT_ENARIAS_LAIR = "enarias_lair";
    private static final String NBT_ROOMS = "rooms";
    private static final String NBT_SURFACE_STAIRS = "surface_stairs";
    private static final String NBT_TUNNELS_NS = "tunnels_ns";
    private static final String NBT_TUNNELS_EW = "tunnels_ew";

    // Downward index is for going down floors, upward is for doing the opposite
    private static final Integer STAIR_DOWNWARD_INDEX = -1;
    private static final Integer STAIR_UPWARD_INDEX = -2;

    /**
     * Constructor just passes down the structure name
     */
    public StructureGnomishCity()
    {
        super("gnomish_city");
    }

    /**
     * Tests if this structure is valid for the given position
     *
     * @param blockPos      The position that the structure would begin at
     * @param heightmap     The heightmap to use in deciding if the structure will fit at the position
     * @param biomeProvider The provider used to generate the world, use biomeProvider.getBiomes() to get what biomes exist at a position
     * @return true if the structure fits at the position, false otherwise
     */
    @Override
    public double computeChanceToGenerateAt(BlockPos blockPos, IHeightmap heightmap, BiomeProvider biomeProvider)
    {
        return this.processChunks(new IChunkProcessor<Double>()
        {
            // Compute the minimum and maximum height over all the chunks that the witch hut will cross over
            int minHeight = Integer.MAX_VALUE;
            int maxHeight = Integer.MIN_VALUE;

            @Override
            public boolean processChunk(ChunkPos chunkPos)
            {
                // Compute min and max height
                minHeight = Math.min(minHeight, heightmap.getLowestHeight(chunkPos));
                maxHeight = Math.max(maxHeight, heightmap.getHighestHeight(chunkPos));
                // If there's less than a 20 block difference, continue, otherwise exit out
                return (maxHeight - minHeight) <= 20;
            }

            @Override
            public Double getResult()
            {
                // .13% chance to generate in any chunks this fits in
                return 0.0015 * AfraidOfTheDark.INSTANCE.getConfigurationHandler().getGnomishCityFrequency();
            }

            @Override
            public Double getDefaultResult()
            {
                return 0D;
            }
        }, new InteriorChunkIterator(this, blockPos));
    }

    /**
     * Generates the structure at a position with an optional argument of chunk position
     *
     * @param world    The world to generate the structure in
     * @param chunkPos Optional chunk position of a chunk to generate in. If supplied all blocks generated must be in this chunk only!
     * @param data     NBT data containing the structure's position
     */
    @Override
    public void generate(World world, ChunkPos chunkPos, NBTTagCompound data)
    {
        // Grab the rooms NBT and generate the 9 * 2 rooms first
        NBTTagList rooms = data.getTagList(NBT_ROOMS, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < rooms.tagCount(); i++)
        {
            NBTTagCompound room = rooms.getCompoundTagAt(i);
            // Get the room index and position
            Integer roomIndex = room.getInteger(NBT_ROOM_INDEX);
            BlockPos roomPos = NBTUtil.getPosFromTag(room.getCompoundTag(NBT_POSITION));
            // If the index is 0-rooms.length it's a standard room
            if (roomIndex >= 0)
            {
                SchematicGenerator.generateSchematic(ModSchematics.GNOMISH_CITY_ROOMS[roomIndex], world, roomPos, chunkPos, ModLootTables.GNOMISH_CITY);
            }
            // If the index is DOWNWARD_INDEX then it is the stairs that lead downwards
            else if (roomIndex.equals(STAIR_DOWNWARD_INDEX))
            {
                SchematicGenerator.generateSchematic(ModSchematics.ROOM_STAIR_DOWN, world, roomPos, chunkPos, ModLootTables.GNOMISH_CITY);
            }
            // If the index is UPWARD_INDEX then it is the stairs that lead upwards
            else if (roomIndex.equals(STAIR_UPWARD_INDEX))
            {
                SchematicGenerator.generateSchematic(ModSchematics.ROOM_STAIR_UP, world, roomPos, chunkPos, ModLootTables.GNOMISH_CITY);
            }
        }

        // Generate the east-west tunnels, this is pretty straight forward. Grab the position and generate the schematic
        NBTTagList tunnelsEW = data.getTagList(NBT_TUNNELS_EW, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tunnelsEW.tagCount(); i++)
        {
            NBTTagCompound tunnelEW = tunnelsEW.getCompoundTagAt(i);
            BlockPos tunnelPos = NBTUtil.getPosFromTag(tunnelEW.getCompoundTag(NBT_POSITION));
            SchematicGenerator.generateSchematic(ModSchematics.TUNNEL_EW, world, tunnelPos, chunkPos);
        }

        // Generate the north-south tunnels, this is pretty straight forward. Grab the position and generate the schematic
        NBTTagList tunnelsNS = data.getTagList(NBT_TUNNELS_NS, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tunnelsNS.tagCount(); i++)
        {
            NBTTagCompound tunnelNS = tunnelsNS.getCompoundTagAt(i);
            BlockPos tunnelPos = NBTUtil.getPosFromTag(tunnelNS.getCompoundTag(NBT_POSITION));
            SchematicGenerator.generateSchematic(ModSchematics.TUNNEL_NS, world, tunnelPos, chunkPos);
        }

        // Generate a set of two surface stairs on top of each other, one starting at the gnomish city stairwell
        // and one right above it to ensure the stairs are tall enough
        NBTTagCompound surfaceStairs = data.getCompoundTag(NBT_SURFACE_STAIRS);
        BlockPos surfaceStairsPos = NBTUtil.getPosFromTag(surfaceStairs.getCompoundTag(NBT_POSITION));
        SchematicGenerator.generateSchematic(ModSchematics.STAIRWELL, world, surfaceStairsPos, chunkPos);
        SchematicGenerator.generateSchematic(ModSchematics.STAIRWELL, world, surfaceStairsPos.add(0, ModSchematics.STAIRWELL.getHeight(), 0), chunkPos);

        // Generate enaria's lair at the bottom under the structure
        NBTTagCompound enariasLair = data.getCompoundTag(NBT_ENARIAS_LAIR);
        BlockPos enariasLairPos = NBTUtil.getPosFromTag(enariasLair.getCompoundTag(NBT_POSITION));
        SchematicGenerator.generateSchematic(ModSchematics.ENARIA_LAIR, world, enariasLairPos, chunkPos);
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
        // Create an nbt compound to return
        NBTTagCompound compound = new NBTTagCompound();
        // Set the block pos to be at y=20 where all gnomish cities spawn at
        blockPos = new BlockPos(blockPos.getX(), 5, blockPos.getZ());
        // Set the pos tag
        compound.setTag(NBT_POSITION, NBTUtil.createPosTag(blockPos));

        ///
        /// Compute what rooms will appear in each of the 18 spots
        ///

        // 3 special rooms that are stairwells
        int stairSurfaceTo1;
        int stairs1To2;
        int stairs2ToEnaria;

        // Compute the stairs from surface to level 1, can be a random room
        stairSurfaceTo1 = world.rand.nextInt(9);
        // We must place enaria's stairs here, unfortunately it's the only spot big enough to fit without
        // breaking the bounding box of the 3x3 rooms
        stairs2ToEnaria = 7;

        // Compute the stairs from level 1 to level 2, can't be in the same spot as stairs from 'surface -> 1' or '2 -> enaria'
        do
        {
            stairs1To2 = world.rand.nextInt(9);
        } while (stairSurfaceTo1 == stairs1To2 || stairs2ToEnaria == stairs1To2);

        // Create a queue of rooms where each room is represented twice.
        LinkedList<Integer> roomIndicesQueue = new LinkedList<>();
        // Populate the list with the indices 0-length twice
        IntStream.range(0, ModSchematics.GNOMISH_CITY_ROOMS.length)
                .flatMap(index -> IntStream.of(index, index))
                .forEach(roomIndicesQueue::add);
        // Shuffle the list so it's random
        Collections.shuffle(roomIndicesQueue, world.rand);

        // Generate the room NBTs
        NBTTagList rooms = new NBTTagList();

        // floor 0 (bottom) or 1 (upper)
        for (int floor = 0; floor < 2; floor++)
        {
            // Rooms in the x direction
            for (int xIndex = 0; xIndex < 3; xIndex++)
            {
                // Rooms in the z direction
                for (int zIndex = 0; zIndex < 3; zIndex++)
                {
                    // Grab the current room index
                    int currentRoom = xIndex + zIndex * 3;

                    // Create an NBT for this room
                    NBTTagCompound room = new NBTTagCompound();

                    // The room will always occur at the same position
                    room.setTag(NBT_POSITION, NBTUtil.createPosTag(blockPos.add(xIndex * 50, floor * 15 + 15, zIndex * 50)));

                    // If the room is the stair from surface to level 1 and the floor is upper (1) generate the stair room and stairwell
                    if (currentRoom == stairSurfaceTo1 && floor == 1)
                    {
                        room.setInteger(NBT_ROOM_INDEX, STAIR_UPWARD_INDEX);

                        // Create an NBT for the surface stairs that are used to walk down into the gnomish city
                        NBTTagCompound surfaceStairs = new NBTTagCompound();
                        surfaceStairs.setTag(NBT_POSITION, NBTUtil.createPosTag(blockPos.add(xIndex * 50 + 13, floor * 15 + 30, zIndex * 50 + 13)));
                        compound.setTag(NBT_SURFACE_STAIRS, surfaceStairs);
                    }
                    // If the room is the stair from level 1 to 2 and the floor is upper (1) generate the stair room
                    else if (currentRoom == stairs1To2 && floor == 1)
                    {
                        room.setInteger(NBT_ROOM_INDEX, STAIR_DOWNWARD_INDEX);
                    }
                    // If the room is the stair from level 1 to 2 and the floor is lower (0) generate the stair room
                    else if (currentRoom == stairs1To2 && floor == 0)
                    {
                        room.setInteger(NBT_ROOM_INDEX, STAIR_UPWARD_INDEX);
                    }
                    // If the room is the stair from level 2 to enaria and the floor is lower (0) generate the stair room
                    else if (currentRoom == stairs2ToEnaria && floor == 0)
                    {
                        room.setInteger(NBT_ROOM_INDEX, STAIR_DOWNWARD_INDEX);

                        // Create the enaria lair too to fit under this room
                        NBTTagCompound enariaRoom = new NBTTagCompound();
                        enariaRoom.setTag(NBT_POSITION, NBTUtil.createPosTag(blockPos.add(xIndex * 50 - 14, floor * 15, zIndex * 50 - 73)));
                        compound.setTag(NBT_ENARIAS_LAIR, enariaRoom);
                    }
                    // Generate a non-stair room, pop it off of the queue so we don't get it again
                    else
                    {
                        room.setInteger(NBT_ROOM_INDEX, roomIndicesQueue.pop());
                    }

                    // Append the room tag
                    rooms.appendTag(room);
                }
            }
        }

        // Append the rooms to the NBT
        compound.setTag(NBT_ROOMS, rooms);

        // Create tunnels inbetween rooms, 6 are east to west, 6 are north to south
        NBTTagList tunnelsEW = new NBTTagList();
        NBTTagList tunnelsNS = new NBTTagList();
        // Create tunnels for each floor
        for (int floor = 0; floor < 2; floor++)
        {
            // One loop that iterates 3 times for the axis (x or z) that contains 3 tunnels
            for (int i = 0; i < 3; i++)
            {
                // One loop that iterates 2 times for the axis (x or z) that contains 2 tunnels
                for (int j = 0; j < 2; j++)
                {
                    // Create an east-west tunnel for these parameters
                    NBTTagCompound tunnelEW = new NBTTagCompound();
                    tunnelEW.setTag(NBT_POSITION, NBTUtil.createPosTag(blockPos.add(i * 50 + 13, floor * 15 + 15 + 7, j * 50 + 32)));
                    tunnelsEW.appendTag(tunnelEW);

                    // Create a north-south tunnel for these parameters
                    NBTTagCompound tunnelNS = new NBTTagCompound();
                    tunnelNS.setTag(NBT_POSITION, NBTUtil.createPosTag(blockPos.add(j * 50 + 32, floor * 15 + 15 + 7, i * 50 + 13)));
                    tunnelsNS.appendTag(tunnelNS);
                }
            }
        }

        // Append the tunnels
        compound.setTag(NBT_TUNNELS_EW, tunnelsEW);
        compound.setTag(NBT_TUNNELS_NS, tunnelsNS);

        return compound;
    }

    /**
     * @return The width of the structure in blocks
     */
    @Override
    public int getXWidth()
    {
        // Room width is the same for all schematics
        int roomWidth = ModSchematics.ROOM_CAVE.getWidth();
        int tunnelWidth = ModSchematics.TUNNEL_NS.getWidth();
        // 3 rooms + 2 tunnels - 4 connector blocks
        return roomWidth * 3 + tunnelWidth * 2 - 4;
    }

    /**
     * @return The length of the structure in blocks
     */
    @Override
    public int getZLength()
    {
        // Room length is the same for all schematics
        int roomLength = ModSchematics.ROOM_CAVE.getLength();
        int tunnelLength = ModSchematics.TUNNEL_EW.getLength();
        // 3 rooms + 2 tunnels - 4 connector blocks
        return roomLength * 3 + tunnelLength * 2 - 4;
    }
}
