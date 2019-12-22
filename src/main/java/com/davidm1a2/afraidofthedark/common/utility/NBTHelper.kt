package com.davidm1a2.afraidofthedark.common.utility

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompressedStreamTools
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.server.MinecraftServer
import org.apache.commons.lang3.exception.ExceptionUtils
import java.io.File
import java.io.FileInputStream
import java.io.IOException

/**
 * Class that lets us work with itemstack NBT more easily
 */
object NBTHelper
{
    /**
     * Returns a list of all saved player nbts, if the flag is set then player data is saved to disk before being read
     *
     * @param minecraftServer The minecraft server to get the NBTs from
     * @param saveFirst       True if the existing online player data should be written to disk first, false otherwise
     * @return A list of player NBTTagCompounds containing all players both offline and online
     */
    fun getAllSavedPlayerNBTs(minecraftServer: MinecraftServer, saveFirst: Boolean): List<NBTTagCompound>
    {
        // Write all player data to disk if the flag is set
        if (saveFirst)
        {
            minecraftServer.playerList.saveAllPlayerData()
        }

        // A list of player NBTs to return
        val toReturn = mutableListOf<NBTTagCompound>()

        // All worlds share a single save handler so we can just use the overworld's save handler to grab all player data even players in other dimensions
        val saveHandler = minecraftServer.worlds[0].saveHandler

        // Grab the playerdata directory that stores all player's NBT data
        val playerDataDirectory = File(saveHandler.worldDirectory, "playerdata")

        // Iterate over each player's UUID file
        for (playerUUID in saveHandler.playerNBTManager.availablePlayerDat)
        {
            // Create a file pointed at that player's data
            val playerData = File(playerDataDirectory, "$playerUUID.dat")

            // If the player data exists, is a file, and can be read read the file
            if (playerData.exists() && playerData.isFile && playerData.canRead())
            {
                // Open a file input stream to the file
                try
                {
                    FileInputStream(playerData).use()
                    {
                        // Read the player data into NBT
                        val playerDataCompound = CompressedStreamTools.readCompressed(it)
                        // Store the data compound to return
                        toReturn.add(playerDataCompound)
                    }
                }
                // If something goes wrong log an error
                catch (e: IOException)
                {
                    AfraidOfTheDark.INSTANCE.logger.error(
                        "Could not read player data for file ${playerData.absolutePath}, exception was:\n${ExceptionUtils.getStackTrace(e)}"
                    )
                }
            }
        }
        return toReturn
    }

    /**
     * Tests if a given itemstack has the required key name
     *
     * @param itemStack The item stack to test
     * @param keyName   The key to search for in the item stack
     * @return True if the itemstack has the key, false otherwise
     */
    fun hasTag(itemStack: ItemStack, keyName: String): Boolean
    {
        return itemStack.tagCompound != null && itemStack.tagCompound!!.hasKey(keyName)
    }

    /**
     * Removes a tag off of a given itemstack
     *
     * @param itemStack The item stack to remove from
     * @param keyName   The key to remove
     * @return
     */
    fun removeTag(itemStack: ItemStack, keyName: String)
    {
        if (hasTag(itemStack, keyName))
        {
            itemStack.tagCompound!!.removeTag(keyName)
        }
    }

    /**
     * Initializes the NBT Tag Compound for the given ItemStack if it is null
     *
     * @param itemStack The ItemStack for which its NBT Tag Compound is being checked for initialization
     */
    private fun initNBTTagCompound(itemStack: ItemStack)
    {
        // Make sure the item stack has an NBT tag compound, if not add it
        if (itemStack.tagCompound == null)
        {
            itemStack.tagCompound = NBTTagCompound()
        }
    }

    /**
     * Sets an item stack's key name to store a long value
     *
     * @param itemStack The item stack to update
     * @param keyName   The name of the key to store the long with
     * @param keyValue  The long to store with that key value
     */
    fun setLong(itemStack: ItemStack, keyName: String, keyValue: Long)
    {
        // Make sure the item stack has an NBT tag compound, if not add it
        initNBTTagCompound(itemStack)
        // Set the long value onto the tag compound
        itemStack.tagCompound!!.setLong(keyName, keyValue)
    }

