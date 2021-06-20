package com.davidm1a2.afraidofthedark.common.capabilities.world.spell

import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState

class WorldSpellStates : IWorldSpellStates {
    private val delayedDeliveryEntries: MutableList<DelayedDeliveryEntry> = mutableListOf()

    override fun addDelayedDelivery(state: DeliveryTransitionState) {
        delayedDeliveryEntries.add(DelayedDeliveryEntry(state))
    }

    override fun tick() {
        // Update each delayed delivery entry
        delayedDeliveryEntries.forEach { it.update() }
        // Get a list of delayed entries that are ready to fire
        val readyDelayedEntries = delayedDeliveryEntries.filter { it.isReadyToFire() }
        // If we have any ready delayed entries remove and fire them
        if (readyDelayedEntries.isNotEmpty()) {
            // Remove the delayed entries
            delayedDeliveryEntries.removeAll(readyDelayedEntries)
            // Fire the delayed entries
            readyDelayedEntries.forEach { it.fire() }
        }
    }

    override fun getDelayedDeliveryEntries(): List<DelayedDeliveryEntry> {
        return delayedDeliveryEntries
    }

    override fun setDelayedDeliveryEntries(entries: List<DelayedDeliveryEntry>) {
        delayedDeliveryEntries.clear()
        delayedDeliveryEntries.addAll(entries)
    }
}