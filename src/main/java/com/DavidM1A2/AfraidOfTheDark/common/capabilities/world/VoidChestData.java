package com.DavidM1A2.afraidofthedark.common.capabilities.world;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class representing all data store about the void chest world
 */
public class VoidChestData extends WorldSavedData
{
	// The ID of the AOTD void chest data
	private static final String IDENTIFIER = Constants.MOD_ID + "_void_chest_data";

	// The NBT key for the unique visitors value
	private static final String NBT_UNIQUE_VISITORS = "unique_visitors";

	// The current number of unique void chest dimension visitors
	private AtomicInteger uniqueVoidChestVisitors = new AtomicInteger(-1);

	/**
	 * Constructor just calls super with our ID
	 */
	public VoidChestData()
	{
		this(IDENTIFIER);
	}

	/**
	 * Constructor where we can supply our own ID
	 *
	 * @param identifier The ID to use for this data
	 */
	public VoidChestData(String identifier)
	{
		super(identifier);
	}

	/**
	 * Called to get the void chest data for this world. Returns null if on client side or if the world is not the void chest
	 *
	 * @param world The world to get data for
	 * @return The data for that world or null if it is not present
	 */
	public static VoidChestData get(World world)
	{
		// If we are on client side or the world is not the void chest dimension return 0
		// TODO: FILL IN VOID CHEST DIMENSION ID!!!!
		if (world.isRemote || world.provider.getDimension() != 0)
			return null;

		// Grab the storage object for this world
		MapStorage storage = world.getPerWorldStorage();
		// Get the saved void chest data for this world
		VoidChestData voidChestData = (VoidChestData) storage.getOrLoadData(VoidChestData.class, IDENTIFIER);

		// If it does not exist, instantiate new void chest data and store it into the storage object
		if (voidChestData == null)
		{
			voidChestData = new VoidChestData();
			storage.setData(IDENTIFIER, voidChestData);
			voidChestData.markDirty();
		}

		// Return the data
		return voidChestData;
	}

	/**
	 * Reads the saved data from NBT
	 *
	 * @param nbt The NBT data to read from
	 */
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.uniqueVoidChestVisitors.set(nbt.getInteger(NBT_UNIQUE_VISITORS));
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
		compound.setInteger(NBT_UNIQUE_VISITORS, this.uniqueVoidChestVisitors.get());

		return compound;
	}

	/**
	 * Adds a new visitor to the unique visitors list and returns the newly updated count of unique visitors
	 *
	 * @return The newly updated count of unique visitors
	 */
	public int addAndReturnNewVisitor()
	{
		return this.uniqueVoidChestVisitors.incrementAndGet();
	}
}
