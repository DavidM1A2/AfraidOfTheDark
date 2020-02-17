package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod

import com.davidm1a2.afraidofthedark.common.capabilities.world.SpellStateData
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionStateBuilder
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.util.ResourceLocation

/**
 * Self delivery method delivers the spell after a delay
 *
 * @constructor initializes the editable properties
 */
class SpellDeliveryMethodDelay : AOTDSpellDeliveryMethod(ResourceLocation(Constants.MOD_ID, "delay"))
{
    init
    {
        addEditableProperty(
                SpellComponentPropertyFactory.longProperty()
                        .withName("Delay")
                        .withDescription("The delay of the delivery in ticks (20 ticks = 1 second).")
                        .withSetter { instance, newValue -> instance.data.setLong(NBT_DELAY, newValue) }
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
    override fun executeDelivery(state: DeliveryTransitionState)
    {
        // Delayed adds this spell to the queue to wait
        val spellStateData = SpellStateData.get(state.world)
        spellStateData.addDelayedDelivery(state)
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
                        .copyOf(state)
                        .withStageIndex(spellIndex + 1)
                        .withDeliveryEntity(null)
                        .build()
        )
    }

    /**
     * Gets the cost of the delivery method
     *
     * @param instance The spell delivery method instance
     * @return The cost of the delivery method
     */
    override fun getCost(instance: SpellComponentInstance<SpellDeliveryMethod>): Double
    {
        return 10 + getDelay(instance) / 10.0
    }

    /**
     * Gets the multiplier that this delivery method will apply to the stage it's in
     *
     * @param instance The spell delivery method instance
     * @return The spell stage multiplier for cost
     */
    override fun getStageCostMultiplier(instance: SpellComponentInstance<SpellDeliveryMethod>): Double
    {
        return 1.0
    }

    /**
     * Gets the delay of the delivery in ticks
     *
     * @param instance The spell delivery method instance
     * @return the delay of the delivery in ticks
     */
    fun getDelay(instance: SpellComponentInstance<SpellDeliveryMethod>): Long
    {
        return instance.data.getLong(NBT_DELAY)
    }

    companion object
    {
        // The NBT keys
        private const val NBT_DELAY = "delay"
    }
}