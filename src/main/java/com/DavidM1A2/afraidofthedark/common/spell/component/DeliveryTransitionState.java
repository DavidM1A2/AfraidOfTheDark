package com.DavidM1A2.afraidofthedark.common.spell.component;

import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import com.DavidM1A2.afraidofthedark.common.spell.SpellStage;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Class representing a spell in the transition state. This object is immutable
 */
public class DeliveryTransitionState
{
    private final Spell spell;
    private final int stageIndex;
    private final World world;
    private final Vec3d position;
    private final BlockPos blockPos;
    private final Vec3d direction;
    private final Entity entity;
    private final Entity deliveryEntity;

    /**
     * Constructor initializes all final fields
     *
     * @param spell          The spell being transitioned
     * @param stageIndex     The current stage that finished firing. The next stage to fire is stageIndex + 1
     * @param world          The world that the spell is transitioning in
     * @param position       The position the transition is happening at
     * @param direction      The direction the transition is toward
     * @param entity         The entity being transitioned through
     * @param deliveryEntity The entity that caused the transition
     */
    DeliveryTransitionState(Spell spell, int stageIndex, World world, Vec3d position, BlockPos blockPos, Vec3d direction, Entity entity, Entity deliveryEntity)
    {
        this.spell = spell;
        this.stageIndex = stageIndex;
        this.world = world;
        this.position = position;
        this.blockPos = blockPos;
        this.direction = direction;
        this.entity = entity;
        this.deliveryEntity = deliveryEntity;
    }

    /**
     * Utility method to get the current spell stage from the spell and index
     *
     * @return The current spell stage
     */
    public SpellStage getCurrentStage()
    {
        return this.getSpell().getStage(this.getStageIndex());
    }

    /**
     * @return The spell that is transitioning
     */
    public Spell getSpell()
    {
        return spell;
    }

    /**
     * @return The index of the spell stage that is transitioning
     */
    public int getStageIndex()
    {
        return stageIndex;
    }

    /**
     * @return The world that the transition is happening in
     */
    public World getWorld()
    {
        return world;
    }

    /**
     * @return The exact double vector position the transition occurred
     */
    public Vec3d getPosition()
    {
        return position;
    }

    /**
     * @return A rounded for of getPosition() that estimates the block the transition occurred in
     */
    public BlockPos getBlockPosition()
    {
        return blockPos;
    }

    /**
     * @return A normalized vector pointing in the direction the transition was in
     */
    public Vec3d getDirection()
    {
        return direction;
    }

    /**
     * @return The entity that this state was transitioning through
     */
    public Entity getEntity()
    {
        return entity;
    }

    /**
     * @return The entity that performed the delivery and transition
     */
    public Entity getDeliveryEntity()
    {
        return deliveryEntity;
    }
}
