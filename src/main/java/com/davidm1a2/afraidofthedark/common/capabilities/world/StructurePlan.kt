package com.davidm1a2.afraidofthedark.common.capabilities.world

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.Structure
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagString
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World
import net.minecraft.world.storage.WorldSavedData
import net.minecraftforge.common.util.Constants
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.math.NumberUtils

/**
 * Class used to store a world's structure plan
 *
 * @constructor Just calls super with our ID
 * @property chunkToStructure The actual structure plan map that we are saving
 */
class StructurePlan @JvmOverloads constructor(identifier: String = IDENTIFIER) : WorldSavedData(identifier), IStructurePlan
{
    private val chunkToStructure: MutableMap<ChunkPos, PlacedStructure> = mutableMapOf()

    /**
     * Writes the contents of the structure plan to NBT. We don't write the structure data over and over,
     * instead write coord -> id pairs and then id -> structure data pairs into the NBT structure. This
     * ensures we don't use unnecessary memory
     *
     * @param nbt The NBT tag to write to
     * @return The same NBT tag as passed in
     */
    override fun writeToNBT(nbt: NBTTagCompound): NBTTagCompound
    {
        // For each position add the tag "XCoord ZCoord" -> structureUUID
        val chunkMap = NBTTagCompound()
        chunkToStructure.forEach()
        { (position: ChunkPos, structure: PlacedStructure) ->
            chunkMap.setTag("${position.x} ${position.z}", NBTTagString(structure.uuid.toString()))
        }

        // For each structure add the structureNBT
        val structureData = NBTTagList()
        chunkToStructure.values.distinct().forEach { structureData.appendTag(it.serializeNBT()) }
        // Add the two different NBT compounds to the nbt
        nbt.setTag(NBT_CHUNK_MAP, chunkMap)
        nbt.setTag(NBT_STRUCTURE_DATA, structureData)
        return nbt
    }

    /**
     * Reads the saved structure plan from NBT
     *
     * @param nbt The NBT data to read from
     */
    override fun readFromNBT(nbt: NBTTagCompound)
    {
        // Grab the structureNBT list
        val structureData = nbt.getTagList(NBT_STRUCTURE_DATA, Constants.NBT.TAG_COMPOUND)
        // A temporary map of structureUUID -> structure to be used later
        val uuidToStructure: MutableMap<String, PlacedStructure> = mutableMapOf()
        // For each structure data create a placed structure object and store it into the map
        for (i in 0 until structureData.tagCount())
        {
            val placedStructure = PlacedStructure(structureData.getCompoundTagAt(i))
            uuidToStructure[placedStructure.uuid.toString()] = placedStructure
        }

        // Grab the map of "chunkX chunkZ" -> structureUUID
        val chunkMap = nbt.getCompoundTag(NBT_CHUNK_MAP)
        // Iterate over all keys in the NBT which should be all positions
        for (positionKey in chunkMap.keySet)
        {
            // Parse the X coordinate from string to integer
            val x = NumberUtils.toInt(StringUtils.substringBefore(positionKey, " "), Int.MAX_VALUE)
            // Parse the Z coordinate from string to integer
            val z = NumberUtils.toInt(StringUtils.substringAfter(positionKey, " "), Int.MAX_VALUE)
            // Ensure both X and Z are valid
            if (x != Int.MAX_VALUE && z != Int.MAX_VALUE)
            {
                // Grab the value, should be the NBT data compound
                val structureUUID = chunkMap.getString(positionKey)
                // Use our map of structureUUID -> structure to avoid creating a bunch of placed structure objects
                chunkToStructure[ChunkPos(x, z)] = uuidToStructure[structureUUID]!!
            }
            else
            {
                AfraidOfTheDark.INSTANCE.logger.error("Found an invalid key in the world saved data NBT: $positionKey")
            }
        }
    }

