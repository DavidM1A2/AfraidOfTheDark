package com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base;

/**
 * Interface representing a delivery method transitioner to go from one delivery method to another
 */
public interface ISpellDeliveryTransitioner
{
    /**
     * Called to deliver the effects to the target by whatever means necessary
     *
     * @param state The state of the spell to deliver
     */
    void transition(DeliveryTransitionState state);
}
