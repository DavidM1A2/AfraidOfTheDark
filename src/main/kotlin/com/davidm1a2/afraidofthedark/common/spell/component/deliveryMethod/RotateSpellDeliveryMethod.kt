package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionStateBuilder
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import com.davidm1a2.afraidofthedark.common.utility.Matrix3d
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.Vec3d
import kotlin.math.cos
import kotlin.math.sin

class RotateSpellDeliveryMethod : AOTDSpellDeliveryMethod(ResourceLocation(Constants.MOD_ID, "rotate")) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.doubleProperty()
                .withName("Yaw")
                .withDescription("The yaw (left/right rotation) to apply in degrees. Yaw gets applied before pitch")
                .withSetter { instance, newValue -> instance.data.putDouble(NBT_YAW, newValue) }
                .withGetter { it.data.getDouble(NBT_YAW) }
                .withDefaultValue(0.0)
                .withMinValue(-180.0)
                .withMaxValue(180.0)
                .build()
        )
        addEditableProperty(
            SpellComponentPropertyFactory.doubleProperty()
                .withName("Pitch")
                .withDescription("The pitch (up/down rotation) to apply in degrees. Yaw gets applied before pitch")
                .withSetter { instance, newValue -> instance.data.putDouble(NBT_PITCH, newValue) }
                .withGetter { it.data.getDouble(NBT_PITCH) }
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
        val leftRightDir = forwardBackwardDir.crossProduct(Vec3d(0.0, 1.0, 0.0)).normalize()
        val upDownDir = forwardBackwardDir.crossProduct(leftRightDir).normalize()
        val finalDir = forwardBackwardDir.rotateAround(upDownDir, yaw).rotateAround(leftRightDir, pitch)

        val newState = DeliveryTransitionStateBuilder()
            .withSpell(state.spell)
            .withStageIndex(state.stageIndex)
            .withWorld(state.world)
            .withPosition(state.position)
            .withBlockPosition(state.blockPosition)
            .withDirection(finalDir)
            .withCasterEntity(state.getCasterEntity())
            .build()

        procEffects(newState)
        transitionFrom(newState)
    }

    override fun getCost(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return 10.0
    }

    override fun getStageCostMultiplier(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return 1.0
    }

    fun getYaw(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return instance.data.getDouble(NBT_YAW)
    }

    fun getPitch(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return instance.data.getDouble(NBT_PITCH)
    }

    companion object {
        // The NBT keys
        private const val NBT_YAW = "yaw"
        private const val NBT_PITCH = "pitch"
        private fun Vec3d.rotateAround(axis: Vec3d, radians: Double): Vec3d {
            // Use the Rodrigues formula to construct a rotation matrix around "axis" "radians" amount
            // https://math.stackexchange.com/questions/2741515/rotation-around-a-vector
            val basisMatrix = Matrix3d(
                0.0, -axis.z, axis.y,
                axis.z, 0.0, -axis.x,
                -axis.y, axis.x, 0.0
            )

            val termTwo = basisMatrix.clone()
                .mul(sin(radians))
            val termThree = basisMatrix.clone()
                .mul(basisMatrix.clone())
                .mul(1 - cos(radians))
            val rotationMatrix = Matrix3d()
                .setIdentity()
                .add(termTwo)
                .add(termThree)

            return rotationMatrix.mul(this)
        }
    }
}