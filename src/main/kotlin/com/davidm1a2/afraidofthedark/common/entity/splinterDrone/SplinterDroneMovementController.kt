package com.davidm1a2.afraidofthedark.common.entity.splinterDrone

import net.minecraft.entity.ai.controller.MovementController
import net.minecraft.util.math.vector.Vector3d
import kotlin.math.ceil

/**
 * Move helper makes the splinter drone move like a ghast
 *
 * @constructor Sets the splinter drone entity reference
 * @param splinterDrone The splinter drone entity to move
 * @property ticksUntilNextUpdate The number of ticks until we should re-compute pathing
 */
class SplinterDroneMovementController(splinterDrone: SplinterDroneEntity) : MovementController(splinterDrone) {
    private var ticksUntilNextUpdate = 0

    /**
     * Called to update movement of the entity, code copied from the ghast
     */
    override fun tick() {
        // Test if this move helper is updating and doesn't have a target, fly around
        if (operation == Action.MOVE_TO && mob.target == null) {
            // If we should update perform a motion update
            if (ticksUntilNextUpdate-- <= 0) {
                // Update this entity again in 2 - 7 ticks
                ticksUntilNextUpdate = ticksUntilNextUpdate + mob.random.nextInt(5) + 2

                val dir = Vector3d(wantedX - mob.x, wantedY - mob.y, wantedZ - mob.z)
                val dirLength = dir.length()
                val dirNorm = dir.normalize()

                // If we can move to our position update the entity's motion to move to the position
                if (canReach(dirNorm, ceil(dirLength).toInt())) {
                    mob.deltaMovement = mob.deltaMovement.add(dirNorm.scale(speedModifier))
                } else {
                    operation = Action.WAIT
                }
            }
        }
    }

    private fun canReach(direction: Vector3d, dirMagnitude: Int): Boolean {
        // Grab the entity bounding box
        var droneBoundingBox = mob.boundingBox

        // Test if this entity can fit in each position as it moves to the position
        var i = 1
        while (i < dirMagnitude) {
            // Move the bounding box in the direction of movement
            droneBoundingBox = droneBoundingBox.move(direction)
            // Test if the entity would fit
            if (!mob.level.noCollision(mob, droneBoundingBox)) {
                // False, there is no path
                return false
            }
            i++
        }
        // True, we can make the move
        return true
    }
}