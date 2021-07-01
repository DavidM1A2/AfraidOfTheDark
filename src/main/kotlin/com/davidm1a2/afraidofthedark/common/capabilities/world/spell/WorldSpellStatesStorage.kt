package com.davidm1a2.afraidofthedark.common.capabilities.world.spell

import net.minecraft.nbt.INBT
import net.minecraft.nbt.ListNBT
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import org.apache.commons.logging.LogFactory

/**
 * Default storage implementation for AOTD spell states
 */
class WorldSpellStatesStorage : Capability.IStorage<IWorldSpellStates> {
    override fun writeNBT(capability: Capability<IWorldSpellStates>, instance: IWorldSpellStates, side: Direction?): INBT {
        val delayedEntries = ListNBT()
        // Write each entry to the list
        instance.getDelayedDeliveryEntries().forEach {
            delayedEntries.add(it.serializeNBT())
        }
        return delayedEntries
    }

    override fun readNBT(capability: Capability<IWorldSpellStates>, instance: IWorldSpellStates, side: Direction?, nbt: INBT) {
        // Test if the nbt tag base is an NBT tag list
        if (nbt is ListNBT) {
            // Go over each delayed delivery entry and read it in
            val delayedEntries = (0 until nbt.size).map { DelayedDeliveryEntry(nbt.getCompound(it)) }
            instance.setDelayedDeliveryEntries(delayedEntries)
        } else {
            LOG.error("Attempted to deserialize an NBTBase that was not a ListNBT!")
        }
    }

    companion object {
        private val LOG = LogFactory.getLog(WorldSpellStatesStorage::class.java)
    }
}