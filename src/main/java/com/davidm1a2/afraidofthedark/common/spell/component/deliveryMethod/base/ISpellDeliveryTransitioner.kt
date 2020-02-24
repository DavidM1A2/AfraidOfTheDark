package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base

import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState

/**
 * Interface representing a delivery method transitioner to go from one delivery method to another
 */
interface ISpellDeliveryTransitioner {
    /**
     * Called to deliver the effects to the target by whatever means necessary
     *
     * @param state The state of the spell to deliver
     */
    fun transition(state: DeliveryTransitionState)
}