package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base

import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponent
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraft.util.ResourceLocation

/**
 * Entry used to store a reference to a delivery method
 *
 * @constructor just passes on the id and factory
 * @param id      The ID of this delivery method entry
 * @property deliveryCustomTransitioners A map of previous delivery entry type to transitioner to fire to move from that delivery method to this one
 * @property deliveryEffectCustomApplicators A map of effect entries to custom effect applicators, used to specify how effects are applied
 */
abstract class SpellDeliveryMethod(id: ResourceLocation) :
    SpellComponent<SpellDeliveryMethod>(
        id,
        ResourceLocation(id.namespace, "textures/gui/spell_component/delivery_methods/${id.path}.png")
    ) {
    private val deliveryCustomTransitioners = mutableMapOf<SpellDeliveryMethod, ISpellDeliveryTransitioner>()
    private val deliveryEffectCustomApplicators = mutableMapOf<SpellEffect, ISpellDeliveryEffectApplicator>()

    /**
     * Called to begin delivering the effects to the target by whatever means necessary
     *
     * @param state The state of the spell at the current delivery method
     */
    abstract fun executeDelivery(state: DeliveryTransitionState)

    /**
     * Applies the effects that are a part of this delivery method to the targets
     *
     * @param state The state of the spell at the current delivery method
     */
    fun procEffects(state: DeliveryTransitionState) {
        // Go over each effect
        for (effect in state.getCurrentStage().effects) {
            if (effect != null) {
                // Test if there's a special custom applicator for this effect, if so use that
                val customApplicator = getApplicator(effect.component)
                if (customApplicator != null) {
                    // Test if the custom application was successful
                    val customApplicationSuccessful = customApplicator.procEffect(state, effect)
                    // If it was not, use the default logic
                    if (!customApplicationSuccessful) {
                        defaultEffectProc(state, effect)
                    }
                } else {
                    defaultEffectProc(state, effect)
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
    abstract fun defaultEffectProc(state: DeliveryTransitionState, effect: SpellComponentInstance<SpellEffect>)

    /**
     * Called after the delivery finishes and we transition from this state into the next
     *
     * @param state The state of the spell to transition FROM
     */
    fun transitionFrom(state: DeliveryTransitionState) {
        val spell = state.spell
        // Test if we can transition to the next stage
        if (spell.hasStage(state.stageIndex + 1)) {
            // Grab the next delivery method
            val nextDeliveryInstance = spell.getStage(state.stageIndex + 1)!!.deliveryInstance!!.component
            // Grab the transitioner from the current delivery method to the next one
            val transitioner =
                nextDeliveryInstance.getTransitioner(state.getCurrentStage().deliveryInstance!!.component)
            // Perform the transition if a custom transitioner is present
            transitioner?.transition(state) ?: performDefaultTransition(state)
        }
    }

    /**
     * Performs the default transition from this delivery method to the next
     *
     * @param state The state of the spell to transition
     */
    abstract fun performDefaultTransition(state: DeliveryTransitionState)

    /**
     * Gets the cost of the delivery method
     *
     * @return The cost of the delivery method
     */
    abstract fun getCost(instance: SpellComponentInstance<SpellDeliveryMethod>): Double

    /**
     * Gets the multiplier that this delivery method will apply to the stage it's in
     *
     * @return The spell stage multiplier for cost
     */
    abstract fun getStageCostMultiplier(instance: SpellComponentInstance<SpellDeliveryMethod>): Double

    /**
     * @return Gets the unlocalized name of the component
     */
    override fun getUnlocalizedBaseName(): String {
        return "delivery_method.${registryName!!.namespace}.${registryName!!.path}"
    }

    /**
     * Adds a custom transitioner to this delivery method
     *
     * @param previous     The previous delivery method that causes this custom transitioner
     * @param transitioner The transitioner to be used if the previous delivery method fired
     */
    fun addCustomTransitioner(previous: SpellDeliveryMethod, transitioner: ISpellDeliveryTransitioner) {
        deliveryCustomTransitioners[previous] = transitioner
    }

    /**
     * Adds a custom applicator to this effect for the current delivery method
     *
     * @param effectEntry         The effect to modify application of
     * @param applicator          The applicator to modify the application with
     */
    fun addCustomEffectApplicator(effectEntry: SpellEffect, applicator: ISpellDeliveryEffectApplicator) {
        deliveryEffectCustomApplicators[effectEntry] = applicator
    }

    /**
     * Gets the transitioner to use for going to this delivery method from a given previous delivery method
     *
     * @param previous The previous delivery method type
     * @return The transitioner to use to go from the previous delivery method to this one
     */
    private fun getTransitioner(previous: SpellDeliveryMethod): ISpellDeliveryTransitioner? {
        return deliveryCustomTransitioners[previous]
    }

    /**
     * Gets the applicator to use for this effect or the default if no custom one was specified
     *
     * @param effectEntry The effect entry to apply
     * @return The applicator to use to apply this effect
     */
    private fun getApplicator(effectEntry: SpellEffect): ISpellDeliveryEffectApplicator? {
        return deliveryEffectCustomApplicators[effectEntry]
    }
}