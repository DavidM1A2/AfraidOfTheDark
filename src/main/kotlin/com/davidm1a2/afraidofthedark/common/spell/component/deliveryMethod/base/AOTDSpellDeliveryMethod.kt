package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base

import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionStateBuilder
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraft.util.ResourceLocation

/**
 * Base class for all AOTD delivery methods
 *
 * @constructor calls super
 * @param id The ID of this delivery method entry
 */
abstract class AOTDSpellDeliveryMethod(id: ResourceLocation) : SpellDeliveryMethod(id) {
    /**
     * Applies a given effect given the spells current state
     *
     * @param state  The state of the spell at the current delivery method
     * @param effect The effect that needs to be applied
     */
    override fun defaultEffectProc(state: DeliveryTransitionState, effect: SpellComponentInstance<SpellEffect>) {
        // The effect is just applied to the target
        effect.component.procEffect(state, effect)
    }

    /**
     * Performs the default transition from this delivery method to the next
     *
     * @param state The state of the spell to transition
     */
    override fun performDefaultTransition(state: DeliveryTransitionState) {
        val spell = state.spell
        val spellIndex = state.stageIndex
        // Perform the transition between the next delivery method and the current delivery method
        spell.getStage(spellIndex + 1)!!.deliveryInstance!!.component.executeDelivery(
            DeliveryTransitionStateBuilder()
                .copyOf(state)
                .withStageIndex(spellIndex + 1)
                .build()
        )
    }
}
