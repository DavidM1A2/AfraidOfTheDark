package com.DavidM1A2.afraidofthedark.common.utility;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.ISaveHandler;
import org.codehaus.plexus.util.ExceptionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Class that lets us work with itemstack NBT more easily
 */
public class NBTHelper
{
    /**
     * Returns a list of all saved player nbts, if the flag is set then player data is saved to disk before being read
     *
     * @param minecraftServer The minecraft server to get the NBTs from
     * @param saveFirst       True if the existing online player data should be written to disk first, false otherwise
     * @return A list of player NBTTagCompounds containing all players both offline and online
     */
    public static List<NBTTagCompound> getAllSavedPlayerNBTs(MinecraftServer minecraftServer, boolean saveFirst)
    {
        // Write all player data to disk if the flag is set
        if (saveFirst)
        {
            minecraftServer.getPlayerList().saveAllPlayerData();
        }

        // A list of player NBTs to return
        List<NBTTagCompound> toReturn = Lists.newArrayList();

        // All worlds share a single save handler so we can just use the overworld's save handler to grab all player data even players in other dimensions
        ISaveHandler saveHandler = minecraftServer.worlds[0].getSaveHandler();
        // Grab the playerdata directory that stores all player's NBT data
        File playerDataDirectory = new File(saveHandler.getWorldDirectory(), "playerdata");
        // Iterate over each player's UUID file
        for (String playerUUID : saveHandler.getPlayerNBTManager().getAvailablePlayerDat())
        {
            // Create a file pointed at that player's data
            File playerData = new File(playerDataDirectory, playerUUID + ".dat");
            // If the player data exists, is a file, and can be read read the file
            if (playerData.exists() && playerData.isFile() && playerData.canRead())
            {
                // Open a file input stream to the file
                try (FileInputStream playerDataFileInputStream = new FileInputStream(playerData))
                {
                    // Read the player data into NBT
                    NBTTagCompound playerDataCompound = CompressedStreamTools.readCompressed(playerDataFileInputStream);
                    // Store the data compound to return
                    toReturn.add(playerDataCompound);
                }
                // If something goes wrong log an error
                catch (IOException e)
                {
                    AfraidOfTheDark.INSTANCE.getLogger().error("Could not read player data for file " + playerData.getAbsolutePath() + ", exception was:\n" + ExceptionUtils.getStackTrace(e));
                }
            }
        }

        return toReturn;
    }

    /**
     * Tests if a given itemstack has the required key name
     *
     * @param itemStack The item stack to test
     * @param keyName   The key to search for in the item stack
     * @return True if the itemstack has the key, false otherwise
     */
    public static boolean hasTag(ItemStack itemStack, String keyName)
    {
        return itemStack != null && itemStack.getTagCompound() != null && itemStack.getTagCompound().hasKey(keyName);
    }

    /**
     * Removes a tag off of a given itemstack
     *
     * @param itemStack The item stack to remove from
     * @param keyName   The key to remove
     * @return
     */
    public static void removeTag(ItemStack itemStack, String keyName)
    {
        if (NBTHelper.hasTag(itemStack, keyName))
        {
            itemStack.getTagCompound().removeTag(keyName);
        }
    }

    /**
     * Initializes the NBT Tag Compound for the given ItemStack if it is null
     *
     * @param itemStack The ItemStack for which its NBT Tag Compound is being checked for initialization
     */
    private static void initNBTTagCompound(ItemStack itemStack)
    {
        // Make sure the item stack has an NBT tag compound, if not add it
        if (itemStack.getTagCompound() == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }
    }

    /**
     * Sets an item stack's key name to store a long value
     *
     * @param itemStack The item stack to update
     * @param keyName   The name of the key to store the long with
     * @param keyValue  The long to store with that key value
     */
    public static void setLong(ItemStack itemStack, String keyName, Long keyValue)
    {
        // Make sure the item stack is non-null
        if (itemStack != null)
        {
            // Make sure the item stack has an NBT tag compound, if not add it
            NBTHelper.initNBTTagCompound(itemStack);
            // Set the long value onto the tag compound
            itemStack.getTagCompound().setLong(keyName, keyValue);
        }
    }

