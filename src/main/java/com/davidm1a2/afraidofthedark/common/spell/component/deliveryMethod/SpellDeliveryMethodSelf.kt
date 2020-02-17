package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionStateBuilder
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraft.util.ResourceLocation

/**
 * Self delivery method delivers the spell to the caster
 *
 * @constructor does not initialize anything
 */
class SpellDeliveryMethodSelf : AOTDSpellDeliveryMethod(ResourceLocation(Constants.MOD_ID, "self"))
{
    /**
     * Called to begin delivering the effects to the target by whatever means necessary
     *
     * @param state The state of the spell to deliver
     */
    override fun executeDelivery(state: DeliveryTransitionState)
    {
        // Self just procs the effects and transitions at the target entity
        if (state.getEntity() != null)
        {
            this.procEffects(state)
            this.transitionFrom(state)
        }
    }

    /**
     * Applies a given effect given the spells current state
     *
     * @param state  The state of the spell at the current delivery method
     * @param effect The effect that needs to be applied
     */
    override fun defaultEffectProc(state: DeliveryTransitionState, effect: SpellComponentInstance<SpellEffect>)
    {
        // The effect is just applied to the target
        effect.component.procEffect(state, effect)
    }

    /**
     * Performs the default transition from this delivery method to the next
     *
     * @param state The state of the spell to transition
     */
    override fun performDefaultTransition(state: DeliveryTransitionState)
    {
        val spell = state.spell
        val spellIndex = state.stageIndex

        // Perform the transition between the next delivery method and the current delivery method
        spell.getStage(spellIndex + 1)!!.deliveryInstance!!.component.executeDelivery(
                DeliveryTransitionStateBuilder()
                        .withSpell(spell)
                        .withStageIndex(spellIndex + 1)
                        .withEntity(state.getEntity()!!)
                        .build()
        )
    }

    /**
     * Gets the cost of the delivery method
     *
     * @return The cost of the delivery method
     */
    override fun getCost(instance: SpellComponentInstance<SpellDeliveryMethod>): Double
    {
        return 1.0
    }

    /**
     * Gets the multiplier that this delivery method will apply to the stage it's in
     *
     * @return The spell stage multiplier for cost
     */
    override fun getStageCostMultiplier(instance: SpellComponentInstance<SpellDeliveryMethod>): Double
    {
        return 1.0
    }
}