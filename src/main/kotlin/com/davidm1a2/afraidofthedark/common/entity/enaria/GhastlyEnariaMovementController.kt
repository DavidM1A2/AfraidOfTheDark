package com.davidm1a2.afraidofthedark.common.entity.enaria

import net.minecraft.entity.ai.controller.MovementController
import net.minecraft.util.math.vector.Vector3d
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
        if (operation == Action.MOVE_TO) {
            // Code copied from super, moves the entity in the direction enaria wants to move to
            operation = Action.WAIT

            // Compute the distance between the entities current position and the position the entity wants to be in
            val xDifference = this.wantedX - mob.x
            val yDifference = this.wantedY - mob.y
            val zDifference = this.wantedZ - mob.z

            // Compute the total difference in all distances
            val totalDifference =
                sqrt(xDifference * xDifference + yDifference * yDifference + zDifference * zDifference)

            // If we haven't gotten to the final position update the entities motion to be in the correct direction
            if (totalDifference != 0.0) {
                mob.deltaMovement = Vector3d(
                    mob.deltaMovement.x + xDifference / totalDifference * speedModifier,
                    mob.deltaMovement.y + yDifference / totalDifference * speedModifier,
                    mob.deltaMovement.z + zDifference / totalDifference * speedModifier
                )
            }
        }
    }
}