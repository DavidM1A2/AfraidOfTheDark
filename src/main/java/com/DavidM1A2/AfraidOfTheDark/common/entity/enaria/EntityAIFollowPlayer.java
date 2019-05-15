package com.DavidM1A2.afraidofthedark.common.entity.enaria;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;
import java.util.Optional;

/**
 * AI task to make an entity follow a target player
 */
public class EntityAIFollowPlayer extends EntityAIBase
{
    // Ticks inbetween pathing updates
    private static final int TICKS_BETWEEN_PATHING_UPDATES = 10;

    // The entity doing the following
    private EntityLiving entity;
    // The target player to follow
    private EntityPlayer target;
    // The range at which to stop following
    private final double minRange;
    // The max range to follow existing tracked players
    private final double maxRange;
    // The range at which to pick up new players to follow
    private final double trackRange;
    // The ticks remaining until we check path finding again
    private int ticksUntilNextUpdate = 0;

    /**
     * Constructor initializes final constants
     *
     * @param entity     The entity that is following the player
     * @param minRange   The minimum range to be at from the player
     * @param maxRange   The maximum range to follow the player
     * @param trackRange The range to pick up new players to follow
     */
    public EntityAIFollowPlayer(EntityLiving entity, double minRange, double maxRange, double trackRange)
    {
        this.entity = entity;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.trackRange = trackRange;
    }

    /**
     * @return True if the following should execute, false otherwise
     */
    @Override
    public boolean shouldExecute()
    {
        // Grab a list of nearby players
        List<EntityPlayer> players = this.entity.world.getEntitiesWithinAABB(EntityPlayer.class, this.entity.getEntityBoundingBox().grow(this.trackRange));
        // Grab the closest player
        Optional<EntityPlayer> closestPlayer = players.stream()
                .filter(entityPlayer -> !entityPlayer.capabilities.isCreativeMode)
                .min((p1, p2) -> Float.compare(p1.getDistance(this.entity), p2.getDistance(this.entity)));
        // If there is no closest player return false
        if (!closestPlayer.isPresent())
        {
            return false;
        }
        // If the distance to the player is less than min don't walk towards the player
        double distance = closestPlayer.get().getDistance(this.entity);
        if (distance < minRange)
        {
            return false;
        }
        // Otherwise the player is our target, return true
        else
        {
            this.target = closestPlayer.get();
            return true;
        }
    }

    /**
     * Resets the task and clears the existing path
     */
    @Override
    public void resetTask()
    {
        // Clear the path, clear the target, and reset the update tick counter
        this.entity.getNavigator().clearPath();
        this.target = null;
        this.ticksUntilNextUpdate = 0;
    }

    /**
     * @return True if the pathing should continue to execute, false otherwise
     */
    @Override
    public boolean shouldContinueExecuting()
    {
        // If the target is dead or in creative don't execute
        if (!this.target.isEntityAlive() || this.target.capabilities.isCreativeMode)
        {
            return false;
        }
        // Otherwise ensure the entity hasn't run away
        else
        {
            double distance = this.entity.getDistance(this.target);
            return distance >= this.minRange && distance <= this.maxRange;
        }
    }

    /**
     * Updates the entity pathing
     */
    @Override
    public void updateTask()
    {
        // Only update the task once every 10 ticks
        if (this.ticksUntilNextUpdate-- <= 0)
        {
            this.ticksUntilNextUpdate = TICKS_BETWEEN_PATHING_UPDATES;
            // Move to the player to the entity
            this.entity.getNavigator().tryMoveToEntityLiving(this.target, this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
        }
    }
}
