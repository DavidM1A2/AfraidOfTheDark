package com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod;

import com.DavidM1A2.afraidofthedark.common.capabilities.world.SpellStateData;
import com.DavidM1A2.afraidofthedark.common.constants.ModSpellDeliveryMethods;
import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import com.DavidM1A2.afraidofthedark.common.spell.component.DeliveryTransitionState;
import com.DavidM1A2.afraidofthedark.common.spell.component.DeliveryTransitionStateBuilder;
import com.DavidM1A2.afraidofthedark.common.spell.component.EditableSpellComponentProperty;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethodEntry;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffect;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Self delivery method delivers the spell after a delay
 */
public class SpellDeliveryMethodDelay extends AOTDSpellDeliveryMethod
{
    // The NBT keys
    private static final String NBT_DELAY = "delay";

    // Default property values
    private static final long DEFAULT_DELAY = 20;

    // The delay of the delivery in ticks
    private long delay = DEFAULT_DELAY;

    /**
     * Constructor initializes the editable properties
     */
    public SpellDeliveryMethodDelay()
    {
        super();
        this.addEditableProperty(new EditableSpellComponentProperty(
                "Delay",
                "The delay of the delivery in ticks (20 ticks = 1 second)",
                () -> Long.toString(this.delay),
                newValue ->
                {
                    // Ensure the number is parsable
                    if (NumberUtils.isDigits(newValue))
                    {
                        // Parse the delay
                        this.delay = Long.parseLong(newValue);
                        // Ensure delay is valid
                        if (this.delay >= 1.0)
                        {
                            return null;
                        }
                        else
                        {
                            this.delay = DEFAULT_DELAY;
                            return "Delay must be larger than or equal to 1";
                        }
                    }
                    // If it's not valid return an error
                    else
                    {
                        return newValue + " is not a valid integer!";
                    }
                }));
    }

    /**
     * Called to deliver the effects to the target by whatever means necessary
     *
     * @param state The state of the spell to deliver
     */
    @Override
    public void executeDelivery(DeliveryTransitionState state)
    {
        // Delayed adds this spell to the queue to wait
        SpellStateData spellStateData = SpellStateData.get(state.getWorld());
        spellStateData.addDelayedDelivery(state);
    }

    /**
     * Applies a given effect given the spells current state
     *
     * @param state  The state of the spell at the current delivery method
     * @param effect The effect that needs to be applied
     */
    @Override
    public void defaultEffectProc(DeliveryTransitionState state, SpellEffect effect)
    {
        // The effect is just applied to the target
        effect.procEffect(state);
    }

    /**
     * Performs the default transition from this delivery method to the next
     *
     * @param state The state of the spell to transition
     */
    @Override
    public void performDefaultTransition(DeliveryTransitionState state)
    {
        Spell spell = state.getSpell();
        int spellIndex = state.getStageIndex();
        // Perform the transition between the next delivery method and the current delivery method
        spell.getStage(spellIndex + 1).getDeliveryMethod().executeDelivery(new DeliveryTransitionStateBuilder()
                .copyOf(state)
                .withStageIndex(spellIndex + 1)
                .withDeliveryEntity(null)
                .build());
    }

    /**
     * Gets the cost of the delivery method
     *
     * @return The cost of the delivery method
     */
    @Override
    public double getCost()
    {
        return 10 + this.delay / 10.0D;
    }

    /**
     * Gets the multiplier that this delivery method will apply to the stage it's in
     *
     * @return The spell stage multiplier for cost
     */
    @Override
    public double getStageCostMultiplier()
    {
        return 1.0;
    }

    /**
     * Should get the SpellDeliveryMethodEntry registry's type
     *
     * @return The registry entry that this delivery method was built with, used for deserialization
     */
    @Override
    public SpellDeliveryMethodEntry getEntryRegistryType()
    {
        return ModSpellDeliveryMethods.DELAY;
    }

    /**
     * Serializes the spell component to NBT, override to add additional fields
     *
     * @return An NBT compound containing any required spell component info
     */
    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = super.serializeNBT();

        nbt.setLong(NBT_DELAY, this.delay);

        return nbt;
    }

    /**
     * Deserializes the state of this spell component from NBT
     *
     * @param nbt The NBT to deserialize from
     */
    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        super.deserializeNBT(nbt);
        this.delay = nbt.getLong(NBT_DELAY);
    }

    ///
    /// Getters
    ///

    public long getDelay()
    {
        return delay;
    }
}
