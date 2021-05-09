package com.davidm1a2.afraidofthedark.common.capabilities.world.spellState

import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.INBT
import net.minecraft.nbt.ListNBT
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.Constants
import org.apache.commons.logging.LogFactory

/**
 * Default storage implementation for AOTD spell state
 */
class AOTDWorldSpellStateStorage : Capability.IStorage<IAOTDWorldSpellStates> {
    override fun writeNBT(capability: Capability<IAOTDWorldSpellStates>, instance: IAOTDWorldSpellStates, side: Direction?): INBT {
        // Create a compound to write
        val compound = CompoundNBT()

        val delayedEntries = ListNBT()
        // Write each entry to the list
        instance.getDelayedDeliveryEntries().forEach {
            delayedEntries.add(it.serializeNBT())
        }
        compound.put(NBT_DELAYED_DELIVERY_ENTRIES, delayedEntries)

        return compound
    }

    override fun readNBT(capability: Capability<IAOTDWorldSpellStates>, instance: IAOTDWorldSpellStates, side: Direction?, nbt: INBT) {
        // Test if the nbt tag base is an NBT tag compound
        if (nbt is CompoundNBT) {
            // Go over each delayed delivery entry and read it in
            val delayedEntryNbts = nbt.getList(NBT_DELAYED_DELIVERY_ENTRIES, Constants.NBT.TAG_COMPOUND)
            val delayedEntries = (0 until delayedEntryNbts.size).map { DelayedDeliveryEntry(delayedEntryNbts.getCompound(it)) }
            instance.setDelayedDeliveryEntries(delayedEntries)
        } else {
            LOG.error("Attempted to deserialize an NBTBase that was not an NBTTagCompound!")
        }
    }

    companion object {
        private val LOG = LogFactory.getLog(AOTDWorldSpellStateStorage::class.java)

        // NBT constants used in serialization/deserialization
        private const val NBT_DELAYED_DELIVERY_ENTRIES = "delayed_delivery_entries"
    }
}