    /**
     * Gets a long value from the NBT on the item stack given a key
     *
     * @param itemStack The itemstack to read NBT data from
     * @param keyName   The key that contains the data we want to retrieve
     * @return The long value represented by the key or null if it was not present
     */
    public static Long getLong(ItemStack itemStack, String keyName)
    {
        if (NBTHelper.hasTag(itemStack, keyName))
        {
            return itemStack.getTagCompound().getLong(keyName);
        }
        return null;
    }

    /**
     * Sets an item stack's key name to store a string value
     *
     * @param itemStack The item stack to update
     * @param keyName   The name of the key to store the string with
     * @param keyValue  The string to store with that key value
     */
    public static void setString(ItemStack itemStack, String keyName, String keyValue)
    {
        // Make sure the item stack is non-null
        if (itemStack != null)
        {
            // Make sure the item stack has an NBT tag compound, if not add it
            NBTHelper.initNBTTagCompound(itemStack);
            // Set the string value onto the tag compound
            itemStack.getTagCompound().setString(keyName, keyValue);
        }
    }

    /**
     * Gets a string value from the NBT on the item stack given a key
     *
     * @param itemStack The itemstack to read NBT data from
     * @param keyName   The key that contains the data we want to retrieve
     * @return The string value represented by the key or null if it was not present
     */
    public static String getString(ItemStack itemStack, String keyName)
    {
        if (NBTHelper.hasTag(itemStack, keyName))
        {
            return itemStack.getTagCompound().getString(keyName);
        }
        return null;
    }

    /**
     * Sets an item stack's key name to store a boolean value
     *
     * @param itemStack The item stack to update
     * @param keyName   The name of the key to store the boolean with
     * @param keyValue  The boolean to store with that key value
     */
    public static void setBoolean(ItemStack itemStack, String keyName, Boolean keyValue)
    {
        // Make sure the item stack is non-null
        if (itemStack != null)
        {
            // Make sure the item stack has an NBT tag compound, if not add it
            NBTHelper.initNBTTagCompound(itemStack);
            // Set the boolean value onto the tag compound
            itemStack.getTagCompound().setBoolean(keyName, keyValue);
        }
    }

    /**
     * Gets a boolean value from the NBT on the item stack given a key
     *
     * @param itemStack The itemstack to read NBT data from
     * @param keyName   The key that contains the data we want to retrieve
     * @return The boolean value represented by the key or null if it was not present
     */
    public static Boolean getBoolean(ItemStack itemStack, String keyName)
    {
        if (NBTHelper.hasTag(itemStack, keyName))
        {
            return itemStack.getTagCompound().getBoolean(keyName);
        }
        return null;
    }

    /**
     * Sets an item stack's key name to store a byte value
     *
     * @param itemStack The item stack to update
     * @param keyName   The name of the key to store the byte with
     * @param keyValue  The byte to store with that key value
     */
    public static void setByte(ItemStack itemStack, String keyName, Byte keyValue)
    {
        // Make sure the item stack is non-null
        if (itemStack != null)
        {
            // Make sure the item stack has an NBT tag compound, if not add it
            NBTHelper.initNBTTagCompound(itemStack);
            // Set the byte value onto the tag compound
            itemStack.getTagCompound().setByte(keyName, keyValue);
        }
    }

    /**
     * Gets a byte value from the NBT on the item stack given a key
     *
     * @param itemStack The itemstack to read NBT data from
     * @param keyName   The key that contains the data we want to retrieve
     * @return The byte value represented by the key or null if it was not present
     */
    public static Byte getByte(ItemStack itemStack, String keyName)
    {
        if (NBTHelper.hasTag(itemStack, keyName))
        {
            return itemStack.getTagCompound().getByte(keyName);
        }
        return null;
    }

