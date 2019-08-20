package com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base;

import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import net.minecraft.entity.Entity;
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
    private final Vec3d direction;
    private final Entity entity;

    /**
     * Constructor initializes all final fields
     *
     * @param spell      The spell being transitioned
     * @param stageIndex The current stage that finished firing. The next stage to fire is stageIndex + 1
     * @param world      The world that the spell is transitioning in
     * @param position   The position the transition is happening at
     * @param direction  The direction the transition is toward
     * @param entity     The entity being transitioned through
     */
    DeliveryTransitionState(Spell spell, int stageIndex, World world, Vec3d position, Vec3d direction, Entity entity)
    {
        this.spell = spell;
        this.stageIndex = stageIndex;
        this.world = world;
        this.position = position;
        this.direction = direction;
        this.entity = entity;
    }

    ///
    /// Getters
    ///

    public Spell getSpell()
    {
        return spell;
    }

    public int getStageIndex()
    {
        return stageIndex;
    }

    public World getWorld()
    {
        return world;
    }

    public Vec3d getPosition()
    {
        return position;
    }

    public Vec3d getDirection()
    {
        return direction;
    }

    public Entity getEntity()
    {
        return entity;
    }
}
