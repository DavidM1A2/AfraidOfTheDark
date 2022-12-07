package com.davidm1a2.afraidofthedark.common.spell.component.effect.base

import com.davidm1a2.afraidofthedark.common.research.Research
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponent
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import net.minecraft.util.ResourceLocation

/**
 * Entry used to store a reference to an effect
 *
 * @constructor just passes on the id and factory
 * @param id The ID of this delivery method entry
 * @param prerequisiteResearch The research required to use this component, or null if none is required
 */
abstract class SpellEffect(id: ResourceLocation, prerequisiteResearch: Research?) : SpellComponent<SpellEffect>(
    id,
    ResourceLocation(id.namespace, "textures/gui/spell_component/effects/${id.path}.png"),
    prerequisiteResearch
) {
    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     * @param instance The instance of the effect
     * @return The result of procing the effect
     */
    abstract fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>): ProcResult

    /**
     * Gets the cost of the delivery method
     *
     * @param instance The instance of the spell effect to grab the cost of
     * @return The cost of the delivery method
     */
    abstract fun getCost(instance: SpellComponentInstance<SpellEffect>): Double

    /**
     * @return Gets the unlocalized name of the component
     */
    override fun getUnlocalizedBaseName(): String {
        return "effect.${registryName!!.namespace}.${registryName!!.path}"
    }
}