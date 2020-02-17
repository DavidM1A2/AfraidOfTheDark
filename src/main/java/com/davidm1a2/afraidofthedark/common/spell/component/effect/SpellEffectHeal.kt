package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.util.ResourceLocation

/**
 * Effect that heals a hit entity
 *
 * @constructor adds the editable prop
 */
class SpellEffectHeal : AOTDSpellEffect(ResourceLocation(Constants.MOD_ID, "heal"))
{
    init
    {
        addEditableProperty(
                SpellComponentPropertyFactory.intProperty()
                        .withName("Heal Amount")
                        .withDescription("The amount of half hearts to restore.")
                        .withSetter { instance, newValue -> instance.data.setInteger(NBT_HEALING_AMOUNT, newValue) }
                        .withGetter { it.data.getInteger(NBT_HEALING_AMOUNT) }
                        .withDefaultValue(2)
                        .withMinValue(1)
                        .build()
        )
    }

    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>)
    {
        val entity = state.getEntity()
        if (entity is EntityLivingBase && entity !is EntityArmorStand)
        {
            createParticlesAt(1, 3, state.position, entity.dimension)
            entity.heal(getHealAmount(instance).toFloat())
        }
    }

    /**
     * Gets the cost of the delivery method
     *
     * @param instance The instance of the spell effect to grab the cost of
     * @return The cost of the delivery method
     */
    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double
    {
        return getHealAmount(instance) * 2.0
    }

    /**
     * Gets the number of half hearts this spell effect heals
     *
     * @param instance The instance of this effect
     * @return The number of half hearts to heal
     */
    fun getHealAmount(instance: SpellComponentInstance<SpellEffect>): Int
    {
        return instance.data.getInteger(NBT_HEALING_AMOUNT)
    }

    companion object
    {
        // NBT constants for healing amount
        private const val NBT_HEALING_AMOUNT = "healing_amount"
    }
}