package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellStates
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.util.ResourceLocation

/**
 * Self delivery method delivers the spell after a delay
 *
 * @constructor initializes the editable properties
 */
class DelaySpellDeliveryMethod : AOTDSpellDeliveryMethod(ResourceLocation(Constants.MOD_ID, "delay")) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.longProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("delay"))
                .withSetter { instance, newValue -> instance.data.putLong(NBT_DELAY, newValue) }
                .withGetter { it.data.getLong(NBT_DELAY) }
                .withDefaultValue(20L)
                .withMinValue(1L)
                .build()
        )
    }

    /**
     * Called to deliver the effects to the target by whatever means necessary
     *
     * @param state The state of the spell to deliver
     */
    override fun executeDelivery(state: DeliveryTransitionState) {
        // Delayed adds this spell to the queue to wait
        state.world.getSpellStates().addDelayedDelivery(state)
    }

    /**
     * Gets the cost of the delivery method
     *
     * @param instance The spell delivery method instance
     * @return The cost of the delivery method
     */
    override fun getCost(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return 10 + getDelay(instance) / 10.0
    }

    /**
     * Gets the multiplier that this delivery method will apply to the stage it's in
     *
     * @param instance The spell delivery method instance
     * @return The spell stage multiplier for cost
     */
    override fun getStageCostMultiplier(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return 1.5
    }

    /**
     * Gets the delay of the delivery in ticks
     *
     * @param instance The spell delivery method instance
     * @return the delay of the delivery in ticks
     */
    fun getDelay(instance: SpellComponentInstance<SpellDeliveryMethod>): Long {
        return instance.data.getLong(NBT_DELAY)
    }

    companion object {
        // The NBT keys
        private const val NBT_DELAY = "delay"
    }
}