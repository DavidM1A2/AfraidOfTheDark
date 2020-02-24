package com.davidm1a2.afraidofthedark.common.capabilities.world

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World
import net.minecraft.world.storage.WorldSavedData
import org.apache.commons.lang3.math.NumberUtils

/**
 * Class used to store the overworld's heightmap
 *
 * @constructor Just calls super with our ID
 * @property posToHeight The actual heightmap that we are saving
 */
class OverworldHeightmap @JvmOverloads constructor(identifier: String = IDENTIFIER) : WorldSavedData(identifier),
    IHeightmap {
    private val posToHeight: MutableMap<ChunkPos, Pair<Int, Int>> = mutableMapOf()

    /**
     * Reads the saved data from NBT
     *
     * @param nbt The NBT data to read from
     */
    override fun readFromNBT(nbt: NBTTagCompound) {
        // Iterate over all keys in the NBT which should be all positions
        for (positionKey in nbt.keySet) {
            // Parse the position by splitting on space
            val positionXZ = positionKey.split(" ").toTypedArray()
            // Ensure there are 2 elements which should be X and Y
            if (positionXZ.size == 2) {
                // Parse the X coordinate from string to integer
                val x = NumberUtils.toInt(positionXZ[0], Int.MAX_VALUE)
                // Parse the Y coordinate from string to integer
                val z = NumberUtils.toInt(positionXZ[1], Int.MAX_VALUE)
                // Ensure both X and Y are valid
                if (x != Int.MAX_VALUE && z != Int.MAX_VALUE) {
                    val lowAndHigh = nbt.getIntArray(positionKey)
                    // Insert the position -> height
                    posToHeight[ChunkPos(x, z)] = lowAndHigh[0] to lowAndHigh[1]
                }
            } else {
                AfraidOfTheDark.INSTANCE.logger.error("Found an invalid key in the world saved data NBT: $positionKey")
            }
        }
    }

    /**
     * Writes the contents of the heightmap to NBT
     *
     * @param nbt The NBT tag to write to
     * @return The same NBT tag as passed in
     */
    override fun writeToNBT(nbt: NBTTagCompound): NBTTagCompound {
        // For each position add the tag "XCoord YCoord" -> low height, high height
        posToHeight.forEach { (position, height) ->
            nbt.setIntArray("${position.x} ${position.z}", intArrayOf(height.first, height.second))
        }
        return nbt
    }

    /**
     * Tests if we know the height of a given chunk position and quadrant
     *
     * @param chunkPos The chunk to test
     * @return True if we know the height of this position, false otherwise
     */
    override fun heightKnown(chunkPos: ChunkPos): Boolean {
        return posToHeight.containsKey(chunkPos)
    }

    /**
     * Sets the height of a given chunk position, x offset, and z offset
     *
     * @param chunkPos The position of the chunk
     * @param low      The lowest height of that chunk
     * @param high     The highest height of that chunk
     */
    override fun setHeight(chunkPos: ChunkPos, low: Int, high: Int) {
        posToHeight[chunkPos] = low to high
        markDirty()
    }

    /**
     * Gets the lowest height of a given chunk
     *
     * @param chunkPos The position of the chunk
     * @return The low height of that position
     */
    override fun getLowestHeight(chunkPos: ChunkPos): Int {
        return posToHeight.getOrDefault(chunkPos, INVALID).first
    }

    /**
     * Gets the highest height of a given chunk
     *
     * @param chunkPos The position of the chunk
     * @return The high height of that position
     */
    override fun getHighestHeight(chunkPos: ChunkPos): Int {
        return posToHeight.getOrDefault(chunkPos, INVALID).second
    }

    companion object {
        // The ID of the AOTD overworld heightmap
        private const val IDENTIFIER = Constants.MOD_ID + "_overworld_heightmap"
        // Pair of default Low/High values if the height is invalid
        private val INVALID = Int.MIN_VALUE to Int.MAX_VALUE

        /**
         * Called to get the height saved data for this world. Returns null if on client side or if the world is not the overworld
         *
         * @param world The world to get data for
         * @return The data for that world or null if it is not present
         */
        fun get(world: World): IHeightmap {
            // If we are on client side or the world is not the overworld return 0
            if (world.isRemote || world.provider.dimension != 0) {
                throw UnsupportedOperationException("Attempted to get the heightmap client side or for a non-overworld world!")
            }
            // Grab the storage object for this world
            val storage = world.perWorldStorage
            // Get the saved heightmap data for this world
            var heightmap = storage.getOrLoadData(OverworldHeightmap::class.java, IDENTIFIER) as OverworldHeightmap?
            // If it does not exist, instantiate new heightmap data and store it into the storage object
            if (heightmap == null) {
                heightmap = OverworldHeightmap()
                storage.setData(IDENTIFIER, heightmap)
                heightmap.markDirty()
            }
            // Return the data
            return heightmap
        }
    }
}