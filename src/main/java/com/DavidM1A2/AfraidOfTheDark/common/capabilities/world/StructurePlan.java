package com.DavidM1A2.afraidofthedark.common.capabilities.world;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.Structure;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class used to store a world's structure plan
 */
public class StructurePlan extends WorldSavedData implements IStructurePlan
{
    // The ID of the AOTD structure plan
    private static final String IDENTIFIER = Constants.MOD_ID + "_structure_plan";

    // The NBT compound tag keys
    private static final String NBT_CHUNK_MAP = "chunk_map";
    private static final String NBT_STRUCTURE_DATA = "structure_data";

    // The actual structure plan map that we are saving
    private Map<ChunkPos, PlacedStructure> chunkToStructure = new HashMap<>();

    /**
     * Constructor just calls super with our ID
     */
    public StructurePlan()
    {
        this(IDENTIFIER);
    }

    /**
     * Constructor where we can supply our own ID
     *
     * @param identifier The ID to use for this data
     */
    public StructurePlan(String identifier)
    {
        super(identifier);
    }

    /**
     * Called to get the structure plan for this world. Returns null if on client side
     *
     * @param world The world to get data for
     * @return The data for that world or null if it is client side
     */
    public static IStructurePlan get(World world)
    {
        // If we are on client side or the world is not the overworld return 0
        if (world.isRemote)
            return null;

        // Grab the storage object for this world
        MapStorage storage = world.getPerWorldStorage();
        // Get the saved heightmap data for this world
        StructurePlan structurePlan = (StructurePlan) storage.getOrLoadData(StructurePlan.class, IDENTIFIER);

        // If it does not exist, instantiate new structure plan and store it into the storage object
        if (structurePlan == null)
        {
            structurePlan = new StructurePlan();
            storage.setData(IDENTIFIER, structurePlan);
            structurePlan.markDirty();
        }

        // Return the data
        return structurePlan;
    }

