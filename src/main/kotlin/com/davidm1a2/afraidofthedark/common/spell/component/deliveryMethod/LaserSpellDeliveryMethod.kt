package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.entity.spell.laser.SpellLaserEntity
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionStateBuilder
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.RayTraceContext
import java.awt.Color

/**
 * Laser delivery method delivers the spell to the target with a hitscan laser
 *
 * @constructor initializes the editable properties
 */
class LaserSpellDeliveryMethod : AOTDSpellDeliveryMethod(ResourceLocation(Constants.MOD_ID, "laser")) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.doubleProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("range"))
                .withSetter { instance, newValue -> instance.data.putDouble(NBT_RANGE, newValue) }
                .withGetter { it.data.getDouble(NBT_RANGE) }
                .withDefaultValue(50.0)
                .withMinValue(1.0)
                .withMaxValue(300.0)
                .build()
        )
        addEditableProperty(
            SpellComponentPropertyFactory.booleanProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("hit_liquids"))
                .withSetter { instance, newValue -> instance.data.putBoolean(NBT_HIT_LIQUIDS, newValue) }
                .withGetter { it.data.getBoolean(NBT_HIT_LIQUIDS) }
                .withDefaultValue(false)
                .build()
        )
        addEditableProperty(
            SpellComponentPropertyFactory.colorProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("color"))
                .withSetter(this::setColor)
                .withGetter(this::getColor)
                .withDefaultValue(Color.RED)
                .build()
        )
    }

    /**
     * Called to deliver the effects to the target by whatever means necessary
     *
     * @param state The state of the spell to deliver
     */
    override fun executeDelivery(state: DeliveryTransitionState) {
        // Perform a ray trace to try and find a hit point
        val world = state.world
        val entity = state.getEntity()

        // Start at entity eye height if it's from an entity
        val startPos = entity?.getEyePosition(1.0f) ?: state.position

        val direction = state.direction.normalize()
        // The end position is the start position in the right direction scaled to range
        val endPos = startPos.add(direction.scale(getRange(state.getCurrentStage().deliveryInstance!!)))

        // Perform a ray trace, this will not hit blocks
        val hitLiquids = if (hitLiquids(state.getCurrentStage().deliveryInstance!!)) RayTraceContext.FluidMode.ANY else RayTraceContext.FluidMode.NONE
        val rayTraceResult = world.clip(RayTraceContext(startPos, endPos, RayTraceContext.BlockMode.COLLIDER, hitLiquids, entity))

        // Compute the hit vector
        var hitPos = rayTraceResult.location
        // Compute the block position we hit
        val hitBlockPos = rayTraceResult.blockPos

        // Ray tracing doesn't actually hit entities, so use this "hack" to test that. Create a bounding box that is huge and
        // has the entire possible ray inside. Then grab entities inside, then intersect each of their hitboxes manually
        val potentialHitEntities = world.getEntitiesOfClass(
            Entity::class.java,
            AxisAlignedBB(startPos.x, startPos.y, startPos.z, hitPos.x, hitPos.y, hitPos.z)
        )

        val hitEntity = potentialHitEntities
            // Don't laser ourselves
            .filter { it !== entity }
            // Ensure the entity is along the path with the ray
            .filter { it.boundingBox.clip(startPos, endPos).isPresent }
            // Find the closest entity
            .minWithOrNull(Comparator.comparing { it.distanceToSqr(startPos) })
        hitPos = hitEntity?.position() ?: hitPos

        // The entity contains no logic, it's just for rendering a beam
        world.addFreshEntity(SpellLaserEntity(world, startPos, hitPos, getColor(state.getCurrentStage().deliveryInstance!!)))

        // Begin performing effects and transition
        val currentState = if (hitEntity != null) {
            // Apply the effect to the hit entity
            DeliveryTransitionStateBuilder()
                .withSpell(state.spell)
                .withStageIndex(state.stageIndex)
                .withCasterEntity(state.getCasterEntity())
                .withEntity(hitEntity)
                .build()
        } else {
            // Apply the effect at the hit position
            DeliveryTransitionStateBuilder()
                .withSpell(state.spell)
                .withStageIndex(state.stageIndex)
                .withWorld(state.world)
                .withPosition(hitPos)
                .withBlockPosition(hitBlockPos)
                .withDirection(hitPos.subtract(startPos).normalize())
                .withCasterEntity(state.getCasterEntity())
                .build()
        }
        procEffects(currentState)
        transitionFrom(currentState)
    }

    /**
     * Gets the cost of the delivery method
     *
     * @return The cost of the delivery method
     */
    override fun getCost(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return 10 + instance.data.getDouble(NBT_RANGE) / 10.0
    }

    /**
     * Gets the multiplier that this delivery method will apply to the stage it's in
     *
     * @return The spell stage multiplier for cost
     */
    override fun getStageCostMultiplier(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return 2.0
    }

    fun setRange(instance: SpellComponentInstance<SpellDeliveryMethod>, range: Double) {
        instance.data.putDouble(NBT_RANGE, range)
    }

    /**
     * Gets the hitscan range from the NBT data
     *
     * @param instance The spell delivery method instance
     * @return the hitscan range
     */
    fun getRange(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return instance.data.getDouble(NBT_RANGE)
    }

    /**
     * True if liquids can be hit, false otherwise
     *
     * @param instance The spell delivery method instance
     * @return True if liquids can be hit, false otherwise
     */
    fun hitLiquids(instance: SpellComponentInstance<SpellDeliveryMethod>): Boolean {
        return instance.data.getBoolean(NBT_HIT_LIQUIDS)
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
        private const val NBT_RANGE = "range"
        private const val NBT_HIT_LIQUIDS = "hit_liquids"
        private const val NBT_COLOR = "color"
    }
}