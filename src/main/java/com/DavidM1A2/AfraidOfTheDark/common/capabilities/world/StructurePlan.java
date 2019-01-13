package com.DavidM1A2.afraidofthedark.common.capabilities.world;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.Structure;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.math.NumberUtils;

import javax.vecmath.Point2i;
import java.util.HashMap;
import java.util.Map;

/**
 * Class used to store a world's structure plan
 */
public class StructurePlan extends WorldSavedData
{
	// The ID of the AOTD structure plan
	private static final String IDENTIFIER = Constants.MOD_ID + "_structure_plan";

	// Grab a reference to the structure registry which contains all structures
	private static final IForgeRegistry<Structure> STRUCTURE_REGISTRY = GameRegistry.findRegistry(Structure.class);

	// The actual structure plan map that we are saving
	private Map<Point2i, String> chunkToStructureID = new HashMap<>();
	// A map of chunk to position that the structure at that position is originated at
	private Map<Point2i, BlockPos> chunkToOrigin = new HashMap<>();

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
	public static StructurePlan get(World world)
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
	 * Reads the saved structure plan from NBT
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
			String[] positionXY = positionKey.split(" ");
			// Ensure there are 2 elements which should be X and Y
			if (positionXY.length == 2)
			{
				// Parse the X coordinate from string to integer
				int x = NumberUtils.toInt(positionXY[0], Integer.MAX_VALUE);
				// Parse the Y coordinate from string to integer
				int y = NumberUtils.toInt(positionXY[1], Integer.MAX_VALUE);
				// Ensure both X and Y are valid
				if (x != Integer.MAX_VALUE && y != Integer.MAX_VALUE)
				{
					// Grab the value, should be in the form structureID xOrigin yOrigin zOrigin
					String value = nbt.getString(positionKey);
					// Split the value by spaces
					String[] idXYZ = value.split(" ");
					// Ensure we got 4 values
					if (idXYZ.length == 4)
					{
						// Parse the X origin coordinate from string to integer
						int xOrigin = NumberUtils.toInt(idXYZ[1], Integer.MAX_VALUE);
						// Parse the Y origin coordinate from string to integer
						int yOrigin = NumberUtils.toInt(idXYZ[2], Integer.MAX_VALUE);
						// Parse the Z origin coordinate from string to integer
						int zOrigin = NumberUtils.toInt(idXYZ[3], Integer.MAX_VALUE);
						// Ensure we got 4 valid values
						if (xOrigin != Integer.MAX_VALUE && yOrigin != Integer.MAX_VALUE && zOrigin != Integer.MAX_VALUE)
						{
							// Turn x,y into position
							Point2i position = new Point2i(x, y);
							// Insert the position -> structure ID
							chunkToStructureID.put(position, idXYZ[0]);
							// Insert the position -> BlockPos(xOrigin, yOrigin, zOrigin)
							chunkToOrigin.put(position, new BlockPos(xOrigin, yOrigin, zOrigin));
						}
					}
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
	 * Writes the contents of the structure plan to NBT
	 *
	 * @param nbt The NBT tag to write to
	 * @return The same NBT tag as passed in
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		// For each position add the tag "XCoord YCoord" -> structureID xOrigin yOrigin zOrigin
		chunkToStructureID.forEach((position, structureID) ->
		{
			BlockPos blockPos = chunkToOrigin.get(position);
			String value = structureID + " " + blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ();
			nbt.setString(position.x + " " + position.y, value);
		});

		return nbt;
	}

	/**
	 * Returns the structure in the given chunk or null if no such structure exists
	 *
	 * @param chunkPos The position to test
	 * @return A structure if it exists, or null if no structure exists here
	 */
	public Structure getStructureAt(ChunkPos chunkPos)
	{
		// Grab the structure ID at the given position
		String structureID = this.chunkToStructureID.getOrDefault(new Point2i(chunkPos.x, chunkPos.z), null);

		// If the ID is non-null, return the structure, otherwise return null
		if (structureID != null)
			return STRUCTURE_REGISTRY.getValue(new ResourceLocation(structureID));
		return null;
	}

	/**
	 * Returns the origin of a structure that covers the chunk position if it exists, or null if it does not
	 *
	 * @param chunkPos The position to test
	 * @return The origin of the structure that goes through the chunk position or null if it does not exist
	 */
	public BlockPos getStructureOrigin(ChunkPos chunkPos)
	{
		return this.chunkToOrigin.getOrDefault(new Point2i(chunkPos.x, chunkPos.z), null);
	}

	/**
	 * Returns true if a structure exists at the given chunk pos or false if it does not
	 *
	 * @param chunkPos The position to test
	 * @return True if a structure exists at the position, or false otherwise
	 */
	public boolean structureExistsAt(ChunkPos chunkPos)
	{
		return this.chunkToStructureID.containsKey(new Point2i(chunkPos.x, chunkPos.z));
	}

	/**
	 * Tests if a given structure would fit if it was placed at a given position
	 *
	 * @param structure The structure to place
	 * @param blockPos The position to place the structure
	 * @return True if the structure would fit without overlapping another structure, or false otherwise
	 */
	public boolean structureFitsAt(Structure structure, BlockPos blockPos)
	{
		// Grab the bottom left and top right that the structure would occupy
		ChunkPos bottomLeftCorner = new ChunkPos(blockPos);
		ChunkPos topRightCorner = new ChunkPos(blockPos.add(structure.getXWidth(), 0, structure.getZLength()));

		// Iterate over all chunks in the region, and test if each chunk is currently empty
		for (int chunkX = bottomLeftCorner.x; chunkX <= topRightCorner.x; chunkX++)
			for (int chunkZ = bottomLeftCorner.z; chunkZ <= topRightCorner.z; chunkZ++)
				// If any chunk is already planned then return false
				if (this.chunkToStructureID.containsKey(new Point2i(chunkX, chunkZ)))
					return false;

		return true;
	}

	/**
	 * Called to place a given structure at a block position. This will overwrite any existing structures in the area
	 *
	 * @param structure The structure to place
	 * @param blockPos The position to place the structure at
	 */
	public void placeStructureAt(Structure structure, BlockPos blockPos)
	{
		// Compute the bottom left and top right chunk position
		ChunkPos bottomLeftCorner = new ChunkPos(blockPos);
		ChunkPos topRightCorner = new ChunkPos(blockPos.add(structure.getXWidth(), 0, structure.getZLength()));

		// Compute the structure's name from the registry name
		String structureName = structure.getRegistryName().toString();

		// Iterate over all chunks in the region, and update their structure names
		for (int chunkX = bottomLeftCorner.x; chunkX <= topRightCorner.x; chunkX++)
		{
			for (int chunkZ = bottomLeftCorner.z; chunkZ <= topRightCorner.z; chunkZ++)
			{
				Point2i position = new Point2i(chunkX, chunkZ);
				this.chunkToStructureID.put(position, structureName);
				this.chunkToOrigin.put(position, blockPos);
			}
		}
	}
}