    /**
     * Gets a long value from the NBT on the item stack given a key
     *
     * @param itemStack The itemstack to read NBT data from
     * @param keyName   The key that contains the data we want to retrieve
     * @return The long value represented by the key or null if it was not present
     */
    fun getLong(itemStack: ItemStack, keyName: String): Long?
    {
        return if (hasTag(itemStack, keyName))
        {
            itemStack.tagCompound!!.getLong(keyName)
        }
        else null
    }

    /**
     * Sets an item stack's key name to store a string value
     *
     * @param itemStack The item stack to update
     * @param keyName   The name of the key to store the string with
     * @param keyValue  The string to store with that key value
     */
    fun setString(itemStack: ItemStack, keyName: String, keyValue: String)
    {
        // Make sure the item stack has an NBT tag compound, if not add it
        initNBTTagCompound(itemStack)
        // Set the string value onto the tag compound
        itemStack.tagCompound!!.setString(keyName, keyValue)
    }

    /**
     * Gets a string value from the NBT on the item stack given a key
     *
     * @param itemStack The itemstack to read NBT data from
     * @param keyName   The key that contains the data we want to retrieve
     * @return The string value represented by the key or null if it was not present
     */
    fun getString(itemStack: ItemStack, keyName: String): String?
    {
        return if (hasTag(itemStack, keyName))
        {
            itemStack.tagCompound!!.getString(keyName)
        }
        else null
    }

    /**
     * Sets an item stack's key name to store a boolean value
     *
     * @param itemStack The item stack to update
     * @param keyName   The name of the key to store the boolean with
     * @param keyValue  The boolean to store with that key value
     */
    fun setBoolean(itemStack: ItemStack, keyName: String, keyValue: Boolean)
    {
        // Make sure the item stack has an NBT tag compound, if not add it
        initNBTTagCompound(itemStack)
        // Set the boolean value onto the tag compound
        itemStack.tagCompound!!.setBoolean(keyName, keyValue)
    }

    /**
     * Gets a boolean value from the NBT on the item stack given a key
     *
     * @param itemStack The itemstack to read NBT data from
     * @param keyName   The key that contains the data we want to retrieve
     * @return The boolean value represented by the key or null if it was not present
     */
    fun getBoolean(itemStack: ItemStack, keyName: String): Boolean?
    {
        return if (hasTag(itemStack, keyName))
        {
            itemStack.tagCompound!!.getBoolean(keyName)
        }
        else null
    }

    /**
     * Sets an item stack's key name to store a byte value
     *
     * @param itemStack The item stack to update
     * @param keyName   The name of the key to store the byte with
     * @param keyValue  The byte to store with that key value
     */
    fun setByte(itemStack: ItemStack, keyName: String, keyValue: Byte)
    {
        // Make sure the item stack has an NBT tag compound, if not add it
        initNBTTagCompound(itemStack)
        // Set the byte value onto the tag compound
        itemStack.tagCompound!!.setByte(keyName, keyValue)
    }

    /**
     * Gets a byte value from the NBT on the item stack given a key
     *
     * @param itemStack The itemstack to read NBT data from
     * @param keyName   The key that contains the data we want to retrieve
     * @return The byte value represented by the key or null if it was not present
     */
    fun getByte(itemStack: ItemStack, keyName: String): Byte?
    {
        return if (hasTag(itemStack, keyName))
        {
            itemStack.tagCompound!!.getByte(keyName)
        }
        else null
    }

    /**
     * Sets an item stack's key name to store a short value
     *
     * @param itemStack The item stack to update
     * @param keyName   The name of the key to store the short with
     * @param keyValue  The short to store with that key value
     */
    fun setShort(itemStack: ItemStack, keyName: String, keyValue: Short)
    {
        // Make sure the item stack has an NBT tag compound, if not add it
        initNBTTagCompound(itemStack)
        // Set the short value onto the tag compound
        itemStack.tagCompound!!.setShort(keyName, keyValue)
    }

    /**
     * Gets a short value from the NBT on the item stack given a key
     *
     * @param itemStack The itemstack to read NBT data from
     * @param keyName   The key that contains the data we want to retrieve
     * @return The short value represented by the key or null if it was not present
     */
    fun getShort(itemStack: ItemStack, keyName: String): Short?
    {
        return if (hasTag(itemStack, keyName))
        {
            itemStack.tagCompound!!.getShort(keyName)
        }
        else null
    }

