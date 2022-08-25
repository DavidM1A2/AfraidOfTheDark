package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import com.davidm1a2.afraidofthedark.common.utility.getNormal
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.vector.Vector3d
import kotlin.math.PI
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

class ConeSpellDeliveryMethod : AOTDSpellDeliveryMethod("cone", ModResearches.MAGIC_MASTERY) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.doubleProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("radius"))
                .withSetter(this::setRadius)
                .withGetter(this::getRadius)
                .withDefaultValue(2.0)
                .withMinValue(1.0)
                .withMaxValue(10.0)
                .build()
        )
        addEditableProperty(
            SpellComponentPropertyFactory.doubleProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("length"))
                .withSetter(this::setLength)
                .withGetter(this::getLength)
                .withDefaultValue(4.0)
                .withMinValue(1.0)
                .withMaxValue(20.0)
                .build()
        )
    }

    override fun executeDelivery(state: DeliveryTransitionState) {
        val deliveryMethod = state.getCurrentStage().deliveryInstance!!
        val radius = getRadius(deliveryMethod)
        val length = getLength(deliveryMethod)

        val tipPos = state.position
        val forwardBackwardDir = state.direction

        // 1) Find the corners of the box surrounding the cone
        val leftRightDir = forwardBackwardDir.cross(state.normal).normalize()
        val downDir = state.normal.reverse()

        val downScaled = downDir.scale(radius)
        val leftRightScaled = leftRightDir.scale(radius)
        val forwardBackwardScaled = forwardBackwardDir.scale(length)

        val cornerOne = tipPos.add(downScaled).add(leftRightScaled)
        val cornerTwo = tipPos.add(forwardBackwardScaled).add(downScaled.reverse()).add(leftRightScaled.reverse())

        // 2) Find the smallest (x, y, z) and biggest (x, y, z) coordinates to loop through. Ceil/floor the values, so we don't cut off any blocks partially within the cone
        val minX = floor(min(cornerOne.x, cornerTwo.x)).toInt()
        val minY = floor(min(cornerOne.y, cornerTwo.y)).toInt()
        val minZ = floor(min(cornerOne.z, cornerTwo.z)).toInt()
        val maxX = ceil(max(cornerOne.x, cornerTwo.x)).toInt()
        val maxY = ceil(max(cornerOne.y, cornerTwo.y)).toInt()
        val maxZ = ceil(max(cornerOne.z, cornerTwo.z)).toInt()

        // 3) We'll need a triple for loop to go over every block in the rectangle surrounding the cone. We'll filter out any points that lie outside the cone.
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    val conePos = Vector3d(x.toDouble(), y.toDouble(), z.toDouble())
                    if (isWithinCone(tipPos, forwardBackwardDir, length, radius, conePos)) {
                        var newDirection = conePos.subtract(tipPos).normalize()
                        // Direction may be 0 if conePos = tipPos. In this case, move it up
                        if (newDirection == Vector3d.ZERO) {
                            newDirection = Vector3d(0.0, 1.0, 0.0)
                        }
                        var newNormal = newDirection.getNormal()
                        // Straight up means we can't know our normal. Just use 1, 0, 0
                        if (newNormal == Vector3d.ZERO) {
                            newNormal = Vector3d(1.0, 0.0, 0.0)
                        }

                        val newState = DeliveryTransitionState(
                            spell = state.spell,
                            stageIndex = state.stageIndex,
                            world = state.world,
                            position = conePos,
                            blockPosition = BlockPos(conePos),
                            direction = newDirection,
                            normal = newNormal,
                            casterEntity = state.casterEntity
                        )
                        procEffects(newState)
                        transitionFrom(newState)
                    }
                }
            }
        }
    }

    private fun isWithinCone(tip: Vector3d, direction: Vector3d, height: Double, radius: Double, point: Vector3d): Boolean {
        // To detect if a point is within the cone or not, we can use: https://stackoverflow.com/questions/12826117/how-can-i-detect-if-a-point-is-inside-a-cone-or-not-in-3d-space
        val coneDistance = point.subtract(tip).dot(direction)
        if (coneDistance < 0 || coneDistance > height) {
            return false
        }

        val coneRadius = (coneDistance / height) * radius
        val orthographicDistance = point.subtract(tip).subtract(direction.scale(coneDistance)).length()
        return orthographicDistance < coneRadius
    }

    override fun getDeliveryCost(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return 0.0
    }

    override fun getMultiplicity(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return estimateBlocksHit(instance).coerceAtLeast(1.0)
    }

    private fun estimateBlocksHit(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        val radius = getRadius(instance)
        val length = getLength(instance)
        return PI * radius * radius * length / 3
    }

    private fun setRadius(instance: SpellComponentInstance<*>, radius: Double) {
        instance.data.putDouble(NBT_RADIUS, radius)
    }

    private fun getRadius(instance: SpellComponentInstance<*>): Double {
        return instance.data.getDouble(NBT_RADIUS)
    }

    private fun setLength(instance: SpellComponentInstance<*>, length: Double) {
        instance.data.putDouble(NBT_LENGTH, length)
    }

    private fun getLength(instance: SpellComponentInstance<*>): Double {
        return instance.data.getDouble(NBT_LENGTH)
    }

    companion object {
        private const val NBT_RADIUS = "radius"
        private const val NBT_LENGTH = "length"
    }
}