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
import kotlin.math.ceil

/**
 * AOE method delivers the spell to the target in a circle
 *
 * @constructor initializes the editable properties
 */
class AOESpellDeliveryMethod : AOTDSpellDeliveryMethod("aoe", ModResearches.ADVANCED_MAGIC) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.doubleProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("radius"))
                .withSetter(this::setRadius)
                .withGetter(this::getRadius)
                .withDefaultValue(3.0)
                .withMinValue(1.0)
                .withMaxValue(20.0)
                .build()
        )
        addEditableProperty(
            SpellComponentPropertyFactory.booleanProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("shell_only"))
                .withSetter(this::setShellOnly)
                .withGetter(this::getShellOnly)
                .withDefaultValue(false)
                .build()
        )
    }

    /**
     * Called to deliver the effects to the target by whatever means necessary
     *
     * @param state The state of the spell to deliver
     */
    override fun executeDelivery(state: DeliveryTransitionState) {
        val deliveryMethod = state.getCurrentStage().deliveryInstance!!
        val radius = getRadius(deliveryMethod)
        val shellOnly = getShellOnly(deliveryMethod)

        val centerPos = state.position
        val blockRadius = ceil(radius).toInt()

        // Go over every block in the radius
        for (x in -blockRadius..blockRadius) {
            for (y in -blockRadius..blockRadius) {
                for (z in -blockRadius..blockRadius) {
                    val aoePos = centerPos.add(x.toDouble(), y.toDouble(), z.toDouble())
                    val distance = aoePos.distanceTo(centerPos)

                    // Test to see if the block is within the radius. If we're in "shellOnly" mode, only take the outer ~1.5 blocks of shell
                    if (distance <= radius && (!shellOnly || (radius - distance) < 1.5)) {
                        var direction = aoePos.subtract(centerPos).normalize()
                        // Direction may be 0 if aoePos = centerPos. In this case, move it up
                        if (direction == Vector3d.ZERO) {
                            direction = Vector3d(0.0, 1.0, 0.0)
                        }
                        var normal = direction.getNormal()
                        // Straight up means we can't know our normal. Just use 1, 0, 0
                        if (normal == Vector3d.ZERO) {
                            normal = Vector3d(1.0, 0.0, 0.0)
                        }

                        val newState = DeliveryTransitionState(
                            spell = state.spell,
                            stageIndex = state.stageIndex,
                            world = state.world,
                            position = aoePos,
                            blockPosition = BlockPos(aoePos),
                            direction = direction,
                            normal = normal,
                            casterEntity = state.casterEntity
                        )
                        procEffects(newState)
                        transitionFrom(newState)
                    }
                }
            }
        }
    }

    override fun getDeliveryCost(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return 0.0
    }

    override fun getMultiplicity(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return estimateBlocksHit(instance).coerceAtLeast(1.0)
    }

    private fun estimateBlocksHit(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        val shellOnly = getShellOnly(instance)
        val radius = getRadius(instance)
        val volume = 4.0 / 3.0 * Math.PI * radius * radius * radius
        return if (shellOnly) {
            val innerVolume = 4.0 / 3.0 * Math.PI * (radius - 1) * (radius - 1) * (radius - 1)
            // Subtract out the inner volume to just leave the "shell"
            volume - innerVolume
        } else {
            volume
        }
    }

    fun setRadius(instance: SpellComponentInstance<*>, radius: Double) {
        instance.data.putDouble(NBT_RADIUS, radius)
    }

    fun getRadius(instance: SpellComponentInstance<*>): Double {
        return instance.data.getDouble(NBT_RADIUS)
    }

    fun setShellOnly(instance: SpellComponentInstance<*>, shellOnly: Boolean) {
        instance.data.putBoolean(NBT_SHELL_ONLY, shellOnly)
    }

    fun getShellOnly(instance: SpellComponentInstance<*>): Boolean {
        return instance.data.getBoolean(NBT_SHELL_ONLY)
    }

    companion object {
        // The NBT keys
        private const val NBT_RADIUS = "radius"
        private const val NBT_SHELL_ONLY = "shell_only"
    }
}