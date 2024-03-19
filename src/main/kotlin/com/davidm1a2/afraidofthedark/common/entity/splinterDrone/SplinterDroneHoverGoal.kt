package com.davidm1a2.afraidofthedark.common.entity.splinterDrone

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.Goal
import java.util.*
import kotlin.math.floor
import kotlin.math.sqrt

/**
 * AI task that makes the splinter drone randomly fly around like a ghast. Code from EntityGhast
 *
 * @constructor Initializes the entity
 * @property splinterDrone The splinter drone entity that is flying around
 */
class SplinterDroneHoverGoal(private val splinterDrone: SplinterDroneEntity) : Goal() {
    init {
        flags = EnumSet.of(Flag.MOVE)
    }

    /**
     * @return True if the ai task should be executed, false otherwise
     */
    override fun canUse(): Boolean {
        // The move helper to move the splinter drone
        val entityMoveHelper = splinterDrone.moveControl

        // Randomly fly if the move helper isn't updating and the drone doesn't have a target
        return if (!entityMoveHelper.hasWanted() && splinterDrone.target == null) {
            true
        } else {
            // Compute the distance from the move position to the drone, if it's more than 60 or less than 1 then
            // execute, otherwise don't
            val xDistance = entityMoveHelper.wantedX - splinterDrone.x
            val yDistance = entityMoveHelper.wantedY - splinterDrone.y
            val zDistance = entityMoveHelper.wantedZ - splinterDrone.z
            val overallDistance = sqrt(xDistance * xDistance + yDistance * yDistance + zDistance * zDistance)
            overallDistance < 1 || overallDistance > 60
        }
    }

    /**
     * @return False, this task only executes when the splinter drone needs a new position to go to
     */
    override fun canContinueToUse(): Boolean {
        return false
    }

    /**
     * Executes the task and finds a new place to go
     */
    override fun start() {
        // Get the random object off of the splinter drone
        val random = splinterDrone.random

        // Compute random x and z positions to go to, also ensure the splinter drone stays y=3 blocks above the ground
        val xToGoTo = splinterDrone.x + (random.nextFloat() * 2.0f - 1.0f) * 16.0f
        val zToGoTo = splinterDrone.z + (random.nextFloat() * 2.0f - 1.0f) * 16.0f
        val yToGoTo = getHeightToMoveTo(xToGoTo, zToGoTo)

        // Move the the x,y,z position
        splinterDrone.moveControl.setWantedPosition(
            xToGoTo,
            yToGoTo,
            zToGoTo,
            splinterDrone.getAttribute(Attributes.MOVEMENT_SPEED)!!.value
        )
    }

    /**
     * Gets the y value 3 blocks above the ground at x,z
     *
     * @param x The x position to get the y coordinate from
     * @param z The z position to get the y coordinate from
     * @return The y position to move to
     */
    private fun getHeightToMoveTo(x: Double, z: Double): Double {
        // Go from the splinter drone's y position downwards until we find a solid block
        for (y in floor(splinterDrone.y).toInt() downTo 1) {
            val pos = BlockPos(x, y.toDouble(), z)
            // If the block is non-air (so solid) move 3 blocks above it
            if (!splinterDrone.level.isEmptyBlock(pos)) {
                return (y + 3.0).coerceIn(0.0, 255.0)
            }
        }

        // If we can't find a position just stay at the same height
        return splinterDrone.y
    }
}