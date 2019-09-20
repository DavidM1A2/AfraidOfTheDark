package com.davidm1a2.afraidofthedark.common.entity.werewolf;

import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Target location for werewolves, this will ignore players that have not started AOTD based on a flag
 */
public class CustomWerewolfTargetLocator extends EntityAITarget
{
    // The chance that the AI begins performing the target task
    private final int targetChance;
    // A sorter used to sort entities by distance to the center entity
    private final Sorter sorter;
    // The selector for target entities
    private final Predicate<EntityPlayer> targetEntitySelector;
    // The entity that is currently targeted by the werewolf
    private EntityLivingBase targetEntity;

    /**
     * Constructor sets up fields with a creature to locate targets for, a target chance, and a flag telling it if sight is important
     *
     * @param entityCreature   The werewolf entity locate targets for
     * @param targetChance     The chance that the werewolf targets, higher numbers mean less frequent targeting
     * @param shouldCheckSight True if the werewolf needs LOS to attack, false otherwise
     */
    CustomWerewolfTargetLocator(EntityCreature entityCreature, int targetChance, boolean shouldCheckSight)
    {
        // Call the superclass's constructor.
        super(entityCreature, shouldCheckSight, false);
        // Assign our final fields
        this.targetChance = targetChance;
        this.sorter = new Sorter(entityCreature);
        // Required for a target locator, not sure what it does exactly
        this.setMutexBits(1);
        // Create a target predicate which tells us if an entity is valid or not for selection
        this.targetEntitySelector = entityPlayer ->
        {
            // Grab the range at which the werewolf can follow targets
            double followRange = CustomWerewolfTargetLocator.this.getTargetDistance();
            // If the player is sneaking reduce the follow range
            if (entityPlayer.isSneaking())
            {
                followRange = followRange * 0.8;
            }
            // Of the player is invis reduce the follow range based on if the player has armor or not
            if (entityPlayer.isInvisible())
            {
                float visibility = entityPlayer.getArmorVisibility();
                if (visibility < 0.1)
                {
                    visibility = 0.1f;
                }
                followRange = followRange * visibility * 0.7f;
            }
            // If the player is to far to follow dont do anything
            if (entityPlayer.getDistance(CustomWerewolfTargetLocator.this.taskOwner) > followRange)
            {
                return false;
            }
            // Test if the player is suitable to be targeted
            return CustomWerewolfTargetLocator.this.isSuitableTarget(entityPlayer, false);
        };
    }

    /**
     * True if the AI task should execute or false otherwise
     *
     * @return True if the task should execute, false otherwise
     */
    @Override
    public boolean shouldExecute()
    {
        // If we roll a 0 execute the task, otherwise don't
        if (targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0)
        {
            return false;
        }
        else
        {
            // Grab the follow range of the locator
            double followRange = this.getTargetDistance();
            // Grab a list of nearby players
            List<EntityPlayer> nearbyPlayers = this.taskOwner.world.getEntitiesWithinAABB(EntityPlayer.class, this.taskOwner.getEntityBoundingBox().grow(followRange, 4.0D, followRange), this.targetEntitySelector::test);
            // Sort the list using our player comparator
            nearbyPlayers.sort(this.sorter);
            // Iterate over all players nearby and pick a valid target
            for (EntityPlayer entityPlayer : nearbyPlayers)
                if (entityPlayer.getCapability(ModCapabilities.PLAYER_BASICS, null).getStartedAOTD() || ((EntityWerewolf) this.taskOwner).canAttackAnyone())
                {
                    this.targetEntity = entityPlayer;
                    return true;
                }
            return false;
        }
    }

    /**
     * Starts executing the target task
     */
    @Override
    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.targetEntity);
        super.startExecuting();
    }

    /**
     * Sorter class that will be used to sort nearby entities by distance to the entity
     */
    private static class Sorter implements Comparator<Entity>
    {
        // The entity in the center
        private final Entity centerEntity;

        /**
         * Constructor takes the entity to take as the center as input
         *
         * @param centerEntity The central entity
         */
        public Sorter(Entity centerEntity)
        {
            this.centerEntity = centerEntity;
        }

        /**
         * Returns a negative number if entity1 is closer to the center entity than entity2, a positive number if it's the other way around, and 0 if the two
         * entities are the same distance
         *
         * @param entity1 The first entity
         * @param entity2 The second entity
         * @return The comparison of both entities to the center entity
         */
        @Override
        public int compare(Entity entity1, Entity entity2)
        {
            double distance1 = this.centerEntity.getDistanceSq(entity1);
            double distance2 = this.centerEntity.getDistanceSq(entity2);
            return Double.compare(distance1, distance2);
        }
    }
}
