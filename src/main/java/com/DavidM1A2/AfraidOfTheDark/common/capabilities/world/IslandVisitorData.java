package com.DavidM1A2.afraidofthedark.common.capabilities.world;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.constants.ModDimensions;
import com.google.common.collect.ImmutableSet;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Base class for all world data that requires storing player island positions
 */
public class IslandVisitorData extends WorldSavedData
{
    // A list of valid dimensions
    private static final Set<Integer> VALID_DIMENSIONS = ImmutableSet.of(ModDimensions.NIGHTMARE.getId(), ModDimensions.VOID_CHEST.getId());

    // The NBT key for the unique visitors value
    private static final String NBT_UNIQUE_VISITORS = "unique_visitors";

    // The current number of unique dimension visitors
    private AtomicInteger uniqueVisitors = new AtomicInteger(-1);

    // The ID of the AOTD nightmare data
    private static final String IDENTIFIER = Constants.MOD_ID + "_island_visitor_data";

    /**
     * Constructor just calls super with our ID
     */
    public IslandVisitorData()
    {
        this(IDENTIFIER);
    }

    /**
     * Constructor where we can supply our own ID
     *
     * @param identifier The ID to use for this data
     */
    public IslandVisitorData(String identifier)
    {
        super(identifier);
    }

    /**
     * Called to get the island visitor data for this world. Returns null if on client side or if the world is not supported
     *
     * @param world The world to get data for
     * @return The data for that world or null if it is not present
     */
    public static IslandVisitorData get(World world)
    {
        // If we are on client side or the world is not supported return null
        if (world.isRemote || !VALID_DIMENSIONS.contains(world.provider.getDimension()))
            return null;

        // Grab the storage object for this world
        MapStorage storage = world.getPerWorldStorage();
        // Get the saved data for this world
        IslandVisitorData visitorData = (IslandVisitorData) storage.getOrLoadData(IslandVisitorData.class, IDENTIFIER);

        // If it does not exist, instantiate new data and store it into the storage object
        if (visitorData == null)
        {
            visitorData = new IslandVisitorData();
            storage.setData(IDENTIFIER, visitorData);
            visitorData.markDirty();
        }

        // Return the data
        return visitorData;
    }

    /**
     * Reads the saved data from NBT
     *
     * @param nbt The NBT data to read from
     */
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        this.uniqueVisitors.set(nbt.getInteger(NBT_UNIQUE_VISITORS));
    }

    /**
     * Writes the contents of the heightmap to NBT
     *
     * @param compound The NBT tag to write to
     * @return The same NBT tag as passed in
     */
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setInteger(NBT_UNIQUE_VISITORS, this.uniqueVisitors.get());

        return compound;
    }

    /**
     * Adds a new visitor to the unique visitors list and returns the newly updated count of unique visitors
     *
     * @return The newly updated count of unique visitors
     */
    public int addAndReturnNewVisitor()
    {
        return this.uniqueVisitors.incrementAndGet();
    }
}
