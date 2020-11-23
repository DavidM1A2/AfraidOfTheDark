package com.davidm1a2.afraidofthedark.common.capabilities.world.storage

import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionStateBuilder
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.DelaySpellDeliveryMethod
import net.minecraft.nbt.CompoundNBT

/**
 * Class representing the delay delivery method that is waiting to go off
 *
 * @property state The state we're waiting to advance
 * @property ticksLeft How many game ticks are left before we advance the delivery
 */
class DelayedDeliveryEntry {
    private var state: DeliveryTransitionState
    private var ticksLeft: Long

    /**
     * Constructor initializes state and sets the time when active to the current system time "delay" amount in the future
     *
     * @param state The state of the spell
     */
    constructor(state: DeliveryTransitionState) {
        this.state = state
        // Grab the delivery method and get the number of ticks to delay
        val deliveryMethod = state.getCurrentStage().deliveryInstance!!.component
        ticksLeft =
            deliveryMethod.let { it as DelaySpellDeliveryMethod }.getDelay(state.getCurrentStage().deliveryInstance!!)
    }

    /**
     * Restores the state of the entry from NBT
     *
     * @param nbt The NBT to read the state in from
     */
    constructor(nbt: CompoundNBT) {
        state = DeliveryTransitionState(nbt.getCompound(NBT_STATE))
        ticksLeft = nbt.getLong(NBT_TICKS_LEFT)
    }

    /**
     * Writes the state to NBT, used in persisting the NBT across world saves
     *
     * @return The NBT that was saved to
     */
    fun serializeNBT(): CompoundNBT {
        val nbt = CompoundNBT()
        nbt.put(NBT_STATE, state.writeToNbt())
        nbt.putLong(NBT_TICKS_LEFT, ticksLeft)
        return nbt
    }

    /**
     * Called every tick to update the ticks left
     */
    fun update() {
        ticksLeft = ticksLeft - 1
    }

    /**
     * Returns true if this delayed entry has waited long enough and is ready to fire
     *
     * @return True if this delayed entry is ready to fire, false otherwise
     */
    fun isReadyToFire(): Boolean {
        return ticksLeft <= 0
    }

    /**
     * Fires the delayed entry in its current state, assumes isReadyToFire() has been called and is true
     */
    fun fire() {
        val deliveryMethod = state.getCurrentStage().deliveryInstance?.component!!
        // Update the state to reflect the current world position if the entity targeted has moved
        if (state.getEntity() != null) {
            state = DeliveryTransitionStateBuilder()
                .copyOf(state) // With entity updates the pos and direction
                .withEntity(state.getEntity()!!)
                .build()
        }
        // Proc effects and transition, then return true since we delivered
        deliveryMethod.procEffects(state)
        deliveryMethod.transitionFrom(state)
    }

    companion object {
        // NBT keys for serializing the entry
        private const val NBT_STATE = "state"
        private const val NBT_TICKS_LEFT = "ticks_left"
    }
}