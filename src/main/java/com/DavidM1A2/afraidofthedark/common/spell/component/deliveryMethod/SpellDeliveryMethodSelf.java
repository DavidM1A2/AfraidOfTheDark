package com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellDeliveryMethods;
import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.*;

/**
 * Self delivery method delivers the spell to the caster
 */
public class SpellDeliveryMethodSelf extends AOTDSpellDeliveryMethod
{
    /**
     * Constructor does not initialize anything
     */
    public SpellDeliveryMethodSelf()
    {
        super();
    }

    /**
     * Called to deliver the effects to the target by whatever means necessary
     *
     * @param state The state of the spell to deliver
     */
    @Override
    public void deliver(DeliveryTransitionState state)
    {
        // Can only deliver "self" to an entity
        if (state.getEntity() != null)
        {
            Spell spell = state.getSpell();
            int spellIndex = state.getStageIndex();

            // Delivery is as simple as applying effects for the "self" delivery method
            spell.getStage(spellIndex).forAllValidEffects((spellEffect, index) ->
            {
                ISpellDeliveryEffectApplicator effectApplicator = this.getEntryRegistryType().getApplicator(spellEffect.getEntryRegistryType());
                effectApplicator.applyEffect(spell, spellIndex, index, state.getEntity());
            });

            // Grab the next delivery method
            SpellDeliveryMethod nextDeliveryMethod = spell.hasStage(spellIndex + 1) ? spell.getStage(spellIndex + 1).getDeliveryMethod() : null;
            if (nextDeliveryMethod != null)
            {
                // Perform the transition between the next delivery method and the current delivery method
                nextDeliveryMethod.getEntryRegistryType().getTransitioner(this.getEntryRegistryType()).transition(state);
            }
        }
    }

    /**
     * Gets the cost of the delivery method
     *
     * @return The cost of the delivery method
     */
    @Override
    public double getCost()
    {
        return 0;
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
        return ModSpellDeliveryMethods.SELF;
    }
}
