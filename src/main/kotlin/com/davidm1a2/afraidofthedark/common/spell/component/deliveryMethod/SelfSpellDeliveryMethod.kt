package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod

/**
 * Self delivery method delivers the spell to the caster
 *
 * @constructor does not initialize anything
 */
class SelfSpellDeliveryMethod : AOTDSpellDeliveryMethod("self", ModResearches.THE_JOURNEY_BEGINS) {
    /**
     * Called to begin delivering the effects to the target by whatever means necessary
     *
     * @param state The state of the spell to deliver
     */
    override fun executeDelivery(state: DeliveryTransitionState) {
        // Self just procs the effects and transitions at the target entity
        if (state.entity != null) {
            this.procEffects(state)
            this.transitionFrom(state)
        }
    }

    /**
     * Gets the cost of the delivery method
     *
     * @return The cost of the delivery method
     */
    override fun getDeliveryCost(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return 0.1
    }

    /**
     * Gets the multiplier that this delivery method will apply to the stage it's in
     *
     * @return The spell stage multiplier for cost
     */
    override fun getMultiplicity(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return 1.0
    }
}