    /**
     * Returns the structure in the given chunk or null if no such structure exists
     *
     * @param chunkPos The position to test
     * @return A structure if it exists, or null if no structure exists here
     */
    override fun getPlacedStructureAt(chunkPos: ChunkPos): PlacedStructure?
    {
        return chunkToStructure.getOrDefault(chunkPos, null)
    }

    /**
     * Returns an copy of the list of structures present in the world.
     *
     * @return A copy of the structures in the structure plan
     */
    override fun getPlacedStructures(): List<PlacedStructure>
    {
        return chunkToStructure.values.distinct().toList()
    }

    /**
     * Returns true if a structure exists at the given chunk pos or false if it does not
     *
     * @param chunkPos The position to test
     * @return True if a structure exists at the position, or false otherwise
     */
    override fun structureExistsAt(chunkPos: ChunkPos): Boolean
    {
        return chunkToStructure.containsKey(chunkPos)
    }

    /**
     * Tests if a given structure would fit if it was placed at a given position
     *
     * @param structure The structure to place
     * @param blockPos  The position to place the structure
     * @return True if the structure would fit without overlapping another structure, or false otherwise
     */
    override fun structureFitsAt(structure: Structure, blockPos: BlockPos): Boolean
    {
        // Grab the bottom left and top right that the structure would occupy
        val bottomLeftCorner = ChunkPos(blockPos)
        val topRightCorner = ChunkPos(blockPos.add(structure.xWidth, 0, structure.zLength))
        // Iterate over all chunks in the region, and test if each chunk is currently empty
        for (chunkX in bottomLeftCorner.x..topRightCorner.x)
        {
            for (chunkZ in bottomLeftCorner.z..topRightCorner.z)
            {
                // If any chunk is already planned then return false
                if (chunkToStructure.containsKey(ChunkPos(chunkX, chunkZ)))
                {
                    return false
                }
            }
        }
        return true
    }

    /**
     * Called to place a given structure at a block position. This will overwrite any existing structures in the area
     *
     * @param structure The structure to place
     * @param data      Any additional data the structure requires
     */
    override fun placeStructure(structure: Structure, data: NBTTagCompound)
    {
        // Extract the blockpos from the structure's data
        val blockPos = structure.getPosition(data)

        // Compute the bottom left and top right chunk position
        val bottomLeftCorner = ChunkPos(blockPos)
        val topRightCorner = ChunkPos(blockPos.add(structure.xWidth, 0, structure.zLength))
        // The structure entry to be placed down
        val placedStructure = PlacedStructure(structure, data)
        // Iterate over all chunks in the region, and update their structure names
        for (chunkX in bottomLeftCorner.x..topRightCorner.x)
        {
            for (chunkZ in bottomLeftCorner.z..topRightCorner.z)
            {
                chunkToStructure[ChunkPos(chunkX, chunkZ)] = placedStructure
            }
        }
        // Make sure to write chnages to disk
        markDirty()
    }

    companion object
    {
        // The ID of the AOTD structure plan
        private const val IDENTIFIER = com.davidm1a2.afraidofthedark.common.constants.Constants.MOD_ID + "_structure_plan"
        // The NBT compound tag keys
        private const val NBT_CHUNK_MAP = "chunk_map"
        private const val NBT_STRUCTURE_DATA = "structure_data"

        /**
         * Called to get the structure plan for this world. Returns null if on client side
         *
         * @param world The world to get data for
         * @return The data for that world or null if it is client side
         */
        operator fun get(world: World): IStructurePlan?
        {
            // If we are on client side or the world is not the overworld return 0
            if (world.isRemote)
            {
                return null
            }
            // Grab the storage object for this world
            val storage = world.perWorldStorage
            // Get the saved heightmap data for this world
            var structurePlan = storage.getOrLoadData(StructurePlan::class.java, IDENTIFIER) as StructurePlan?
            // If it does not exist, instantiate new structure plan and store it into the storage object
            if (structurePlan == null)
            {
                structurePlan = StructurePlan()
                storage.setData(IDENTIFIER, structurePlan)
                structurePlan.markDirty()
            }
            // Return the data
            return structurePlan
        }
    }
}