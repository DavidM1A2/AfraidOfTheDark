package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionStateBuilder
import com.davidm1a2.afraidofthedark.common.spell.component.InvalidValueException
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.helper.TargetType
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentProperty
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import kotlin.math.floor

/**
 * AOE method delivers the spell to the target in a circle
 *
 * @constructor initializes the editable properties
 */
class SpellDeliveryMethodAOE : AOTDSpellDeliveryMethod(ResourceLocation(Constants.MOD_ID, "aoe"))
{
    init
    {
        addEditableProperty(
            SpellComponentPropertyFactory.doubleProperty()
                .withName("Radius")
                .withDescription("The area of effect radius in blocks.")
                .withSetter { instance, newValue -> instance.data.setDouble(NBT_RADIUS, newValue) }
                .withGetter { it.data.getDouble(NBT_RADIUS) }
                .withDefaultValue(3.0)
                .withMinValue(1.0)
                .withMaxValue(150.0)
                .build()
        )
        addEditableProperty(
            SpellComponentProperty(
                "Target Type",
                "Should be either 'entity' or 'block'. If the target type is 'block' all nearby blocks will be affected, if it is 'entity' all nearby entities will be affected.",
                { instance, newValue ->
                    // Check the two valid options first
                    when
                    {
                        newValue.equals("entity", ignoreCase = true) ->
                        {
                            instance.data.setInteger(NBT_TARGET_TYPE, TargetType.ENTITY.ordinal)
                        }
                        newValue.equals("block", ignoreCase = true) ->
                        {
                            instance.data.setInteger(NBT_TARGET_TYPE, TargetType.BLOCK.ordinal)
                        }
                        else ->
                        {
                            throw InvalidValueException("Invalid value $newValue, should be 'entity' or 'block'")
                        }
                    }
                },
                {
                    @Suppress("UNCHECKED_CAST")
                    if (getTargetType(it as SpellComponentInstance<SpellDeliveryMethod>) == TargetType.ENTITY) "entity" else "block"
                },
                {
                    it.data.setInteger(NBT_TARGET_TYPE, TargetType.BLOCK.ordinal)
                }
            )
        )
    }

