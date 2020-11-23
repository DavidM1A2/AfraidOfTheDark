package com.davidm1a2.afraidofthedark.common.capabilities.world

import com.davidm1a2.afraidofthedark.common.capabilities.world.storage.DelayedDeliveryEntry
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.ListNBT
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.server.ServerWorld
import net.minecraft.world.storage.WorldSavedData
import net.minecraftforge.common.util.Constants
import net.minecraftforge.fml.server.ServerLifecycleHooks

/**
 * World data that stores any spell data that is not stored in entity form and must persist
 *
 * @constructor just calls super with our ID
 * @property delayedDeliveryEntries A list of delayed delivery entries that are awaiting delivery
 */
class SpellStateData @JvmOverloads constructor(identifier: String = IDENTIFIER) : WorldSavedData(identifier) {
    private val delayedDeliveryEntries: MutableList<DelayedDeliveryEntry> = mutableListOf()

    /**
     * Writes the delayed entries to NBT
     *
     * @param nbt The NBT to write to
     * @return The compound that represents the spell state data
     */
    override fun write(nbt: CompoundNBT): CompoundNBT {
        val delayedEntries = ListNBT()
        // Write each entry to the list
        delayedDeliveryEntries.forEach {
            delayedEntries.add(it.serializeNBT())
        }
        nbt.put(NBT_DELAYED_DELIVERY_ENTRIES, delayedEntries)
        return nbt
    }

    /**
     * Reads the spell state data in from NBT
     *
     * @param nbt The NBT to read spell data in
     */
    override fun read(nbt: CompoundNBT) {
        // Go over each delayed delivery entry and read it in
        val delayedEntries = nbt.getList(NBT_DELAYED_DELIVERY_ENTRIES, Constants.NBT.TAG_COMPOUND)
        for (i in 0 until delayedEntries.size) {
            delayedDeliveryEntries.add(DelayedDeliveryEntry(delayedEntries.getCompound(i)))
        }
    }

    /**
     * Adds a delayed delivery method to the world data for storing until the delay is over
     *
     * @param state The state to store
     */
    fun addDelayedDelivery(state: DeliveryTransitionState?) {
        delayedDeliveryEntries.add(DelayedDeliveryEntry(state!!))
        markDirty()
    }

    /**
     * Called once per tick, tests every delayed delivery method to see if it's ready to fire
     */
    fun update() {
        // Update each delayed delivery entry
        delayedDeliveryEntries.forEach { it.update() }
        // Get a list of delayed entries that are ready to fire
        val readyDelayedEntries = delayedDeliveryEntries.filter { it.isReadyToFire() }.toList()
        // If we have any ready delayed entries remove and fire them
        if (readyDelayedEntries.isNotEmpty()) {
            // Remove the delayed entries
            delayedDeliveryEntries.removeAll(readyDelayedEntries)
            // Fire the delayed entries
            readyDelayedEntries.forEach { it.fire() }
            // Ensure the data gets written to disk
            markDirty()
        }
    }

    companion object {
        // The ID of the AOTD spell states in progress
        private const val IDENTIFIER = com.davidm1a2.afraidofthedark.common.constants.Constants.MOD_ID + "_spell_states"

        // NBT constants used in serialization/deserialization
        private const val NBT_DELAYED_DELIVERY_ENTRIES = "delayed_delivery_entries"

        /**
         * Called to get the SpellStateData for this world. Returns null if on client side
         *
         * @return The data for that world or null if it is not present
         */
        fun get(): SpellStateData? {
            // Hardcoded to use overworld to simulate "global" data
            val world: ServerWorld? = ServerLifecycleHooks.getCurrentServer()?.getWorld(DimensionType.OVERWORLD)
            // If we are on client side throw an exception
            if (world == null || world.isRemote) {
                return null
            }
            // Grab the storage object for all worlds (per server, not per world!)
            val storage = world.savedData
            // func_212426_a roughly equal to getOrLoadData?
            var spellStateData = storage.get({ SpellStateData() }, IDENTIFIER)
            // If it does not exist, instantiate new spell state data and store it into the storage object
            if (spellStateData == null) {
                spellStateData = SpellStateData()
                // func_212424_a roughly equal to setData?
                storage.set(spellStateData)
                spellStateData.markDirty()
            }
            // Return the data
            return spellStateData
        }
    }
}