    /**
     * Sets an item stack's key name to store an integer value
     *
     * @param itemStack The item stack to update
     * @param keyName   The name of the key to store the integer with
     * @param keyValue  The integer to store with that key value
     */
    fun setInteger(itemStack: ItemStack, keyName: String, keyValue: Int)
    {
        // Make sure the item stack has an NBT tag compound, if not add it
        initNBTTagCompound(itemStack)
        // Set the integer value onto the tag compound
        itemStack.tagCompound!!.setInteger(keyName, keyValue)
    }

    /**
     * Gets an integer value from the NBT on the item stack given a key
     *
     * @param itemStack The itemstack to read NBT data from
     * @param keyName   The key that contains the data we want to retrieve
     * @return The integer value represented by the key or null if it was not present
     */
    fun getInteger(itemStack: ItemStack, keyName: String): Int?
    {
        return if (hasTag(itemStack, keyName))
        {
            itemStack.tagCompound!!.getInteger(keyName)
        }
        else null
    }

    /**
     * Sets an item stack's key name to store a float value
     *
     * @param itemStack The item stack to update
     * @param keyName   The name of the key to store the float with
     * @param keyValue  The float to store with that key value
     */
    fun setFloat(itemStack: ItemStack, keyName: String, keyValue: Float)
    {
        // Make sure the item stack has an NBT tag compound, if not add it
        initNBTTagCompound(itemStack)
        // Set the float value onto the tag compound
        itemStack.tagCompound!!.setFloat(keyName, keyValue)
    }

    /**
     * Gets a float value from the NBT on the item stack given a key
     *
     * @param itemStack The itemstack to read NBT data from
     * @param keyName   The key that contains the data we want to retrieve
     * @return The float value represented by the key or null if it was not present
     */
    fun getFloat(itemStack: ItemStack, keyName: String): Float?
    {
        return if (hasTag(itemStack, keyName))
        {
            itemStack.tagCompound!!.getFloat(keyName)
        }
        else null
    }

    /**
     * Sets an item stack's key name to store a double value
     *
     * @param itemStack The item stack to update
     * @param keyName   The name of the key to store the double with
     * @param keyValue  The double to store with that key value
     */
    fun setDouble(itemStack: ItemStack, keyName: String, keyValue: Double)
    {
        // Make sure the item stack has an NBT tag compound, if not add it
        initNBTTagCompound(itemStack)
        // Set the double value onto the tag compound
        itemStack.tagCompound!!.setDouble(keyName, keyValue)
    }

    /**
     * Gets a double value from the NBT on the item stack given a key
     *
     * @param itemStack The itemstack to read NBT data from
     * @param keyName   The key that contains the data we want to retrieve
     * @return The double value represented by the key or null if it was not present
     */
    fun getDouble(itemStack: ItemStack, keyName: String): Double?
    {
        return if (hasTag(itemStack, keyName))
        {
            itemStack.tagCompound!!.getDouble(keyName)
        }
        else null
    }

    /**
     * Sets an item stack's key name to store an NBT compound value
     *
     * @param itemStack The item stack to update
     * @param keyName   The name of the key to store the double with
     * @param keyValue  The nbt compound to store with that key value
     */
    fun setCompound(itemStack: ItemStack, keyName: String, keyValue: NBTTagCompound)
    {
        // Make sure the item stack has an NBT tag compound, if not add it
        initNBTTagCompound(itemStack)
        // Set the nbt compound value onto the tag compound
        itemStack.tagCompound!!.setTag(keyName, keyValue)
    }

    /**
     * Gets an nbt compound value from the NBT on the item stack given a key
     *
     * @param itemStack The itemstack to read NBT data from
     * @param keyName   The key that contains the data we want to retrieve
     * @return The nbt compound value represented by the key or null if it was not present
     */
    fun getCompound(itemStack: ItemStack, keyName: String): NBTTagCompound?
    {
        return if (hasTag(itemStack, keyName))
        {
            itemStack.tagCompound!!.getCompoundTag(keyName)
        }
        else null
    }
}