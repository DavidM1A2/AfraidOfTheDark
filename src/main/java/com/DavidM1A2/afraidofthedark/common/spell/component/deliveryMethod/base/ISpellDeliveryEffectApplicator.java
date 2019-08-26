package com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base;

import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffect;

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
     */
    void procEffect(DeliveryTransitionState state, SpellEffect effect);
}
