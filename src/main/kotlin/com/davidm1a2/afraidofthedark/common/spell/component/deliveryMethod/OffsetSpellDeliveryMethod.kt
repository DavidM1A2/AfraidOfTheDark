package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionStateBuilder
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import kotlin.math.sqrt

class OffsetSpellDeliveryMethod : AOTDSpellDeliveryMethod(ResourceLocation(Constants.MOD_ID, "offset")) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.doubleProperty()
                .withName("X Position Offset")
                .withDescription("The X position offset to deliver at")
                .withSetter { instance, newValue -> instance.data.putDouble(NBT_X_POSITION_OFFSET, newValue) }
                .withGetter { it.data.getDouble(NBT_X_POSITION_OFFSET) }
                .withDefaultValue(0.0)
                .withMinValue(-60_000_000.0)
                .withMaxValue(60_000_000.0)
                .build()
        )
        addEditableProperty(
            SpellComponentPropertyFactory.doubleProperty()
                .withName("Y Position Offset")
                .withDescription("The Y position offset to deliver at")
                .withSetter { instance, newValue -> instance.data.putDouble(NBT_Y_POSITION_OFFSET, newValue) }
                .withGetter { it.data.getDouble(NBT_Y_POSITION_OFFSET) }
                .withDefaultValue(0.0)
                .withMinValue(-60_000_000.0)
                .withMaxValue(60_000_000.0)
                .build()
        )
        addEditableProperty(
            SpellComponentPropertyFactory.doubleProperty()
                .withName("Z Position Offset")
                .withDescription("The Z position offset to deliver at")
                .withSetter { instance, newValue -> instance.data.putDouble(NBT_Z_POSITION_OFFSET, newValue) }
                .withGetter { it.data.getDouble(NBT_Z_POSITION_OFFSET) }
                .withDefaultValue(0.0)
                .withMinValue(-60_000_000.0)
                .withMaxValue(60_000_000.0)
                .build()
        )
        addEditableProperty(
            SpellComponentPropertyFactory.booleanProperty()
                .withName("Direction Relative")
                .withDescription("If true, offsets will be relative to the delivery method's facing, where x is left/right, z is forward/back, and y is up/down. If false, offsets are based on world coordinates.")
                .withSetter { instance, newValue -> instance.data.putBoolean(NBT_IS_DIRECTION_RELATIVE, newValue) }
                .withGetter { it.data.getBoolean(NBT_IS_DIRECTION_RELATIVE) }
                .withDefaultValue(false)
                .build()
        )
    }

    override fun executeDelivery(state: DeliveryTransitionState) {
        val instance = state.getCurrentStage().deliveryInstance!!
        val xPositionOffset = getXPositionOffset(instance)
        val yPositionOffset = getYPositionOffset(instance)
        val zPositionOffset = getZPositionOffset(instance)

        val offset = if (isDirectionRelative(instance)) {
            val forwardBackwardDir = state.direction
            val leftRightDir = forwardBackwardDir.crossProduct(Vec3d(0.0, 1.0, 0.0))
            val upDownDir = forwardBackwardDir.crossProduct(leftRightDir)
            forwardBackwardDir.scale(zPositionOffset).add(leftRightDir.scale(xPositionOffset))
                .add(upDownDir.scale(yPositionOffset))
        } else {
            Vec3d(xPositionOffset, yPositionOffset, zPositionOffset)
        }

        val newX = (state.position.x + offset.x).coerceIn(-30_000_000.0, 30_000_000.0)
        val newY = (state.position.y + offset.y).coerceIn(-30_000_000.0, 30_000_000.0)
        val newZ = (state.position.z + offset.z).coerceIn(-30_000_000.0, 30_000_000.0)

        val newState = DeliveryTransitionStateBuilder()
            .withSpell(state.spell)
            .withStageIndex(state.stageIndex)
            .withWorld(state.world)
            .withPosition(Vec3d(newX, newY, newZ))
            .withBlockPosition(BlockPos(newX, newY, newZ))
            .withDirection(state.direction)
            .withCasterEntity(state.getCasterEntity())
            .build()
        procEffects(newState)
        transitionFrom(newState)
    }

    override fun getCost(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        val positionOffset = Vec3d(
            getXPositionOffset(instance),
            getYPositionOffset(instance),
            getZPositionOffset(instance)
        )
        return sqrt(if (positionOffset.x == 0.0 && positionOffset.y == 0.0 && positionOffset.z == 0.0) 0.0 else positionOffset.length())
    }

    override fun getStageCostMultiplier(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return 1.0
    }

    fun getXPositionOffset(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return instance.data.getDouble(NBT_X_POSITION_OFFSET)
    }

    fun getYPositionOffset(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return instance.data.getDouble(NBT_Y_POSITION_OFFSET)
    }

    fun getZPositionOffset(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return instance.data.getDouble(NBT_Z_POSITION_OFFSET)
    }

    fun isDirectionRelative(instance: SpellComponentInstance<SpellDeliveryMethod>): Boolean {
        return instance.data.getBoolean(NBT_IS_DIRECTION_RELATIVE)
    }

    companion object {
        // The NBT keys
        private const val NBT_X_POSITION_OFFSET = "x_position_offset"
        private const val NBT_Y_POSITION_OFFSET = "y_position_offset"
        private const val NBT_Z_POSITION_OFFSET = "z_position_offset"
        private const val NBT_IS_DIRECTION_RELATIVE = "is_direction_relative"
    }
}