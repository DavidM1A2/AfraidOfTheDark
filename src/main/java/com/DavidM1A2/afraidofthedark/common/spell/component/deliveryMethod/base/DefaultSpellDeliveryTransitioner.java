package com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base;

import com.DavidM1A2.afraidofthedark.common.spell.Spell;

/**
 * Class representing a delivery method transitioner to go from one delivery method to another
 */
public class DefaultSpellDeliveryTransitioner implements ISpellDeliveryTransitioner
{
    /**
     * Called to deliver the effects to the target by whatever means necessary
     *
     * @param state The state of the spell to deliver
     */
    @Override
    public void transition(DeliveryTransitionState state)
    {
        Spell spell = state.getSpell();
        // Advance to the the next delivery method
        int nextStageIndex = state.getStageIndex() + 1;

        // If the spell has this stage execute it
        if (spell.hasStage(nextStageIndex))
        {
            spell.getStage(nextStageIndex)
                    .getDeliveryMethod()
                    .deliver(new DeliveryTransitionStateBuilder()
                            .copyOf(state)
                            .withStageIndex(nextStageIndex)
                            .build());
        }
        // Spell complete
    }
}