    /**
     * Called to deliver the effects to the target by whatever means necessary
     *
     * @param state The state of the spell to deliver
     */
    override fun executeDelivery(state: DeliveryTransitionState)
    {
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
    override fun defaultEffectProc(state: DeliveryTransitionState, effect: SpellComponentInstance<SpellEffect>)
    {
        val radius = getRadius(state.getCurrentStage().deliveryInstance!!)

        // This AOE should target entities hit all nearby entities
        if (getTargetType(state.getCurrentStage().deliveryInstance!!) == TargetType.ENTITY)
        {
            // A list of nearby entities
            val entitiesWithinAABB =
                state.world.getEntitiesWithinAABB(Entity::class.java, AxisAlignedBB(BlockPos(state.position)).grow(radius))

            // Go over each nearby entity
            entitiesWithinAABB.forEach()
            {
                // Apply it to the entity
                effect.component.procEffect(
                    DeliveryTransitionStateBuilder()
                        .withSpell(state.spell)
                        .withStageIndex(state.stageIndex)
                        .withEntity(it!!)
                        .build(),
                    effect
                )
            }
        }
        else
        {
            // Grab references to
            val basePos = BlockPos(state.position)

            // Compute the radius in blocks
            val blockRadius = floor(radius).toInt()
            val transitionBuilder = DeliveryTransitionStateBuilder()
                .withSpell(state.spell)
                .withStageIndex(state.stageIndex)
                .withWorld(state.world)

            // Go over every block in the radius
            for (x in -blockRadius..blockRadius)
            {
                for (y in -blockRadius..blockRadius)
                {
                    for (z in -blockRadius..blockRadius)
                    {
                        // Grab the blockpos
                        val aoePos = basePos.add(x, y, z)
                        // Test to see if the block is within the radius
                        if (aoePos.distanceSq(basePos) < radius * radius)
                        {
                            // Apply the effect at the position
                            effect.component.procEffect(
                                transitionBuilder
                                    .withPosition(Vec3d(aoePos.x.toDouble(), aoePos.y.toDouble(), aoePos.z.toDouble()))
                                    .withBlockPosition(aoePos)
                                    // Random direction, AOE has no direction
                                    .withDirection(Vec3d(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize())
                                    .build(),
                                effect
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
    override fun performDefaultTransition(state: DeliveryTransitionState)
    {
        val spell = state.spell
        val spellIndex = state.stageIndex
        if (getTargetType(state.getCurrentStage().deliveryInstance!!) == TargetType.ENTITY)
        {
            // A list of nearby entities
            val entitiesWithinAABB =
                state.world.getEntitiesWithinAABB(Entity::class.java, AxisAlignedBB(BlockPos(state.position)).grow(getRadius(state.getCurrentStage().deliveryInstance!!)))

            // Go over each nearby entity
            entitiesWithinAABB.forEach()
            {
                // Perform the transition between the next delivery method and the current delivery method
                spell.getStage(spellIndex + 1)!!.deliveryInstance!!.component.executeDelivery(
                    DeliveryTransitionStateBuilder()
                        .withSpell(state.spell)
                        .withStageIndex(spellIndex + 1)
                        .withEntity(it!!)
                        .build()
                )
            }
        }
        else
        {
            val deliveryTransitionStateBuilder = DeliveryTransitionStateBuilder()
                .withSpell(state.spell)
                .withStageIndex(spellIndex + 1)
                .withWorld(state.world)
                .withBlockPosition(state.blockPosition)

            // Send out deliveries in all 6 possible directions around the hit point
            // Randomize which order the directions get applied in
            val cardinalDirections = mutableListOf(
                Vec3d(1.0, 0.0, 0.0),
                Vec3d(0.0, 1.0, 0.0),
                Vec3d(0.0, 0.0, 1.0),
                Vec3d(-1.0, 0.0, 0.0),
                Vec3d(0.0, -1.0, 0.0),
                Vec3d(0.0, 0.0, -1.0)
            ).apply { shuffle() }
                .toList()

            cardinalDirections.forEach()
            {
                // Perform the transition between the next delivery method and the current delivery method
                spell.getStage(spellIndex + 1)!!.deliveryInstance!!.component.executeDelivery(
                    deliveryTransitionStateBuilder
                        .withPosition(state.position.add(it.scale(0.2)))
                        .withDirection(it)
                        .build()
                )
            }
        }
    }

    /**
     * Gets the cost of the delivery method
     *
     * @return The cost of the delivery method
     */
    override fun getCost(instance: SpellComponentInstance<SpellDeliveryMethod>): Double
    {
        return 10 + getRadius(instance) * getRadius(instance)
    }

    /**
     * Gets the multiplier that this delivery method will apply to the stage it's in
     *
     * @return The spell stage multiplier for cost
     */
    override fun getStageCostMultiplier(instance: SpellComponentInstance<SpellDeliveryMethod>): Double
    {
        return 3.0
    }

    /**
     * Gets the radius of the delivery in blocks
     *
     * @param instance The spell delivery method instance
     * @return the radius of the delivery in blocks
     */
    fun getRadius(instance: SpellComponentInstance<SpellDeliveryMethod>): Double
    {
        return instance.data.getDouble(NBT_RADIUS)
    }

    /**
     * Gets the target type of the AOE
     *
     * @param instance The spell delivery method instance
     * @return the target type of the AOE
     */
    fun getTargetType(instance: SpellComponentInstance<SpellDeliveryMethod>): TargetType
    {
        return TargetType.values()[instance.data.getInteger(NBT_TARGET_TYPE)]
    }

    companion object
    {
        // The NBT keys
        private const val NBT_RADIUS = "radius"
        private const val NBT_TARGET_TYPE = "target_type"
    }
}