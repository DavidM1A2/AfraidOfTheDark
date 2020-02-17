package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation

/**
 * Effect that feeds a hit player
 *
 * @constructor adds the editable prop
 */
class SpellEffectFeed : AOTDSpellEffect(ResourceLocation(Constants.MOD_ID, "feed"))
{
    init
    {
        addEditableProperty(
                SpellComponentPropertyFactory.intProperty()
                        .withName("Hunger Amount")
                        .withDescription("The amount of food half 'drumsticks' to restore.")
                        .withSetter { instance, newValue -> instance.data.setInteger(NBT_HUNGER_VALUE, newValue) }
                        .withGetter { it.data.getInteger(NBT_HUNGER_VALUE) }
                        .withDefaultValue(2)
                        .withMinValue(1)
                        .withMaxValue(300)
                        .build()
        )
        addEditableProperty(
                SpellComponentPropertyFactory.intProperty()
                        .withName("Saturation Amount")
                        .withDescription("The amount of saturation restore.")
                        .withSetter { instance, newValue -> instance.data.setInteger(NBT_SATURATION_VALUE, newValue) }
                        .withGetter { it.data.getInteger(NBT_SATURATION_VALUE) }
                        .withDefaultValue(1)
                        .withMinValue(0)
                        .build()
        )
    }

    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     * @param instance The instance of the effect
     */
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>)
    {
        val entity = state.getEntity()
        if (entity is EntityPlayer)
        {
            createParticlesAt(1, 2, state.position, entity.dimension)
            val foodStats = entity.foodStats
            foodStats.addStats(getHungerValue(instance), getSaturationValue(instance).toFloat())
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
        return getHungerValue(instance) / 2.0 + getSaturationValue(instance) * 2.0
    }

    /**
     * Gets the hunger value this feed instance adds
     *
     * @param instance The instance of the feed effect
     * @return The hunger value this instance adds
     */
    fun getHungerValue(instance: SpellComponentInstance<SpellEffect>): Int
    {
        return instance.data.getInteger(NBT_HUNGER_VALUE)
    }

    /**
     * Gets the saturation value this feed instance adds
     *
     * @param instance The instance of the feed effect
     * @return The saturation value this instance adds
     */
    fun getSaturationValue(instance: SpellComponentInstance<SpellEffect>): Int
    {
        return instance.data.getInteger(NBT_SATURATION_VALUE)
    }

    companion object
    {
        // NBT constants
        private const val NBT_HUNGER_VALUE = "hunger_value"
        private const val NBT_SATURATION_VALUE = "saturation_value"
    }
}