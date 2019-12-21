package com.davidm1a2.afraidofthedark.common.entity.werewolf

import com.davidm1a2.afraidofthedark.common.capabilities.getBasics
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityCreature
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.ai.EntityAITarget
import net.minecraft.entity.player.EntityPlayer
import java.util.*

/**
 * Target location for werewolves, this will ignore players that have not started AOTD based on a flag
 *
 * @param entityCreature The creature we are locating targets for
 * @param shouldCheckSight True if creature has to see the target to track it
 * @property targetChance The chance that the AI begins performing the target task
 * @property sorter A sorter used to sort entities by distance to the center entity
 * @property targetEntitySelector The selector for target entities
 * @property targetEntity The entity that is currently targeted by the werewolf
 */
class CustomWerewolfTargetLocator internal constructor(
    entityCreature: EntityCreature,
    shouldCheckSight: Boolean,
    private val targetChance: Int
) : EntityAITarget(entityCreature, shouldCheckSight, false)
{
    private val sorter: Sorter
    private val targetEntitySelector: (EntityPlayer) -> Boolean
    private var targetEntity: EntityLivingBase? = null

    init
    {
        sorter = Sorter(entityCreature)

        // Required for a target locator, not sure what it does exactly
        mutexBits = 1

        // Create a target predicate which tells us if an entity is valid or not for selection
        targetEntitySelector =
            {
                // Grab the range at which the werewolf can follow targets
                var followRange = this@CustomWerewolfTargetLocator.targetDistance

                // If the player is sneaking reduce the follow range
                if (it.isSneaking)
                {
                    followRange = followRange * 0.8
                }

                // Of the player is invis reduce the follow range based on if the player has armor or not
                if (it.isInvisible)
                {
                    var visibility = it.armorVisibility
                    if (visibility < 0.1)
                    {
                        visibility = 0.1f
                    }
                    followRange = followRange * visibility * 0.7f
                }

                // If the player is to far to follow dont do anything
                if (it.getDistance(taskOwner) > followRange)
                {
                    false
                }
                else
                {
                    isSuitableTarget(it, false)
                }
            }
    }

    /**
     * True if the AI task should execute or false otherwise
     *
     * @return True if the task should execute, false otherwise
     */
    override fun shouldExecute(): Boolean
    {
        // If we roll a 0 execute the task, otherwise don't
        return if (targetChance > 0 && taskOwner.rng.nextInt(targetChance) != 0)
        {
            false
        }
        else
        {
            // Grab the follow range of the locator
            val followRange = this.targetDistance

            // Grab a list of nearby players sorted by distance
            val nearbyPlayers = taskOwner.world.getEntitiesWithinAABB(EntityPlayer::class.java, taskOwner.entityBoundingBox.grow(followRange, 4.0, followRange))
                .filter { targetEntitySelector(it) }
                .sortedWith(sorter)

            // Iterate over all players nearby and pick a valid target
            for (entityPlayer in nearbyPlayers)
            {
                if (entityPlayer.getBasics().startedAOTD || (taskOwner as EntityWerewolf).canAttackAnyone())
                {
                    targetEntity = entityPlayer
                    return true
                }
            }
            false
        }
    }

    /**
     * Starts executing the target task
     */
    override fun startExecuting()
    {
        taskOwner.attackTarget = targetEntity
        super.startExecuting()
    }

    /**
     * Sorter class that will be used to sort nearby entities by distance to the entity
     *
     * @constructor takes the entity to take as the center as input
     * @property centerEntity The entity in the center
     */
    private class Sorter(private val centerEntity: Entity) : Comparator<Entity>
    {
        /**
         * Returns a negative number if entity1 is closer to the center entity than entity2, a positive number if it's the other way around, and 0 if the two
         * entities are the same distance
         *
         * @param entity1 The first entity
         * @param entity2 The second entity
         * @return The comparison of both entities to the center entity
         */
        override fun compare(entity1: Entity, entity2: Entity): Int
        {
            val distance1 = centerEntity.getDistanceSq(entity1)
            val distance2 = centerEntity.getDistanceSq(entity2)
            return distance1.compareTo(distance2)
        }
    }
}