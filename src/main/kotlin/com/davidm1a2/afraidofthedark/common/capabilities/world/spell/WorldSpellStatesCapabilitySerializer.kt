package com.davidm1a2.afraidofthedark.common.capabilities.world.spell

import com.davidm1a2.afraidofthedark.common.capabilities.AOTDCapabilitySerializer
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import net.minecraft.nbt.ListTag

class WorldSpellStatesCapabilitySerializer(instance: IWorldSpellStates = WorldSpellStates()) : AOTDCapabilitySerializer<IWorldSpellStates, ListTag>(instance) {
    override fun getCapability() = ModCapabilities.WORLD_SPELL_STATES

    override fun serializeNBT(): ListTag {
        val delayedEntries = ListTag()
        // Write each entry to the list
        instance.getDelayedDeliveryEntries().forEach {
            delayedEntries.add(it.serializeNBT())
        }
        return delayedEntries
    }

    override fun deserializeNBTSafe(nbt: ListTag) {
        // Go over each delayed delivery entry and read it in
        val delayedEntries = (0 until nbt.size).map { DelayedDeliveryEntry(nbt.getCompound(it)) }
        instance.setDelayedDeliveryEntries(delayedEntries)
    }
}