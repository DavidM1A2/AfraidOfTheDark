package com.davidm1a2.afraidofthedark.common.utility

import net.minecraft.world.item.ItemStack
import net.minecraft.nbt.CompoundTag

/**
 * Class that lets us work with itemstack NBT more easily
 */
object NBTHelper {
    /**
     * Tests if a given itemstack has the required key name
     *
     * @param itemStack The item stack to test
     * @param keyName   The key to search for in the item stack
     * @return True if the itemstack has the key, false otherwise
     */
    fun hasTag(itemStack: ItemStack, keyName: String): Boolean {
        return itemStack.tag != null && itemStack.tag!!.contains(keyName)
    }

    /**
     * Removes a tag off of a given itemstack
     *
     * @param itemStack The item stack to remove from
     * @param keyName   The key to remove
     * @return
     */
    fun removeTag(itemStack: ItemStack, keyName: String) {
        if (hasTag(itemStack, keyName)) {
            itemStack.tag!!.remove(keyName)
        }
    }

    /**
     * Initializes the NBT Tag Compound for the given ItemStack if it is null
     *
     * @param itemStack The ItemStack for which its NBT Tag Compound is being checked for initialization
     */
    private fun initNBTTagCompound(itemStack: ItemStack) {
        // Make sure the item stack has an NBT tag compound, if not add it
        if (itemStack.tag == null) {
            itemStack.tag = CompoundTag()
        }
    }

    /**
     * Sets an item stack's key name to store a long value
     *
     * @param itemStack The item stack to update
     * @param keyName   The name of the key to store the long with
     * @param keyValue  The long to store with that key value
     */
    fun setLong(itemStack: ItemStack, keyName: String, keyValue: Long) {
        // Make sure the item stack has an NBT tag compound, if not add it
        initNBTTagCompound(itemStack)
        // Set the long value onto the tag compound
        itemStack.tag!!.putLong(keyName, keyValue)
    }

    /**
     * Gets a long value from the NBT on the item stack given a key
     *
     * @param itemStack The itemstack to read NBT data from
     * @param keyName   The key that contains the data we want to retrieve
     * @return The long value represented by the key or null if it was not present
     */
    fun getLong(itemStack: ItemStack, keyName: String): Long? {
        return if (hasTag(itemStack, keyName)) {
            itemStack.tag!!.getLong(keyName)
        } else null
    }

    /**
     * Sets an item stack's key name to store a string value
     *
     * @param itemStack The item stack to update
     * @param keyName   The name of the key to store the string with
     * @param keyValue  The string to store with that key value
     */
    fun setString(itemStack: ItemStack, keyName: String, keyValue: String) {
        // Make sure the item stack has an NBT tag compound, if not add it
        initNBTTagCompound(itemStack)
        // Set the string value onto the tag compound
        itemStack.tag!!.putString(keyName, keyValue)
    }

    /**
     * Gets a string value from the NBT on the item stack given a key
     *
     * @param itemStack The itemstack to read NBT data from
     * @param keyName   The key that contains the data we want to retrieve
     * @return The string value represented by the key or null if it was not present
     */
    fun getString(itemStack: ItemStack, keyName: String): String? {
        return if (hasTag(itemStack, keyName)) {
            itemStack.tag!!.getString(keyName)
        } else null
    }

    /**
     * Sets an item stack's key name to store a boolean value
     *
     * @param itemStack The item stack to update
     * @param keyName   The name of the key to store the boolean with
     * @param keyValue  The boolean to store with that key value
     */
    fun setBoolean(itemStack: ItemStack, keyName: String, keyValue: Boolean) {
        // Make sure the item stack has an NBT tag compound, if not add it
        initNBTTagCompound(itemStack)
        // Set the boolean value onto the tag compound
        itemStack.tag!!.putBoolean(keyName, keyValue)
    }

    /**
     * Gets a boolean value from the NBT on the item stack given a key
     *
     * @param itemStack The itemstack to read NBT data from
     * @param keyName   The key that contains the data we want to retrieve
     * @return The boolean value represented by the key or null if it was not present
     */
    fun getBoolean(itemStack: ItemStack, keyName: String): Boolean? {
        return if (hasTag(itemStack, keyName)) {
            itemStack.tag!!.getBoolean(keyName)
        } else null
    }

    /**
     * Sets an item stack's key name to store a byte value
     *
     * @param itemStack The item stack to update
     * @param keyName   The name of the key to store the byte with
     * @param keyValue  The byte to store with that key value
     */
    fun setByte(itemStack: ItemStack, keyName: String, keyValue: Byte) {
        // Make sure the item stack has an NBT tag compound, if not add it
        initNBTTagCompound(itemStack)
        // Set the byte value onto the tag compound
        itemStack.tag!!.putByte(keyName, keyValue)
    }

    /**
     * Gets a byte value from the NBT on the item stack given a key
     *
     * @param itemStack The itemstack to read NBT data from
     * @param keyName   The key that contains the data we want to retrieve
     * @return The byte value represented by the key or null if it was not present
     */
    fun getByte(itemStack: ItemStack, keyName: String): Byte? {
        return if (hasTag(itemStack, keyName)) {
            itemStack.tag!!.getByte(keyName)
        } else null
    }

