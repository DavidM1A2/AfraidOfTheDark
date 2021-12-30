package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import com.davidm1a2.afraidofthedark.common.utility.getNormal
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.vector.Vector3d
import kotlin.math.floor
import kotlin.math.sqrt

/**
 * AOE method delivers the spell to the target in a circle
 *
 * @constructor initializes the editable properties
 */
class AOESpellDeliveryMethod : AOTDSpellDeliveryMethod(ResourceLocation(Constants.MOD_ID, "aoe"), ModResearches.ADVANCED_MAGIC) {
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

        val basePos = BlockPos(state.position)
        val blockRadius = floor(radius).toInt()

        // Go over every block in the radius
        for (x in -blockRadius..blockRadius) {
            for (y in -blockRadius..blockRadius) {
                for (z in -blockRadius..blockRadius) {
                    // Grab the blockpos
                    val aoePos = basePos.offset(x, y, z)
                    val distance = sqrt(aoePos.distSqr(basePos))

                    // Test to see if the block is within the radius
                    if (distance < radius && (!shellOnly || (radius - distance) < 1)) {
                        // Apply the effect at the position
                        val position = Vector3d.atCenterOf(aoePos)
                        val direction = position.subtract(state.position).normalize()
                        var normal = direction.getNormal()
                        // Straight up means we can't know our normal. Just use 1, 0, 0
                        if (normal == Vector3d.ZERO) {
                            normal = Vector3d(1.0, 0.0, 0.0)
                        }

                        val newState = DeliveryTransitionState(
                            spell = state.spell,
                            stageIndex = state.stageIndex,
                            world = state.world,
                            position = position,
                            blockPosition = aoePos,
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

    override fun getCost(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        val blocksHit = estimateBlocksHit(instance)
        // 5 is an arbitrary base cost
        return 5 + blocksHit
    }

    override fun getStageCostMultiplier(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return estimateBlocksHit(instance).coerceAtLeast(1.0)
    }

    private fun estimateBlocksHit(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        val shellOnly = getShellOnly(instance)
        val radius = getRadius(instance)
        return if (shellOnly) {
            4.0 * Math.PI * radius * radius
        } else {
            4.0 / 3.0 * Math.PI * radius * radius * radius
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