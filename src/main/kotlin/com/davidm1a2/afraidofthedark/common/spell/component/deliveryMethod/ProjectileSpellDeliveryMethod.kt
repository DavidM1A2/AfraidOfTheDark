package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.entity.spell.projectile.SpellProjectileEntity
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.util.ResourceLocation
import java.awt.Color

/**
 * Projectile delivery method delivers the spell to the target with a projectile
 *
 * @constructor initializes the editable properties
 */
class ProjectileSpellDeliveryMethod : AOTDSpellDeliveryMethod(ResourceLocation(Constants.MOD_ID, "projectile"), ModResearches.ENCHANTED_FROGS) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.doubleProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("range"))
                .withSetter(this::setRange)
                .withGetter(this::getRange)
                .withDefaultValue(50.0)
                .withMinValue(1.0)
                .withMaxValue(300.0)
                .build()
        )
        addEditableProperty(
            SpellComponentPropertyFactory.doubleProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("speed"))
                .withSetter(this::setSpeed)
                .withGetter(this::getSpeed)
                .withDefaultValue(0.6)
                .withMinValue(0.0)
                .withMaxValue(10.0)
                .build()
        )
        addEditableProperty(
            SpellComponentPropertyFactory.colorProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("color"))
                .withSetter(this::setColor)
                .withGetter(this::getColor)
                .withDefaultValue(Color(155, 0, 255))
                .build()
        )
    }

    /**
     * Called to deliver the effects to the target by whatever means necessary
     *
     * @param state The state of the spell to deliver
     */
    override fun executeDelivery(state: DeliveryTransitionState) {
        val spellProjectile = SpellProjectileEntity(
            state.world,
            state.spell,
            state.stageIndex,
            state.casterEntity,
            state.position,
            state.direction,
            state.entity
        )
        state.world.addFreshEntity(spellProjectile)
    }

    /**
     * Gets the cost of the delivery method
     *
     * @return The cost of the delivery method
     */
    override fun getCost(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        // Cost of using the delivery method
        val baseCost = 1.0
        // 4 vitae per speed
        val speedCost = getSpeed(instance) * 4.0
        // 50 range per vitae
        val rangeCost = getRange(instance) * 0.02
        return baseCost + speedCost + rangeCost
    }

    /**
     * Gets the multiplier that this delivery method will apply to the stage it's in
     *
     * @return The spell stage multiplier for cost
     */
    override fun getStageCostMultiplier(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return 1.0
    }

    fun setSpeed(instance: SpellComponentInstance<*>, speed: Double) {
        instance.data.putDouble(NBT_SPEED, speed)
    }

    fun getSpeed(instance: SpellComponentInstance<*>): Double {
        return instance.data.getDouble(NBT_SPEED)
    }

    fun setRange(instance: SpellComponentInstance<*>, range: Double) {
        instance.data.putDouble(NBT_RANGE, range)
    }

    fun getRange(instance: SpellComponentInstance<*>): Double {
        return instance.data.getDouble(NBT_RANGE)
    }

    fun setColor(instance: SpellComponentInstance<*>, color: Color) {
        instance.data.putString(NBT_COLOR, "${color.red} ${color.green} ${color.blue}")
    }

    fun getColor(instance: SpellComponentInstance<*>): Color {
        val rgb = instance.data.getString(NBT_COLOR).split(Regex("\\s+")).map { it.toInt() }
        return Color(rgb[0], rgb[1], rgb[2])
    }

    companion object {
        // The NBT keys
        private const val NBT_SPEED = "speed"
        private const val NBT_RANGE = "range"
        private const val NBT_COLOR = "color"
    }
}