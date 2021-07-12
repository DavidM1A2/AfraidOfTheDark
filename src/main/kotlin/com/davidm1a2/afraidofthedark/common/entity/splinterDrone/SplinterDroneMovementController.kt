package com.davidm1a2.afraidofthedark.common.entity.splinterDrone

import net.minecraft.entity.ai.controller.MovementController
import net.minecraft.util.math.Vec3d
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
        if (action == Action.MOVE_TO && mob.attackTarget == null) {
            // If we should update perform a motion update
            if (ticksUntilNextUpdate-- <= 0) {
                // Update this entity again in 2 - 7 ticks
                ticksUntilNextUpdate = ticksUntilNextUpdate + mob.rng.nextInt(5) + 2

                val dir = Vec3d(posX - mob.posX, posY - mob.posY, posZ - mob.posZ)
                val dirLength = dir.length()
                val dirNorm = dir.normalize()

                // If we can move to our position update the entity's motion to move to the position
                if (isNotColliding(dirNorm, ceil(dirLength).toInt())) {
                    mob.motion = mob.motion.add(dirNorm.scale(speed))
                } else {
                    action = Action.WAIT
                }
            }
        }
    }

    private fun isNotColliding(direction: Vec3d, dirMagnitude: Int): Boolean {
        // Grab the entity bounding box
        var droneBoundingBox = mob.boundingBox

        // Test if this entity can fit in each position as it moves to the position
        var i = 1
        while (i < dirMagnitude) {
            // Move the bounding box in the direction of movement
            droneBoundingBox = droneBoundingBox.offset(direction)
            // Test if the entity would fit
            if (!mob.world.hasNoCollisions(mob, droneBoundingBox)) {
                // False, there is no path
                return false
            }
            i++
        }
        // True, we can make the move
        return true
    }
}