    /**
     * Sets an item stack's key name to store a short value
     *
     * @param itemStack The item stack to update
     * @param keyName   The name of the key to store the short with
     * @param keyValue  The short to store with that key value
     */
    fun setShort(itemStack: ItemStack, keyName: String, keyValue: Short) {
        // Make sure the item stack has an NBT tag compound, if not add it
        initNBTTagCompound(itemStack)
        // Set the short value onto the tag compound
        itemStack.tag!!.putShort(keyName, keyValue)
    }

    /**
     * Gets a short value from the NBT on the item stack given a key
     *
     * @param itemStack The itemstack to read NBT data from
     * @param keyName   The key that contains the data we want to retrieve
     * @return The short value represented by the key or null if it was not present
     */
    fun getShort(itemStack: ItemStack, keyName: String): Short? {
        return if (hasTag(itemStack, keyName)) {
            itemStack.tag!!.getShort(keyName)
        } else null
    }

    /**
     * Sets an item stack's key name to store an integer value
     *
     * @param itemStack The item stack to update
     * @param keyName   The name of the key to store the integer with
     * @param keyValue  The integer to store with that key value
     */
    fun setInteger(itemStack: ItemStack, keyName: String, keyValue: Int) {
        // Make sure the item stack has an NBT tag compound, if not add it
        initNBTTagCompound(itemStack)
        // Set the integer value onto the tag compound
        itemStack.tag!!.putInt(keyName, keyValue)
    }

    /**
     * Gets an integer value from the NBT on the item stack given a key
     *
     * @param itemStack The itemstack to read NBT data from
     * @param keyName   The key that contains the data we want to retrieve
     * @return The integer value represented by the key or null if it was not present
     */
    fun getInteger(itemStack: ItemStack, keyName: String): Int? {
        return if (hasTag(itemStack, keyName)) {
            itemStack.tag!!.getInt(keyName)
        } else null
    }

    /**
     * Sets an item stack's key name to store a float value
     *
     * @param itemStack The item stack to update
     * @param keyName   The name of the key to store the float with
     * @param keyValue  The float to store with that key value
     */
    fun setFloat(itemStack: ItemStack, keyName: String, keyValue: Float) {
        // Make sure the item stack has an NBT tag compound, if not add it
        initNBTTagCompound(itemStack)
        // Set the float value onto the tag compound
        itemStack.tag!!.putFloat(keyName, keyValue)
    }

    /**
     * Gets a float value from the NBT on the item stack given a key
     *
     * @param itemStack The itemstack to read NBT data from
     * @param keyName   The key that contains the data we want to retrieve
     * @return The float value represented by the key or null if it was not present
     */
    fun getFloat(itemStack: ItemStack, keyName: String): Float? {
        return if (hasTag(itemStack, keyName)) {
            itemStack.tag!!.getFloat(keyName)
        } else null
    }

    /**
     * Sets an item stack's key name to store a double value
     *
     * @param itemStack The item stack to update
     * @param keyName   The name of the key to store the double with
     * @param keyValue  The double to store with that key value
     */
    fun setDouble(itemStack: ItemStack, keyName: String, keyValue: Double) {
        // Make sure the item stack has an NBT tag compound, if not add it
        initNBTTagCompound(itemStack)
        // Set the double value onto the tag compound
        itemStack.tag!!.putDouble(keyName, keyValue)
    }

    /**
     * Gets a double value from the NBT on the item stack given a key
     *
     * @param itemStack The itemstack to read NBT data from
     * @param keyName   The key that contains the data we want to retrieve
     * @return The double value represented by the key or null if it was not present
     */
    fun getDouble(itemStack: ItemStack, keyName: String): Double? {
        return if (hasTag(itemStack, keyName)) {
            itemStack.tag!!.getDouble(keyName)
        } else null
    }

    /**
     * Sets an item stack's key name to store an NBT compound value
     *
     * @param itemStack The item stack to update
     * @param keyName   The name of the key to store the double with
     * @param keyValue  The nbt compound to store with that key value
     */
    fun setCompound(itemStack: ItemStack, keyName: String, keyValue: CompoundTag) {
        // Make sure the item stack has an NBT tag compound, if not add it
        initNBTTagCompound(itemStack)
        // Set the nbt compound value onto the tag compound
        itemStack.tag!!.put(keyName, keyValue)
    }

    /**
     * Gets an nbt compound value from the NBT on the item stack given a key
     *
     * @param itemStack The itemstack to read NBT data from
     * @param keyName   The key that contains the data we want to retrieve
     * @return The nbt compound value represented by the key or null if it was not present
     */
    fun getCompound(itemStack: ItemStack, keyName: String): CompoundTag? {
        return if (hasTag(itemStack, keyName)) {
            itemStack.tag!!.getCompound(keyName)
        } else null
    }

    fun setIntArray(itemStack: ItemStack, keyName: String, keyValue: IntArray) {
        initNBTTagCompound(itemStack)
        itemStack.tag!!.putIntArray(keyName, keyValue)
    }

    fun getIntArray(itemStack: ItemStack, keyName: String): IntArray? {
        return if (hasTag(itemStack, keyName)) {
            itemStack.tag!!.getIntArray(keyName)
        } else null
    }
}