    /**
     * Writes the contents of the structure plan to NBT. We don't write the structure data over and over,
     * instead write coord -> id pairs and then id -> structure data pairs into the NBT structure. This
     * ensures we don't use unnecessary memory
     *
     * @param nbt The NBT tag to write to
     * @return The same NBT tag as passed in
     */
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        // For each position add the tag "XCoord ZCoord" -> structureUUID
        NBTTagCompound chunkMap = new NBTTagCompound();
        chunkToStructure.forEach((position, structure) -> chunkMap.setTag(position.x + " " + position.z, new NBTTagString(structure.getUUID().toString())));
        // For each structure add the structureNBT
        NBTTagList structureData = new NBTTagList();
        chunkToStructure.values().stream().distinct().forEach(placedStructure -> structureData.appendTag(placedStructure.serializeNBT()));
        // Add the two different NBT compounds to the nbt
        nbt.setTag(NBT_CHUNK_MAP, chunkMap);
        nbt.setTag(NBT_STRUCTURE_DATA, structureData);
        return nbt;
    }

    /**
     * Reads the saved structure plan from NBT
     *
     * @param nbt The NBT data to read from
     */
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        // Grab the structureNBT list
        NBTTagList structureData = nbt.getTagList(NBT_STRUCTURE_DATA, net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND);
        // A temporary map of structureUUID -> structure to be used later
        Map<String, PlacedStructure> uuidToStructure = new HashMap<>();
        // For each structure data create a placed structure object and store it into the map
        for (int i = 0; i < structureData.tagCount(); i++)
        {
            PlacedStructure placedStructure = new PlacedStructure(structureData.getCompoundTagAt(i));
            uuidToStructure.put(placedStructure.getUUID().toString(), placedStructure);
        }

        // Grab the map of "chunkX chunkZ" -> structureUUID
        NBTTagCompound chunkMap = nbt.getCompoundTag(NBT_CHUNK_MAP);
        // Iterate over all keys in the NBT which should be all positions
        for (String positionKey : chunkMap.getKeySet())
        {
            // Parse the X coordinate from string to integer
            int x = NumberUtils.toInt(StringUtils.substringBefore(positionKey, " "), Integer.MAX_VALUE);
            // Parse the Z coordinate from string to integer
            int z = NumberUtils.toInt(StringUtils.substringAfter(positionKey, " "), Integer.MAX_VALUE);
            // Ensure both X and Z are valid
            if (x != Integer.MAX_VALUE && z != Integer.MAX_VALUE)
            {
                // Grab the value, should be the NBT data compound
                String structureUUID = chunkMap.getString(positionKey);
                // Use our map of structureUUID -> structure to avoid creating a bunch of placed structure objects
                this.chunkToStructure.put(new ChunkPos(x, z), uuidToStructure.get(structureUUID));
            }
            // We have an invalid tag, throw an error
            else
            {
                AfraidOfTheDark.INSTANCE.getLogger().error("Found an invalid key in the world saved data NBT: " + positionKey);
            }
        }
    }

    /**
     * Returns the structure in the given chunk or null if no such structure exists
     *
     * @param chunkPos The position to test
     * @return A structure if it exists, or null if no structure exists here
     */
    public PlacedStructure getPlacedStructureAt(ChunkPos chunkPos)
    {
        return this.chunkToStructure.getOrDefault(chunkPos, null);
    }

    /**
     * Returns an copy of the list of structures present in the world.
     *
     * @return A copy of the structures in the structure plan
     */
    @Override
    public List<PlacedStructure> getPlacedStructures()
    {
        return this.chunkToStructure.values().stream().distinct().collect(Collectors.toList());
    }

    /**
     * Returns true if a structure exists at the given chunk pos or false if it does not
     *
     * @param chunkPos The position to test
     * @return True if a structure exists at the position, or false otherwise
     */
    @Override
    public boolean structureExistsAt(ChunkPos chunkPos)
    {
        return this.chunkToStructure.containsKey(chunkPos);
    }

    /**
     * Tests if a given structure would fit if it was placed at a given position
     *
     * @param structure The structure to place
     * @param blockPos  The position to place the structure
     * @return True if the structure would fit without overlapping another structure, or false otherwise
     */
    @Override
    public boolean structureFitsAt(Structure structure, BlockPos blockPos)
    {
        // Grab the bottom left and top right that the structure would occupy
        ChunkPos bottomLeftCorner = new ChunkPos(blockPos);
        ChunkPos topRightCorner = new ChunkPos(blockPos.add(structure.getXWidth(), 0, structure.getZLength()));

        // Iterate over all chunks in the region, and test if each chunk is currently empty
        for (int chunkX = bottomLeftCorner.x; chunkX <= topRightCorner.x; chunkX++)
            for (int chunkZ = bottomLeftCorner.z; chunkZ <= topRightCorner.z; chunkZ++)
                // If any chunk is already planned then return false
                if (this.chunkToStructure.containsKey(new ChunkPos(chunkX, chunkZ)))
                    return false;

        return true;
    }

    /**
     * Called to place a given structure at a block position. This will overwrite any existing structures in the area
     *
     * @param structure The structure to place
     * @param data Any additional data the structure requires
     */
    @Override
    public void placeStructure(Structure structure, NBTTagCompound data)
    {
        // Extract the blockpos from the structure's data
        BlockPos blockPos = structure.getPosition(data);

        // Compute the bottom left and top right chunk position
        ChunkPos bottomLeftCorner = new ChunkPos(blockPos);
        ChunkPos topRightCorner = new ChunkPos(blockPos.add(structure.getXWidth(), 0, structure.getZLength()));

        // The structure entry to be placed down
        PlacedStructure placedStructure = new PlacedStructure(structure, data);

        // Iterate over all chunks in the region, and update their structure names
        for (int chunkX = bottomLeftCorner.x; chunkX <= topRightCorner.x; chunkX++)
            for (int chunkZ = bottomLeftCorner.z; chunkZ <= topRightCorner.z; chunkZ++)
                this.chunkToStructure.put(new ChunkPos(chunkX, chunkZ), placedStructure);
    }
}
