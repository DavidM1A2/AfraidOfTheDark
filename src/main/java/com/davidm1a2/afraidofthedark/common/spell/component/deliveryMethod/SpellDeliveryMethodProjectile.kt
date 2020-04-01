package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.entity.spell.projectile.EntitySpellProjectile
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionStateBuilder
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.util.ResourceLocation

/**
 * Projectile delivery method delivers the spell to the target with a projectile
 *
 * @constructor initializes the editable properties
 */
class SpellDeliveryMethodProjectile : AOTDSpellDeliveryMethod(ResourceLocation(Constants.MOD_ID, "projectile")) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.doubleProperty()
                .withName("Range")
                .withDescription("The range of the projectile in blocks.")
                .withSetter { instance, newValue -> instance.data.setDouble(NBT_RANGE, newValue) }
                .withGetter { it.data.getDouble(NBT_RANGE) }
                .withDefaultValue(50.0)
                .withMinValue(1.0)
                .withMaxValue(300.0)
                .build()
        )
        addEditableProperty(
            SpellComponentPropertyFactory.doubleProperty()
                .withName("Speed")
                .withDescription("The speed of the projectile in blocks/tick.")
                .withSetter { instance, newValue -> instance.data.setDouble(NBT_SPEED, newValue) }
                .withGetter { it.data.getDouble(NBT_SPEED) }
                .withDefaultValue(0.6)
                .withMinValue(0.0)
                .withMaxValue(10.0)
                .build()
        )
    }

    /**
     * Called to deliver the effects to the target by whatever means necessary
     *
     * @param state The state of the spell to deliver
     */
    override fun executeDelivery(state: DeliveryTransitionState) {
        val spellProjectile = EntitySpellProjectile(
            state.world,
            state.spell,
            state.stageIndex,
            state.getCasterEntity(),
            state.position,
            state.direction,
            state.getEntity()
        )
        state.world.spawnEntity(spellProjectile)
    }

    /**
     * Applies a given effect given the spells current state
     *
     * @param state  The state of the spell at the current delivery method
     * @param effect The effect that needs to be applied
     */
    override fun defaultEffectProc(state: DeliveryTransitionState, effect: SpellComponentInstance<SpellEffect>) {
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
                .withDeliveryEntity(null)
                .build()
        )
    }

    /**
     * Gets the cost of the delivery method
     *
     * @return The cost of the delivery method
     */
    override fun getCost(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return 5 + getSpeed(instance) + getRange(instance) / 15.0
    }

    /**
     * Gets the multiplier that this delivery method will apply to the stage it's in
     *
     * @return The spell stage multiplier for cost
     */
    override fun getStageCostMultiplier(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return 1.1
    }

    /**
     * Gets the projectile speed
     *
     * @param instance The delivery method instance
     * @return the projectile speed
     */
    fun getSpeed(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return instance.data.getDouble(NBT_SPEED)
    }

    /**
     * Gets the projectile range
     *
     * @param instance The delivery method instance
     * @return the projectile range
     */
    fun getRange(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return instance.data.getDouble(NBT_RANGE)
    }

    companion object {
        // The NBT keys
        private const val NBT_SPEED = "speed"
        private const val NBT_RANGE = "range"
    }
}