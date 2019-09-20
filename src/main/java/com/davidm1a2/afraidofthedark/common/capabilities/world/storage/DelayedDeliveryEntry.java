package com.davidm1a2.afraidofthedark.common.capabilities.world.storage;

import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState;
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionStateBuilder;
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.SpellDeliveryMethodDelay;
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Optional;

/**
 * Class representing the delay delivery method that is waiting to go off
 */
public class DelayedDeliveryEntry
{
    // NBT keys for serializing the entry
    private static final String NBT_STATE = "state";
    private static final String NBT_TICKS_LEFT = "ticks_left";

    // The state we're waiting to advance
    private DeliveryTransitionState state;
    // How many game ticks are left before we advance the delivery
    private long ticksLeft;

    /**
     * Constructor initializes state and sets the time when active to the current system time "delay" amount in the future
     *
     * @param state The state of the spell
     */
    public DelayedDeliveryEntry(DeliveryTransitionState state)
    {
        this.state = state;
        // Grab the delivery method and get the number of ticks to delay
        SpellDeliveryMethod deliveryMethod = state.getCurrentStage().getDeliveryMethod();
        this.ticksLeft = Optional.ofNullable(deliveryMethod).map(SpellDeliveryMethodDelay.class::cast).map(SpellDeliveryMethodDelay::getDelay).orElse(0L);
    }

    /**
     * Restores the state of the entry from NBT
     *
     * @param nbt The NBT to read the state in from
     */
    public DelayedDeliveryEntry(NBTTagCompound nbt)
    {
        this.state = new DeliveryTransitionState(nbt.getCompoundTag(NBT_STATE));
        this.ticksLeft = nbt.getLong(NBT_TICKS_LEFT);
    }

    /**
     * Writes the state to NBT, used in persisting the NBT across world saves
     *
     * @return The NBT that was saved to
     */
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setTag(NBT_STATE, state.writeToNbt());
        nbt.setLong(NBT_TICKS_LEFT, this.ticksLeft);

        return nbt;
    }

    /**
     * Called every tick to update the ticks left
     */
    public void update()
    {
        this.ticksLeft = this.ticksLeft - 1;
    }

    /**
     * Returns true if this delayed entry has waited long enough and is ready to fire
     *
     * @return True if this delayed entry is ready to fire, false otherwise
     */
    public boolean isReadyToFire()
    {
        // If ticks left is <= 0 then it's time to trigger the delivery
        return this.ticksLeft <= 0;
    }

    /**
     * Fires the delayed entry in its current state, assumes isReadyToFire() has been called and is true
     */
    public void fire()
    {
        SpellDeliveryMethod deliveryMethod = this.state.getCurrentStage().getDeliveryMethod();
        // Update the state to reflect the current world position if the entity targeted has moved
        if (this.state.getEntity() != null)
        {
            this.state = new DeliveryTransitionStateBuilder()
                    .copyOf(this.state)
                    // With entity updates the pos and direction
                    .withEntity(this.state.getEntity())
                    .build();
        }
        // Proc effects and transition, then return true since we delivered
        deliveryMethod.procEffects(this.state);
        deliveryMethod.transitionFrom(this.state);
    }
}
