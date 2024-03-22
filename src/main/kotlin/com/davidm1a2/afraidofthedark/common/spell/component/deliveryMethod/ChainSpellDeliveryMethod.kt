package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.entity.spell.SpellChainEntity
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import com.davidm1a2.afraidofthedark.common.utility.getLookNormal
import com.davidm1a2.afraidofthedark.common.utility.getNormal
import net.minecraft.world.entity.Entity
import net.minecraft.entity.EntityPredicate
import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.RayTraceContext
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.level.Level

class ChainSpellDeliveryMethod : AOTDSpellDeliveryMethod("chain", ModResearches.APPRENTICE_ASCENDED) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.doubleProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("max_distance"))
                .withMinValue(0.0)
                .withDefaultValue(5.0)
                .withMaxValue(20.0)
                .withSetter(this::setMaxDistance)
                .withGetter(this::getMaxDistance)
                .build()
        )
        addEditableProperty(
            SpellComponentPropertyFactory.intProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("max_hops"))
                .withMinValue(1)
                .withDefaultValue(3)
                .withMaxValue(25)
                .withSetter(this::setMaxHops)
                .withGetter(this::getMaxHops)
                .build()
        )
    }

    override fun executeDelivery(state: DeliveryTransitionState) {
        val instance = state.getCurrentStage().deliveryInstance!!
        val maxDistance = getMaxDistance(instance)
        val maxHops = getMaxHops(instance)

        recursivelyChain(state.spell, state.stageIndex, state.casterEntity, state.world, state.position, state.position, maxHops, maxDistance)
    }

    private fun recursivelyChain(
        spell: Spell,
        stageIndex: Int,
        casterEntity: Entity?,
        world: Level,
        position: Vector3d,
        lastHitCenterPos: Vector3d,
        hopsRemaining: Int,
        maxRange: Double,
        hitEntities: MutableSet<LivingEntity> = mutableSetOf()
    ) {
        // Recursive base case
        if (hopsRemaining <= 0) {
            return
        }

        val predicate = EntityPredicate()
            .allowInvulnerable()
            .allowSameTeam()
            .allowNonAttackable()
            .range(maxRange)
            .selector { it !in hitEntities && it.canSee(position) }

        val nearestEntity = world.getNearestEntity(
            LivingEntity::class.java,
            predicate,
            null,
            position.x,
            position.y,
            position.z,
            AxisAlignedBB(position.x - maxRange, position.y - maxRange, position.z - maxRange, position.x + maxRange, position.y + maxRange, position.z + maxRange)
        )

        // No entity to chain to, we're done
        if (nearestEntity == null) {
            return
        }

        // Proc the effects and transition from the entity
        val direction = nearestEntity.position().subtract(position).normalize()
        val transitionState = DeliveryTransitionState(
            spell = spell,
            stageIndex = stageIndex,
            world = world,
            position = nearestEntity.position(),
            blockPosition = nearestEntity.blockPosition(),
            direction = direction,
            normal = direction.getNormal(),
            casterEntity = casterEntity,
            entity = nearestEntity
        )
        procEffects(transitionState)
        transitionFrom(
            transitionState.copy(
                direction = nearestEntity.lookAngle,
                normal = nearestEntity.getLookNormal()
            )
        )

        val nearestEntityCenterPos = nearestEntity.position().add(0.0, nearestEntity.bbHeight / 2.0, 0.0)
        world.addFreshEntity(SpellChainEntity(world, lastHitCenterPos, nearestEntityCenterPos))

        hitEntities.add(nearestEntity)
        recursivelyChain(spell, stageIndex, casterEntity, world, nearestEntity.position(), nearestEntityCenterPos, hopsRemaining - 1, maxRange, hitEntities)
    }

    // Copy & Pasted from LivingEntity::canSee(Entity), except uses a position instead of an entity
    private fun LivingEntity.canSee(position: Vector3d): Boolean {
        val startPos = Vector3d(this.x, this.eyeY, this.z)
        return if (position.distanceToSqr(startPos) > 128.0 * 128.0) {
            false
        } else {
            level.clip(
                RayTraceContext(
                    startPos,
                    position,
                    RayTraceContext.BlockMode.COLLIDER,
                    RayTraceContext.FluidMode.NONE,
                    this
                )
            ).type == RayTraceResult.Type.MISS //Forge Backport MC-209819
        }
    }

    override fun getDeliveryCost(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        // Each hop adds 1.5 vitae
        val hopsCostMultiplier = getMaxHops(instance) * 1.5
        // 10 blocks per vitae
        val distanceCost = getMaxDistance(instance) * 0.1
        return hopsCostMultiplier * distanceCost
    }

    override fun getMultiplicity(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return getMaxHops(instance).toDouble()
    }

    fun setMaxDistance(instance: SpellComponentInstance<*>, maxDistance: Double) {
        instance.data.putDouble(NBT_MAX_DISTANCE, maxDistance)
    }

    fun getMaxDistance(instance: SpellComponentInstance<*>): Double {
        return instance.data.getDouble(NBT_MAX_DISTANCE)
    }

    fun setMaxHops(instance: SpellComponentInstance<*>, maxHops: Int) {
        instance.data.putInt(NBT_MAX_HOPS, maxHops)
    }

    fun getMaxHops(instance: SpellComponentInstance<*>): Int {
        return instance.data.getInt(NBT_MAX_HOPS)
    }

    companion object {
        // The NBT keys
        private const val NBT_MAX_DISTANCE = "max_distance"
        private const val NBT_MAX_HOPS = "max_hops"
    }
}