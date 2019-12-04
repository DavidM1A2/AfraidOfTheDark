package com.davidm1a2.afraidofthedark.common.capabilities.world

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World
import net.minecraft.world.storage.WorldSavedData
import java.util.concurrent.atomic.AtomicInteger

/**
 * Base class for all world data that requires storing player island positions
 *
 * @constructor just calls super with our ID
 * @property uniqueVisitors The current number of unique dimension visitors
 */
class IslandVisitorData @JvmOverloads constructor(identifier: String = IDENTIFIER) : WorldSavedData(identifier)
{
    private val uniqueVisitors = AtomicInteger(-1)

    /**
     * Reads the saved data from NBT
     *
     * @param nbt The NBT data to read from
     */
    override fun readFromNBT(nbt: NBTTagCompound)
    {
        uniqueVisitors.set(nbt.getInteger(NBT_UNIQUE_VISITORS))
    }

    /**
     * Writes the contents of the heightmap to NBT
     *
     * @param compound The NBT tag to write to
     * @return The same NBT tag as passed in
     */
    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound
    {
        compound.setInteger(NBT_UNIQUE_VISITORS, uniqueVisitors.get())
        return compound
    }

    /**
     * Adds a new visitor to the unique visitors list and returns the newly updated count of unique visitors
     *
     * @return The newly updated count of unique visitors
     */
    fun addAndReturnNewVisitor(): Int
    {
        return uniqueVisitors.incrementAndGet()
    }

    companion object
    {
        // A list of valid dimensions
        private val VALID_DIMENSIONS = setOf(ModDimensions.NIGHTMARE.id, ModDimensions.VOID_CHEST.id)
        // The NBT key for the unique visitors value
        private const val NBT_UNIQUE_VISITORS = "unique_visitors"
        // The ID of the AOTD nightmare data
        private const val IDENTIFIER = Constants.MOD_ID + "_island_visitor_data"

        /**
         * Called to get the island visitor data for this world. Returns null if on client side or if the world is not supported
         *
         * @param world The world to get data for
         * @return The data for that world or null if it is not present
         */
        fun get(world: World): IslandVisitorData?
        {
            // If we are on client side or the world is not supported return null
            if (world.isRemote || !VALID_DIMENSIONS.contains(world.provider.dimension))
            {
                return null
            }
            // Grab the storage object for this world
            val storage = world.perWorldStorage
            // Get the saved data for this world
            var visitorData = storage.getOrLoadData(IslandVisitorData::class.java, IDENTIFIER) as IslandVisitorData?
            // If it does not exist, instantiate new data and store it into the storage object
            if (visitorData == null)
            {
                visitorData = IslandVisitorData()
                storage.setData(IDENTIFIER, visitorData)
                visitorData.markDirty()
            }
            // Return the data
            return visitorData
        }
    }
}