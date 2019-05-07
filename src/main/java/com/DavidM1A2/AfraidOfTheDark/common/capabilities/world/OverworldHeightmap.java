package com.DavidM1A2.afraidofthedark.common.capabilities.world;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import javafx.util.Pair;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Class used to store the overworld's heightmap
 */
public class OverworldHeightmap extends WorldSavedData implements IHeightmap
{
    // The ID of the AOTD overworld heightmap
    private static final String IDENTIFIER = Constants.MOD_ID + "_overworld_heightmap";
    // Pair of default Low/High values if the height is invalid
    private static final Pair<Integer, Integer> INVALID = new Pair<>(Integer.MIN_VALUE, Integer.MAX_VALUE);
    // The actual heightmap that we are saving
    private Map<ChunkPos, Pair<Integer, Integer>> posToHeight = new HashMap<>();

    /**
     * Constructor just calls super with our ID
     */
    public OverworldHeightmap()
    {
        this(IDENTIFIER);
    }

    /**
     * Constructor where we can supply our own ID
     *
     * @param identifier The ID to use for this data
     */
    public OverworldHeightmap(String identifier)
    {
        super(identifier);
    }

    /**
     * Called to get the height saved data for this world. Returns null if on client side or if the world is not the overworld
     *
     * @param world The world to get data for
     * @return The data for that world or null if it is not present
     */
    public static IHeightmap get(World world)
    {
        // If we are on client side or the world is not the overworld return 0
        if (world.isRemote || world.provider.getDimension() != 0)
        {
            throw new UnsupportedOperationException("Attempted to get the heightmap client side or for a non-overworld world!");
        }

        // Grab the storage object for this world
        MapStorage storage = world.getPerWorldStorage();
        // Get the saved heightmap data for this world
        OverworldHeightmap heightmap = (OverworldHeightmap) storage.getOrLoadData(OverworldHeightmap.class, IDENTIFIER);

        // If it does not exist, instantiate new heightmap data and store it into the storage object
        if (heightmap == null)
        {
            heightmap = new OverworldHeightmap();
            storage.setData(IDENTIFIER, heightmap);
            heightmap.markDirty();
        }

        // Return the data
        return heightmap;
    }

    /**
     * Reads the saved data from NBT
     *
     * @param nbt The NBT data to read from
     */
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        // Iterate over all keys in the NBT which should be all positions
        for (String positionKey : nbt.getKeySet())
        {
            // Parse the position by splitting on space
            String[] positionXZ = positionKey.split(" ");
            // Ensure there are 2 elements which should be X and Y
            if (positionXZ.length == 2)
            {
                // Parse the X coordinate from string to integer
                int x = NumberUtils.toInt(positionXZ[0], Integer.MAX_VALUE);
                // Parse the Y coordinate from string to integer
                int z = NumberUtils.toInt(positionXZ[1], Integer.MAX_VALUE);
                // Ensure both X and Y are valid
                if (x != Integer.MAX_VALUE && z != Integer.MAX_VALUE)
                {
                    int[] lowAndHigh = nbt.getIntArray(positionKey);
                    // Insert the position -> height
                    posToHeight.put(new ChunkPos(x, z), new Pair<>(lowAndHigh[0], lowAndHigh[1]));
                }
            }
            // We have an invalid tag, throw an error
            else
            {
                AfraidOfTheDark.INSTANCE.getLogger().error("Found an invalid key in the world saved data NBT: " + positionKey);
            }
        }
    }

    /**
     * Writes the contents of the heightmap to NBT
     *
     * @param nbt The NBT tag to write to
     * @return The same NBT tag as passed in
     */
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        // For each position add the tag "XCoord YCoord" -> low height, high height
        this.posToHeight.forEach((position, height) -> nbt.setIntArray(position.x + " " + position.z, new int[]{height.getKey(), height.getValue()}));
        return nbt;
    }

    /**
     * Tests if we know the height of a given chunk position and quadrant
     *
     * @param chunkPos The chunk to test
     * @return True if we know the height of this position, false otherwise
     */
    @Override
    public boolean heightKnown(ChunkPos chunkPos)
    {
        return this.posToHeight.containsKey(chunkPos);
    }

    /**
     * Sets the height of a given chunk position, x offset, and z offset
     *
     * @param chunkPos The position of the chunk
     * @param low      The lowest height of that chunk
     * @param high     The highest height of that chunk
     */
    @Override
    public void setHeight(ChunkPos chunkPos, int low, int high)
    {
        this.posToHeight.put(chunkPos, new Pair<>(low, high));
        this.markDirty();
    }

    /**
     * Gets the lowest height of a given chunk
     *
     * @param chunkPos The position of the chunk
     * @return The low height of that position
     */
    @Override
    public int getLowestHeight(ChunkPos chunkPos)
    {
        return this.posToHeight.getOrDefault(chunkPos, INVALID).getKey();
    }

    /**
     * Gets the highest height of a given chunk
     *
     * @param chunkPos The position of the chunk
     * @return The high height of that position
     */
    @Override
    public int getHighestHeight(ChunkPos chunkPos)
    {
        return this.posToHeight.getOrDefault(chunkPos, INVALID).getValue();
    }
}
