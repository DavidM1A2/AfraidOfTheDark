package com.davidm1a2.afraidofthedark.common.capabilities.world.spell

import com.davidm1a2.afraidofthedark.common.capabilities.INullableCapabilitySerializable
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import net.minecraft.core.Direction
import net.minecraft.nbt.ListTag
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import org.apache.logging.log4j.LogManager

class WorldSpellStatesCapabilitySerializer : INullableCapabilitySerializable<ListTag> {
    private val instance: IWorldSpellStates = WorldSpellStates()

    override fun <V> getCapability(capability: Capability<V>?, side: Direction?): LazyOptional<V> {
        return if (capability == ModCapabilities.WORLD_SPELL_STATES) LazyOptional.of { instance }.cast() else LazyOptional.empty()
    }

    override fun serializeNBT(): ListTag {
        val delayedEntries = ListTag()
        // Write each entry to the list
        instance.getDelayedDeliveryEntries().forEach {
            delayedEntries.add(it.serializeNBT())
        }
        return delayedEntries
    }

    override fun deserializeNBT(nbt: ListTag?) {
        // Test if the nbt tag base is an NBT tag list
        if (nbt is ListTag) {
            // Go over each delayed delivery entry and read it in
            val delayedEntries = (0 until nbt.size).map { DelayedDeliveryEntry(nbt.getCompound(it)) }
            instance.setDelayedDeliveryEntries(delayedEntries)
        } else {
            LOG.error("Attempted to deserialize an NBTBase that was null!")
        }
    }

    companion object {
        private val LOG = LogManager.getLogger()
    }
}