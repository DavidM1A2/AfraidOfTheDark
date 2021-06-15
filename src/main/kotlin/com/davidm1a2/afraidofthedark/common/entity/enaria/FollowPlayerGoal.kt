package com.davidm1a2.afraidofthedark.common.entity.enaria

import net.minecraft.entity.MobEntity
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.player.PlayerEntity

/**
 * AI task to make an entity follow a target player
 *
 * @constructor initializes final constants
 * @property entity     The entity that is following the player
 * @property minRange   The minimum range to be at from the player
 * @property maxRange   The maximum range to follow the player
 * @property trackRange The range to pick up new players to follow
 * @property target The target player to follow
 * @property ticksUntilNextUpdate The ticks remaining until we check path finding again
 */
class FollowPlayerGoal(
    private val entity: MobEntity,
    private val minRange: Double,
    private val maxRange: Double,
    private val trackRange: Double
) : Goal() {
    private var target: PlayerEntity? = null
    private var ticksUntilNextUpdate = 0

    /**
     * @return True if the following should execute, false otherwise
     */
    override fun shouldExecute(): Boolean {
        // Grab a list of nearby players
        val players =
            entity.world.getEntitiesWithinAABB(PlayerEntity::class.java, entity.boundingBox.grow(trackRange))

        // Grab the closest player, if there is no closest player return false
        val closestPlayer = players.minWithOrNull { p1, p2 -> p1.getDistance(entity).compareTo(p2.getDistance(entity)) } ?: return false

        // If the distance to the player is less than min don't walk towards the player
        val distance = closestPlayer.getDistance(entity).toDouble()
        return if (distance < minRange) {
            false
        } else {
            target = closestPlayer
            true
        }
    }

    /**
     * Resets the task and clears the existing path
     */
    override fun resetTask() {
        // Clear the path, clear the target, and reset the update tick counter
        entity.navigator.clearPath()
        target = null
        ticksUntilNextUpdate = 0
    }

    /**
     * @return True if the pathing should continue to execute, false otherwise
     */
    override fun shouldContinueExecuting(): Boolean {
        // If the target is dead
        return if (!target!!.isAlive) {
            false
        } else {
            val distance = entity.getDistance(target!!).toDouble()
            distance in minRange..maxRange
        }
    }

    /**
     * Updates the entity pathing
     */
    override fun tick() {
        // Only update the task once every 10 ticks
        if (ticksUntilNextUpdate-- <= 0) {
            ticksUntilNextUpdate = TICKS_BETWEEN_PATHING_UPDATES
            // Move to the player to the entity
            entity.navigator.tryMoveToEntityLiving(
                target!!,
                entity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).value
            )
        }
    }

    companion object {
        // Ticks inbetween pathing updates
        private const val TICKS_BETWEEN_PATHING_UPDATES = 10
    }
}