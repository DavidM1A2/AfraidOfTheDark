package com.davidm1a2.afraidofthedark.common.capabilities.world

import com.davidm1a2.afraidofthedark.common.capabilities.world.storage.DelayedDeliveryEntry
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.world.World
import net.minecraft.world.storage.WorldSavedData
import net.minecraftforge.common.util.Constants

/**
 * World data that stores any spell data that is not stored in entity form and must persist
 *
 * @constructor just calls super with our ID
 * @property delayedDeliveryEntries A list of delayed delivery entries that are awaiting delivery
 */
class SpellStateData @JvmOverloads constructor(identifier: String = IDENTIFIER) : WorldSavedData(identifier)
{
    private val delayedDeliveryEntries: MutableList<DelayedDeliveryEntry> = mutableListOf()

    /**
     * Writes the delayed entries to NBT
     *
     * @param nbt The NBT to write to
     * @return The compound that represents the spell state data
     */
    override fun writeToNBT(nbt: NBTTagCompound): NBTTagCompound
    {
        val delayedEntries = NBTTagList()
        // Write each entry to the list
        delayedDeliveryEntries.forEach()
        {
            delayedEntries.appendTag(it.serializeNBT())
        }
        nbt.setTag(NBT_DELAYED_DELIVERY_ENTRIES, delayedEntries)
        return nbt
    }

    /**
     * Reads the spell state data in from NBT
     *
     * @param nbt The NBT to read spell data in
     */
    override fun readFromNBT(nbt: NBTTagCompound)
    {
        // Go over each delayed delivery entry and read it in
        val delayedEntries = nbt.getTagList(NBT_DELAYED_DELIVERY_ENTRIES, Constants.NBT.TAG_COMPOUND)
        for (i in 0 until delayedEntries.tagCount())
        {
            delayedDeliveryEntries.add(DelayedDeliveryEntry(delayedEntries.getCompoundTagAt(i)))
        }
    }

    /**
     * Adds a delayed delivery method to the world data for storing until the delay is over
     *
     * @param state The state to store
     */
    fun addDelayedDelivery(state: DeliveryTransitionState?)
    {
        delayedDeliveryEntries.add(DelayedDeliveryEntry(state!!))
        markDirty()
    }

    /**
     * Called once per tick, tests every delayed delivery method to see if it's ready to fire
     */
    fun update()
    {
        // Update each delayed delivery entry
        delayedDeliveryEntries.forEach { it.update() }
        // Get a list of delayed entries that are ready to fire
        val readyDelayedEntries = delayedDeliveryEntries.filter { it.isReadyToFire() }.toList()
        // If we have any ready delayed entries remove and fire them
        if (readyDelayedEntries.isNotEmpty())
        {
            // Remove the delayed entries
            delayedDeliveryEntries.removeAll(readyDelayedEntries)
            // Fire the delayed entries
            readyDelayedEntries.forEach { it.fire() }
            // Ensure the data gets written to disk
            markDirty()
        }
    }

    companion object
    {
        // The ID of the AOTD spell states in progress
        private const val IDENTIFIER = com.davidm1a2.afraidofthedark.common.constants.Constants.MOD_ID + "_spell_states"
        // NBT constants used in serialization/deserialization
        private const val NBT_DELAYED_DELIVERY_ENTRIES = "delayed_delivery_entries"

        /**
         * Called to get the SpellStateData for this world. Returns null if on client side
         *
         * @param world The world to get data for
         * @return The data for that world or null if it is not present
         */
        operator fun get(world: World): SpellStateData
        {
            // If we are on client side throw an exception
            if (world.isRemote)
            {
                throw UnsupportedOperationException("Attempted to get the SpellStateData client side!")
            }
            // Grab the storage object for all worlds (per server, not per world!)
            val storage = world.mapStorage
            // Get the saved spell state data for all worlds (per server, not per world!)
            var spellStateData = storage!!.getOrLoadData(SpellStateData::class.java, IDENTIFIER) as SpellStateData?
            // If it does not exist, instantiate new spell state data and store it into the storage object
            if (spellStateData == null)
            {
                spellStateData = SpellStateData()
                storage.setData(IDENTIFIER, spellStateData)
                spellStateData.markDirty()
            }
            // Return the data
            return spellStateData
        }
    }
}