package com.davidm1a2.afraidofthedark.common.spell.component

import com.davidm1a2.afraidofthedark.common.spell.Spell
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

/**
 * Builder class used to build a DeliveryTransitionState object
 *
 * @property spell The spell that is transitioning
 * @property stageIndex The index of the spell stage that is transitioning
 * @property world The world that the transition is happening in
 * @property position The exact double vector position the transition occurred
 * @property blockPosition A rounded for of getPosition() that estimates the block the transition occurred in
 * @property direction A normalized vector pointing in the direction the transition was in
 * @property casterEntity The entity that cast the spell
 * @property entity The entity being delivered to, can be null
 * @property deliveryEntity The entity delivering the spell (ex: projectile entity), can be null
 */
class DeliveryTransitionStateBuilder {
    private lateinit var spell: Spell
    private var stageIndex = 0
    private lateinit var world: World
    private lateinit var position: Vec3d
    private lateinit var blockPosition: BlockPos
    private lateinit var direction: Vec3d
    private var casterEntity: Entity? = null
    private var entity: Entity? = null
    private var deliveryEntity: Entity? = null

    /**
     * Sets the spell to transition for
     *
     * @param spell The spell to transition for
     * @return The builder
     */
    fun withSpell(spell: Spell): DeliveryTransitionStateBuilder {
        this.spell = spell
        return this
    }

    /**
     * Sets the spell index we're transitioning from
     *
     * @param stageIndex The spell index we're transitioning from
     * @return The builder
     */
    fun withStageIndex(stageIndex: Int): DeliveryTransitionStateBuilder {
        this.stageIndex = stageIndex
        return this
    }

    /**
     * Sets the world the transition is happening in
     *
     * @param world The world the transition is happening in
     * @return The builder
     */
    fun withWorld(world: World): DeliveryTransitionStateBuilder {
        this.world = world
        return this
    }

    /**
     * Sets the position the transition is happening at
     *
     * @param position The position the transition is happening at
     * @return The builder
     */
    fun withPosition(position: Vec3d): DeliveryTransitionStateBuilder {
        this.position = position
        return this
    }

    /**
     * Sets the block position the transition is happening at
     *
     * @param blockPosition The block position the transition is happening at
     * @return The builder
     */
    fun withBlockPosition(blockPosition: BlockPos): DeliveryTransitionStateBuilder {
        this.blockPosition = blockPosition
        return this
    }

    /**
     * Sets the direction the transition is happening towards
     *
     * @param direction The direction the transition is happening towards
     * @return The builder
     */
    fun withDirection(direction: Vec3d): DeliveryTransitionStateBuilder {
        this.direction = direction
        return this
    }

    /**
     * Sets the the entity that cast the spell
     *
     * @param casterEntity The entity that cast the spell
     * @return The builder
     */
    fun withCasterEntity(casterEntity: Entity?): DeliveryTransitionStateBuilder {
        this.casterEntity = casterEntity
        return this
    }

    /**
     * Sets the delivery entity that is used to deliver the spell
     *
     * @param deliveryEntity The entity delivering the spell
     * @return The builder
     */
    fun withDeliveryEntity(deliveryEntity: Entity?): DeliveryTransitionStateBuilder {
        this.deliveryEntity = deliveryEntity
        return this
    }

    /**
     * Sets the world, position, direction, and entity based on the entity's position
     *
     * @param entity The entity to grab data from
     * @return The builder
     */
    fun withEntity(entity: Entity): DeliveryTransitionStateBuilder {
        this.entity = entity
        world = entity.entityWorld
        position = entity.getPositionEyes(1.0f)
        blockPosition = BlockPos(position)
        direction = entity.lookVec
        return this
    }

    /**
     * Sets the state of this builder based on an existing state
     *
     * @param state The state to copy from
     * @return The builder
     */
    fun copyOf(state: DeliveryTransitionState): DeliveryTransitionStateBuilder {
        spell = state.spell
        stageIndex = state.stageIndex
        world = state.world
        position = state.position
        blockPosition = state.blockPosition
        direction = state.direction
        casterEntity = state.getCasterEntity()
        entity = state.getEntity()
        deliveryEntity = state.getDeliveryEntity()
        return this
    }

    /**
     * Finishes the state object and returns it
     *
     * @return The state object
     */
    fun build(): DeliveryTransitionState {
        return DeliveryTransitionState(
            spell,
            stageIndex,
            world,
            position,
            blockPosition,
            direction,
            casterEntity,
            entity,
            deliveryEntity
        )
    }
}