    /**
     * Sets an item stack's key name to store a short value
     *
     * @param itemStack The item stack to update
     * @param keyName   The name of the key to store the short with
     * @param keyValue  The short to store with that key value
     */
    public static void setShort(ItemStack itemStack, String keyName, Short keyValue)
    {
        // Make sure the item stack is non-null
        if (itemStack != null)
        {
            // Make sure the item stack has an NBT tag compound, if not add it
            NBTHelper.initNBTTagCompound(itemStack);
            // Set the short value onto the tag compound
            itemStack.getTagCompound().setShort(keyName, keyValue);
        }
    }

    /**
     * Gets a short value from the NBT on the item stack given a key
     *
     * @param itemStack The itemstack to read NBT data from
     * @param keyName   The key that contains the data we want to retrieve
     * @return The short value represented by the key or null if it was not present
     */
    public static Short getShort(ItemStack itemStack, String keyName)
    {
        if (NBTHelper.hasTag(itemStack, keyName))
        {
            return itemStack.getTagCompound().getShort(keyName);
        }
        return null;
    }

    /**
     * Sets an item stack's key name to store an integer value
     *
     * @param itemStack The item stack to update
     * @param keyName   The name of the key to store the integer with
     * @param keyValue  The integer to store with that key value
     */
    public static void setInteger(ItemStack itemStack, String keyName, Integer keyValue)
    {
        // Make sure the item stack is non-null
        if (itemStack != null)
        {
            // Make sure the item stack has an NBT tag compound, if not add it
            NBTHelper.initNBTTagCompound(itemStack);
            // Set the integer value onto the tag compound
            itemStack.getTagCompound().setInteger(keyName, keyValue);
        }
    }

    /**
     * Gets an integer value from the NBT on the item stack given a key
     *
     * @param itemStack The itemstack to read NBT data from
     * @param keyName   The key that contains the data we want to retrieve
     * @return The integer value represented by the key or null if it was not present
     */
    public static Integer getInteger(ItemStack itemStack, String keyName)
    {
        if (NBTHelper.hasTag(itemStack, keyName))
        {
            return itemStack.getTagCompound().getInteger(keyName);
        }
        return null;
    }

    /**
     * Sets an item stack's key name to store a float value
     *
     * @param itemStack The item stack to update
     * @param keyName   The name of the key to store the float with
     * @param keyValue  The float to store with that key value
     */
    public static void setFloat(ItemStack itemStack, String keyName, Float keyValue)
    {
        // Make sure the item stack is non-null
        if (itemStack != null)
        {
            // Make sure the item stack has an NBT tag compound, if not add it
            NBTHelper.initNBTTagCompound(itemStack);
            // Set the float value onto the tag compound
            itemStack.getTagCompound().setFloat(keyName, keyValue);
        }
    }

    /**
     * Gets a float value from the NBT on the item stack given a key
     *
     * @param itemStack The itemstack to read NBT data from
     * @param keyName   The key that contains the data we want to retrieve
     * @return The float value represented by the key or null if it was not present
     */
    public static Float getFloat(ItemStack itemStack, String keyName)
    {
        if (NBTHelper.hasTag(itemStack, keyName))
        {
            return itemStack.getTagCompound().getFloat(keyName);
        }
        return null;
    }

    /**
     * Sets an item stack's key name to store a double value
     *
     * @param itemStack The item stack to update
     * @param keyName   The name of the key to store the double with
     * @param keyValue  The double to store with that key value
     */
    public static void setDouble(ItemStack itemStack, String keyName, Double keyValue)
    {
        // Make sure the item stack is non-null
        if (itemStack != null)
        {
            // Make sure the item stack has an NBT tag compound, if not add it
            NBTHelper.initNBTTagCompound(itemStack);
            // Set the double value onto the tag compound
            itemStack.getTagCompound().setDouble(keyName, keyValue);
        }
    }

    /**
     * Gets a double value from the NBT on the item stack given a key
     *
     * @param itemStack The itemstack to read NBT data from
     * @param keyName   The key that contains the data we want to retrieve
     * @return The double value represented by the key or null if it was not present
     */
    public static Double getDouble(ItemStack itemStack, String keyName)
    {
        if (NBTHelper.hasTag(itemStack, keyName))
        {
            return itemStack.getTagCompound().getDouble(keyName);
        }
        return null;
    }
}
