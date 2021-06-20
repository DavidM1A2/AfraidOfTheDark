package com.davidm1a2.afraidofthedark.common.capabilities.world.spellStates

import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState

/**
 * Capability interface to store spell state information in a world
 */
interface IWorldSpellStates {
    /**
     * Adds a delayed delivery entry to the world
     *
     * @param state The state to add
     */
    fun addDelayedDelivery(state: DeliveryTransitionState)

    /**
     * Should be called once per game tick to pulse delayed spell states
     */
    fun tick()

    /**
     * Lists the existing delayed delivery entries. Used in serialization
     */
    fun getDelayedDeliveryEntries(): List<DelayedDeliveryEntry>

    /**
     * Sets the delayed delivery entries. Used in deserialization
     *
     * @param entries The entries to set
     */
    fun setDelayedDeliveryEntries(entries: List<DelayedDeliveryEntry>)
}