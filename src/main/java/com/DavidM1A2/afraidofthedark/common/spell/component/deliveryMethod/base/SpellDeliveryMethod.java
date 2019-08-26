package com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base;

import com.DavidM1A2.afraidofthedark.common.constants.ModRegistries;
import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import com.DavidM1A2.afraidofthedark.common.spell.component.SpellComponent;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffect;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

/**
 * Base class for all spell delivery methods
 */
public abstract class SpellDeliveryMethod extends SpellComponent
{
    /**
     * Called to begin delivering the effects to the target by whatever means necessary
     *
     * @param state The state of the spell at the current delivery method
     */
    public abstract void executeDelivery(DeliveryTransitionState state);

    /**
     * Applies the effects that are a part of this delivery method to the targets
     *
     * @param state The state of the spell at the current delivery method
     */
    public void procEffects(DeliveryTransitionState state)
    {
        // Go over each effect
        for (SpellEffect effect : state.getCurrentStage().getEffects())
        {
            if (effect != null)
            {
                // Test if there's a special custom applicator for this effect, if so use that
                ISpellDeliveryEffectApplicator customApplicator = this.getEntryRegistryType().getApplicator(effect.getEntryRegistryType());
                if (customApplicator != null)
                {
                    // Test if the custom application was successful
                    boolean customApplicationSuccessful = customApplicator.procEffect(state, effect);
                    // If it was not, use the default logic
                    if (!customApplicationSuccessful)
                    {
                        this.defaultEffectProc(state, effect);
                    }
                }
                // There's no custom applicator, so use the default proc effect
                else
                {
                    this.defaultEffectProc(state, effect);
                }
            }
        }
    }

    /**
     * Applies a given effect given the spells current state
     *
     * @param state  The state of the spell at the current delivery method
     * @param effect The effect that needs to be applied
     */
    public abstract void defaultEffectProc(DeliveryTransitionState state, SpellEffect effect);

    /**
     * Called after the delivery finishes and we transition from this state into the next
     *
     * @param state The state of the spell to transition FROM
     */
    public void transitionFrom(DeliveryTransitionState state)
    {
        Spell spell = state.getSpell();
        // Test if we can transition to the next stage
        if (spell.hasStage(state.getStageIndex() + 1))
        {
            // Grab the next delivery method
            SpellDeliveryMethod nextDeliveryMethod = spell.getStage(state.getStageIndex() + 1).getDeliveryMethod();
            // Grab the transitioner from the current delivery method to the next one
            ISpellDeliveryTransitioner transitioner = nextDeliveryMethod.getEntryRegistryType().getTransitioner(state.getCurrentStage().getDeliveryMethod().getEntryRegistryType());
            // Perform the transition if a custom transitioner is present
            if (transitioner != null)
            {
                transitioner.transition(state);
            }
            else
            {
                performDefaultTransition(state);
            }
        }
    }

    /**
     * Performs the default transition from this delivery method to the next
     *
     * @param state The state of the spell to transition
     */
    public abstract void performDefaultTransition(DeliveryTransitionState state);

    /**
     * Gets the cost of the delivery method
     *
     * @return The cost of the delivery method
     */
    public abstract double getCost();

    /**
     * Gets the multiplier that this delivery method will apply to the stage it's in
     *
     * @return The spell stage multiplier for cost
     */
    public abstract double getStageCostMultiplier();

    /**
     * Should get the SpellDeliveryMethodEntry registry's type
     *
     * @return The registry entry that this delivery method was built with, used for deserialization
     */
    @Override
    public abstract SpellDeliveryMethodEntry getEntryRegistryType();

    /**
     * Utility function to create a spell delivery method from NBT
     *
     * @param nbt The NBT to get the delivery method information from
     * @return The spell delivery method instance from NBT
     */
    public static SpellDeliveryMethod createFromNBT(NBTTagCompound nbt)
    {
        // Figure out the type of delivery method that this NBT represents
        String deliveryMethodTypeId = nbt.getString(NBT_TYPE_ID);
        // Use our registry to create a new instance of this type
        SpellDeliveryMethod deliveryMethod = ModRegistries.SPELL_DELIVERY_METHODS.getValue(new ResourceLocation(deliveryMethodTypeId)).newInstance();
        // Load in the delivery method's state from NBT
        deliveryMethod.deserializeNBT(nbt);
        // Return the delivery method
        return deliveryMethod;
    }
}
