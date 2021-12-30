package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base

import com.davidm1a2.afraidofthedark.common.research.Research
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponent
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import net.minecraft.util.ResourceLocation

/**
 * Entry used to store a reference to a delivery method
 *
 * @constructor just passes on the id and factory
 * @param id The ID of this delivery method entry
 * @param prerequisiteResearch The research required to use this component, or null if none is requiredry entry type to transitioner to fire to move from that delivery method to this one
 */
abstract class SpellDeliveryMethod(id: ResourceLocation, prerequisiteResearch: Research?) : SpellComponent<SpellDeliveryMethod>(
    id,
    ResourceLocation(id.namespace, "textures/gui/spell_component/delivery_methods/${id.path}.png"),
    prerequisiteResearch
) {
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
        for (effect in state.getCurrentStage().effects) {
            effect?.component?.procEffect(state, effect)
        }
    }

    /**
     * Called after the delivery finishes and we transition from this state into the next
     *
     * @param state The state of the spell to transition FROM
     */
    fun transitionFrom(state: DeliveryTransitionState) {
        val spell = state.spell
        val nextStageIndex = state.stageIndex + 1
        // Test if we can transition to the next stage
        if (spell.hasStage(nextStageIndex)) {
            // Perform the transition between the next delivery method and the current delivery method
            spell.getStage(nextStageIndex)!!
                .deliveryInstance!!
                .component
                .executeDelivery(state.copy(stageIndex = nextStageIndex, deliveryEntity = null))
        }
    }

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
}