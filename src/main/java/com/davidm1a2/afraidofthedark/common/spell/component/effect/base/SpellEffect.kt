package com.davidm1a2.afraidofthedark.common.spell.component.effect.base

import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponent
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import net.minecraft.util.ResourceLocation

/**
 * Entry used to store a reference to an effect
 *
 * @constructor just passes on the id and factory
 * @param id      The ID of this delivery method entry
 */
abstract class SpellEffect(id: ResourceLocation) : SpellComponent<SpellEffect>(id, ResourceLocation(id.resourceDomain, "textures/gui/spell_component/effects/${id.resourcePath}.png"))
{
    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     * @param instance The instance of the effect
     */
    abstract fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>)

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
    override fun getUnlocalizedName(): String
    {
        return "effect." + registryName.toString()
    }
}