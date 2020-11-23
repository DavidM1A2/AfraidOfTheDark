package com.davidm1a2.afraidofthedark.common.entity.enaria

import net.minecraft.entity.ai.controller.MovementController
import net.minecraft.util.math.Vec3d
import kotlin.math.sqrt

/**
 * Utility class to allow enaria to path through walls
 *
 * @param enaria the enaria reference by calling super
 */
class GhastlyEnariaMovementController(enaria: GhastlyEnariaEntity) : MovementController(enaria) {
    /**
     * Gets called when the move helper should update and move the entity
     */
    override fun tick() {
        // Only update if updating is true
        if (this.isUpdating) {
            // Code copied from super, moves the entity in the direction enaria wants to move to
            action = Action.WAIT

            // Compute the distance between the entities current position and the position the entity wants to be in
            val xDifference = posX - mob.posX
            val yDifference = posY - mob.posY
            val zDifference = posZ - mob.posZ

            // Compute the total difference in all distances
            val totalDifference =
                sqrt(xDifference * xDifference + yDifference * yDifference + zDifference * zDifference)

            // If we haven't gotten to the final position update the entities motion to be in the correct direction
            if (totalDifference != 0.0) {
                mob.motion = Vec3d(
                    mob.motion.x + xDifference / totalDifference * speed,
                    mob.motion.x + yDifference / totalDifference * speed,
                    mob.motion.x + zDifference / totalDifference * speed
                )
            }
        }
    }
}