package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.helper.TargetType
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import com.davidm1a2.afraidofthedark.common.utility.getLookNormal
import com.davidm1a2.afraidofthedark.common.utility.getNormal
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.vector.Vector3d
import kotlin.math.floor

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
            SpellComponentPropertyFactory.enumProperty<TargetType>()
                .withBaseName(getUnlocalizedPropertyBaseName("target_type"))
                .withSetter(this::setTargetType)
                .withGetter(this::getTargetType)
                .withDefaultValue(TargetType.BLOCK)
                .build()
        )
    }

    /**
     * Called to deliver the effects to the target by whatever means necessary
     *
     * @param state The state of the spell to deliver
     */
    override fun executeDelivery(state: DeliveryTransitionState) {
        // AOE just procs the effects and transitions in a sphere
        procEffects(state)
        transitionFrom(state)
    }

    /**
     * Applies a given effect given the spells current state
     *
     * @param state The state of the spell at the current delivery method
     * @param effect The effect that needs to be applied
     */
    override fun defaultEffectProc(state: DeliveryTransitionState, effect: SpellComponentInstance<SpellEffect>) {
        val radius = getRadius(state.getCurrentStage().deliveryInstance!!)

        // This AOE should target entities hit all nearby entities
        if (getTargetType(state.getCurrentStage().deliveryInstance!!) == TargetType.ENTITY) {
            // A list of nearby entities
            val entitiesWithinAABB =
                state.world.getEntitiesOfClass(
                    Entity::class.java,
                    AxisAlignedBB(BlockPos(state.position)).inflate(radius)
                )

            // Go over each nearby entity
            entitiesWithinAABB.forEach {
                // Apply it to the entity
                effect.component.procEffect(
                    DeliveryTransitionState(
                        spell = state.spell,
                        stageIndex = state.stageIndex,
                        world = state.world,
                        position = it.position(),
                        blockPosition = it.blockPosition(),
                        direction = it.lookAngle,
                        normal = it.getLookNormal(),
                        casterEntity = state.casterEntity,
                        entity = it
                    ),
                    effect
                )
            }
        } else {
            // Grab references to
            val basePos = BlockPos(state.position)

            // Compute the radius in blocks
            val blockRadius = floor(radius).toInt()

            // Go over every block in the radius
            for (x in -blockRadius..blockRadius) {
                for (y in -blockRadius..blockRadius) {
                    for (z in -blockRadius..blockRadius) {
                        // Grab the blockpos
                        val aoePos = basePos.offset(x, y, z)
                        // Test to see if the block is within the radius
                        if (aoePos.distSqr(basePos) < radius * radius) {
                            // Apply the effect at the position
                            val position = Vector3d.atCenterOf(aoePos)
                            val direction = position.subtract(state.position).normalize()
                            var normal = direction.getNormal()
                            // Straight up means we can't know our normal. Just use 1, 0, 0
                            if (normal == Vector3d.ZERO) {
                                normal = Vector3d(1.0, 0.0, 0.0)
                            }
                            effect.component.procEffect(
                                DeliveryTransitionState(
                                    spell = state.spell,
                                    stageIndex = state.stageIndex,
                                    world = state.world,
                                    position = position,
                                    blockPosition = aoePos,
                                    direction = direction,
                                    normal = normal,
                                    casterEntity = state.casterEntity
                                ),
                                effect,
                                reducedParticles = true
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Performs the default transition from this delivery method to the next
     *
     * @param state The state of the spell to transition
     */
    override fun performDefaultTransition(state: DeliveryTransitionState) {
        val spell = state.spell
        val spellIndex = state.stageIndex
        if (getTargetType(state.getCurrentStage().deliveryInstance!!) == TargetType.ENTITY) {
            // A list of nearby entities
            val entitiesWithinAABB =
                state.world.getEntitiesOfClass(
                    Entity::class.java,
                    AxisAlignedBB(BlockPos(state.position)).inflate(getRadius(state.getCurrentStage().deliveryInstance!!))
                )

            // Go over each nearby entity
            entitiesWithinAABB.forEach {
                // Perform the transition between the next delivery method and the current delivery method
                spell.getStage(spellIndex + 1)!!.deliveryInstance!!.component.executeDelivery(
                    DeliveryTransitionState(
                        spell = state.spell,
                        stageIndex = state.stageIndex + 1,
                        world = state.world,
                        position = it.getEyePosition(1.0f),
                        blockPosition = BlockPos(it.getEyePosition(1.0f)),
                        direction = it.lookAngle,
                        normal = it.getLookNormal(),
                        casterEntity = state.casterEntity
                    )
                )
            }
        } else {
            // Send out deliveries in all 6 possible directions around the hit point
            // Randomize which order the directions get applied in
            val cardinalDirections = mutableListOf(
                Vector3d(1.0, 0.0, 0.0),
                Vector3d(0.0, 1.0, 0.0),
                Vector3d(0.0, 0.0, 1.0),
                Vector3d(-1.0, 0.0, 0.0),
                Vector3d(0.0, -1.0, 0.0),
                Vector3d(0.0, 0.0, -1.0)
            ).apply { shuffle() }.toList()

            cardinalDirections.forEach {
                var normal = it.getNormal()
                if (normal == Vector3d.ZERO) {
                    normal = Vector3d(1.0, 0.0, 0.0)
                }
                // Perform the transition between the next delivery method and the current delivery method
                spell.getStage(spellIndex + 1)!!.deliveryInstance!!.component.executeDelivery(
                    DeliveryTransitionState(
                        spell = state.spell,
                        stageIndex = state.stageIndex + 1,
                        world = state.world,
                        position = state.position.add(it.scale(0.2)),
                        blockPosition = state.blockPosition,
                        direction = it,
                        normal = normal,
                        casterEntity = state.casterEntity
                    )
                )
            }
        }
    }

    /**
     * Gets the cost of the delivery method
     *
     * @return The cost of the delivery method
     */
    override fun getCost(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return 20 + getRadius(instance) * getRadius(instance) * getRadius(instance)
    }

    /**
     * Gets the multiplier that this delivery method will apply to the stage it's in
     *
     * @return The spell stage multiplier for cost
     */
    override fun getStageCostMultiplier(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return 6.0
    }

    fun setRadius(instance: SpellComponentInstance<*>, radius: Double) {
        instance.data.putDouble(NBT_RADIUS, radius)
    }

    fun getRadius(instance: SpellComponentInstance<*>): Double {
        return instance.data.getDouble(NBT_RADIUS)
    }

    fun setTargetType(instance: SpellComponentInstance<*>, targetType: TargetType) {
        instance.data.putInt(NBT_TARGET_TYPE, targetType.ordinal)
    }

    fun getTargetType(instance: SpellComponentInstance<*>): TargetType {
        return TargetType.values()[instance.data.getInt(NBT_TARGET_TYPE)]
    }

    companion object {
        // The NBT keys
        private const val NBT_RADIUS = "radius"
        private const val NBT_TARGET_TYPE = "target_type"
    }
}