package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import com.davidm1a2.afraidofthedark.common.utility.rotateAround

class RotateSpellDeliveryMethod : AOTDSpellDeliveryMethod("rotate", ModResearches.FORBIDDEN_CITY) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.doubleProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("yaw"))
                .withSetter(this::setYaw)
                .withGetter(this::getYaw)
                .withDefaultValue(0.0)
                .withMinValue(-180.0)
                .withMaxValue(180.0)
                .build()
        )
        addEditableProperty(
            SpellComponentPropertyFactory.doubleProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("pitch"))
                .withSetter(this::setPitch)
                .withGetter(this::getPitch)
                .withDefaultValue(0.0)
                .withMinValue(-90.0)
                .withMaxValue(90.0)
                .build()
        )
    }

    override fun executeDelivery(state: DeliveryTransitionState) {
        val instance = state.getCurrentStage().deliveryInstance!!
        val yaw = Math.toRadians(getYaw(instance))
        val pitch = Math.toRadians(getPitch(instance))

        val forwardBackwardDir = state.direction

        val forwardBackwardDirAfterYaw = forwardBackwardDir.rotateAround(state.normal, yaw)
        val leftRightDir = state.normal.cross(forwardBackwardDirAfterYaw).normalize()

        val newDir = forwardBackwardDirAfterYaw.rotateAround(leftRightDir, pitch)
        val newNormal = state.normal.rotateAround(leftRightDir, pitch)

        val newState = state.copy(direction = newDir, normal = newNormal)

        procEffects(newState)
        transitionFrom(newState)
    }

    override fun getDeliveryCost(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return 0.2
    }

    override fun getMultiplicity(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return 1.0
    }

    fun setYaw(instance: SpellComponentInstance<*>, yaw: Double) {
        instance.data.putDouble(NBT_YAW, yaw)
    }

    fun getYaw(instance: SpellComponentInstance<*>): Double {
        return instance.data.getDouble(NBT_YAW)
    }

    fun setPitch(instance: SpellComponentInstance<*>, pitch: Double) {
        instance.data.putDouble(NBT_PITCH, pitch)
    }

    fun getPitch(instance: SpellComponentInstance<*>): Double {
        return instance.data.getDouble(NBT_PITCH)
    }

    companion object {
        // The NBT keys
        private const val NBT_YAW = "yaw"
        private const val NBT_PITCH = "pitch"
    }
}