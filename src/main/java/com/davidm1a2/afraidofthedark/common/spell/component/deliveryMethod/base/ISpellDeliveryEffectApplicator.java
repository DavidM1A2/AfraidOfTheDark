package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base;

import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState;
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect;

/**
 * Interface representing how effects are applied based on the delivery method used
 */
public interface ISpellDeliveryEffectApplicator
{
    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     * @param effect The effect to proc
     *
     * @return True if the custom effect proc'd successfully, false if it did not and default logic should be used instead
     */
    boolean procEffect(DeliveryTransitionState state, SpellEffect effect);
}
