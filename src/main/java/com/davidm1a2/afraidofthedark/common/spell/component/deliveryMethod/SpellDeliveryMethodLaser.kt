package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.client.particle.AOTDParticleRegistry
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.packets.otherPackets.SyncParticle
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionStateBuilder
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import java.util.*
import kotlin.Comparator
import kotlin.math.ceil

/**
 * Laser delivery method delivers the spell to the target with a hitscan laser
 *
 * @constructor initializes the editable properties
 */
class SpellDeliveryMethodLaser : AOTDSpellDeliveryMethod(ResourceLocation(Constants.MOD_ID, "laser")) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.doubleProperty()
                .withName("Range")
                .withDescription("The range of the laser in blocks.")
                .withSetter { instance, newValue -> instance.data.setDouble(NBT_RANGE, newValue) }
                .withGetter { it.data.getDouble(NBT_RANGE) }
                .withDefaultValue(50.0)
                .withMinValue(1.0)
                .withMaxValue(300.0)
                .build()
        )
        addEditableProperty(
            SpellComponentPropertyFactory.booleanProperty()
                .withName("Hit Liquids")
                .withDescription("'True' to let liquid blocks be hit, or 'false' to go through them.")
                .withSetter { instance, newValue -> instance.data.setBoolean(NBT_HIT_LIQUIDS, newValue) }
                .withGetter { it.data.getBoolean(NBT_HIT_LIQUIDS) }
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
        // Perform a ray trace to try and find a hit point
        val world = state.world
        val entity = state.getEntity()

        // Start at entity eye height if it's from an entity
        val startPos =
            if (entity == null) state.position else state.position.addVector(0.0, entity.eyeHeight.toDouble(), 0.0)

        val direction = state.direction.normalize()
        // The end position is the start position in the right direction scaled to range
        val endPos = startPos.add(direction.scale(getRange(state.getCurrentStage().deliveryInstance!!)))

        // Perform a ray trace, this will not hit blocks
        val rayTraceResult =
            world.rayTraceBlocks(startPos, endPos, hitLiquids(state.getCurrentStage().deliveryInstance!!))

        // Compute the hit vector
        var hitPos = if (rayTraceResult == null) endPos else rayTraceResult.hitVec
        // Compute the block position we hit
        val hitBlockPos = if (rayTraceResult == null) BlockPos(hitPos) else rayTraceResult.blockPos

        // Ray tracing doesn't actually hit entities, so use this "hack" to test that. Create a bounding box that is huge and
        // has the entire possible ray inside. Then grab entities inside, then intersect each of their hitboxes manually
        val potentialHitEntities = world.getEntitiesWithinAABB(
            Entity::class.java,
            AxisAlignedBB(startPos.x, startPos.y, startPos.z, hitPos.x, hitPos.y, hitPos.z)
        )

        val hitEntity = potentialHitEntities
            // Don't hitscan ourselves
            .filter { it !== entity }
            // Ensure the entity is along the path with the ray
            .filter { it.entityBoundingBox.calculateIntercept(startPos, hitPos) != null }
            // Find the closest entity
            .minWith(Comparator<Entity>
            { entity1, entity2 ->
                entity1.getDistanceSq(BlockPos(startPos)).compareTo(entity2.getDistanceSq(BlockPos(hitPos)))
            })
        hitPos = hitEntity?.positionVector ?: hitPos

        // Compute the distance the ray traveled
        val distanceToHit = startPos.distanceTo(hitPos)
        // Spawn at least 10 particles and at most 100. Take the distance to the hit position and spawn one particle per distance
        val numParticlesToSpawn = ceil(distanceToHit).coerceIn(10.0, 100.0).toInt()

        // Compute points along the hitscan line
        val laserPositions = List<Vec3d>(numParticlesToSpawn)
        {
            startPos.add(direction.scale(it.toDouble() / numParticlesToSpawn * distanceToHit))
        }

        // Spawn laser particles
        AfraidOfTheDark.INSTANCE.packetHandler.sendToDimension(
            SyncParticle(
                AOTDParticleRegistry.ParticleTypes.SPELL_LASER,
                laserPositions,
                Collections.nCopies(numParticlesToSpawn, Vec3d.ZERO)
            ),
            state.world.provider.dimension
        )

        // Begin performing effects and transition
        val currentState = if (hitEntity != null) {
            // Apply the effect to the hit entity
            DeliveryTransitionStateBuilder()
                .withSpell(state.spell)
                .withStageIndex(state.stageIndex)
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
                .build()
        }
        procEffects(currentState)
        transitionFrom(currentState)
    }

    /**
     * Applies a given effect given the spells current state
     *
     * @param state  The state of the spell at the current delivery method
     * @param effect The effect that needs to be applied
     */
    override fun defaultEffectProc(state: DeliveryTransitionState, effect: SpellComponentInstance<SpellEffect>) {
        effect.component.procEffect(state, effect)
    }

    /**
     * Performs the default transition from this delivery method to the next
     *
     * @param state The state of the spell to transition
     */
    override fun performDefaultTransition(state: DeliveryTransitionState) {
        val spell = state.spell
        val spellIndex = state.stageIndex

        // Perform the transition between the next delivery method and the current delivery method
        spell.getStage(spellIndex + 1)!!.deliveryInstance!!.component.executeDelivery(
            DeliveryTransitionStateBuilder()
                .copyOf(state)
                .withStageIndex(spellIndex + 1)
                .build()
        )
    }

    /**
     * Gets the cost of the delivery method
     *
     * @return The cost of the delivery method
     */
    override fun getCost(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return 15 + instance.data.getDouble(NBT_RANGE) * 0.5
    }

    /**
     * Gets the multiplier that this delivery method will apply to the stage it's in
     *
     * @return The spell stage multiplier for cost
     */
    override fun getStageCostMultiplier(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return 1.2
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

    companion object {
        // The NBT keys
        private const val NBT_RANGE = "range"
        private const val NBT_HIT_LIQUIDS = "hit_liquids"
    }
}