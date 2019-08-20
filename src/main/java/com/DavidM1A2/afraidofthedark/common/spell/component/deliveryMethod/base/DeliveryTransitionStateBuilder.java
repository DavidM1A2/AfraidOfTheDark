package com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base;

import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.Validate;

/**
 * Builder class used to build a DeliveryTransitionState object
 */
public class DeliveryTransitionStateBuilder
{
    private Spell spell = null;
    private int stageIndex = 0;
    private World world = null;
    private Vec3d position = null;
    private Vec3d direction = null;
    private Entity entity = null;

    /**
     * Sets the spell to transition for
     *
     * @param spell The spell to transition for
     * @return The builder
     */
    public DeliveryTransitionStateBuilder withSpell(Spell spell)
    {
        this.spell = spell;
        return this;
    }

    /**
     * Sets the spell index we're transitioning from
     *
     * @param stageIndex The spell index we're transitioning from
     * @return The builder
     */
    public DeliveryTransitionStateBuilder withStageIndex(int stageIndex)
    {
        this.stageIndex = stageIndex;
        return this;
    }

    /**
     * Sets the world the transition is happening in
     *
     * @param world The world the transition is happening in
     * @return The builder
     */
    public DeliveryTransitionStateBuilder withWorld(World world)
    {
        this.world = world;
        return this;
    }

    /**
     * Sets the position the transition is happening at
     *
     * @param position The position the transition is happening at
     * @return The builder
     */
    public DeliveryTransitionStateBuilder withPosition(Vec3d position)
    {
        this.position = position;
        return this;
    }

    /**
     * Sets the direction the transition is happening towards
     *
     * @param direction The direction the transition is happening towards
     * @return The builder
     */
    public DeliveryTransitionStateBuilder withDirection(Vec3d direction)
    {
        this.direction = direction;
        return this;
    }

    /**
     * Sets the world, position, direction, and entity based on the entity's position
     *
     * @param entity The entity to grab data from
     * @return The builder
     */
    public DeliveryTransitionStateBuilder withEntity(Entity entity)
    {
        this.entity = entity;
        this.world = entity.getEntityWorld();
        this.position = entity.getPositionVector();
        this.direction = entity.getLookVec();
        return this;
    }

    /**
     * Sets the state of this builder based on an existing state
     *
     * @param state The state to copy from
     * @return The builder
     */
    public DeliveryTransitionStateBuilder copyOf(DeliveryTransitionState state)
    {
        this.spell = state.getSpell();
        this.stageIndex = state.getStageIndex();
        this.world = state.getWorld();
        this.position = state.getPosition();
        this.direction = state.getDirection();
        this.entity = state.getEntity();
        return this;
    }

    /**
     * Finishes the state object and returns it
     *
     * @return The state object
     */
    public DeliveryTransitionState build()
    {
        Validate.notNull(spell, "Spell must not be null!");
        Validate.notNull(world, "World must not be null!");
        Validate.notNull(position, "Position must not be null!");

        return new DeliveryTransitionState(spell, stageIndex, world, position, direction, entity);
    }
}
