package com.davidm1a2.afraidofthedark.common.entity.enaria

import net.minecraft.entity.ai.EntityMoveHelper
import kotlin.math.sqrt

/**
 * Utility class to allow enaria to path through walls
 *
 * @param enaria the enaria reference by calling super
 */
class GhastlyEnariaMoveHelper(enaria: EntityGhastlyEnaria) : EntityMoveHelper(enaria)
{
    /**
     * Gets called when the move helper should update and move the entity
     */
    override fun onUpdateMoveHelper()
    {
        // Only update if updating is true
        if (this.isUpdating)
        {
            // Code copied from super, moves the entity in the direction enaria wants to move to
            action = Action.WAIT
            // Compute the distance between the entities current position and the position the entity wants to be in
            val xDifference = posX - entity.posX
            val yDifference = posY - entity.posY
            val zDifference = posZ - entity.posZ
            // Compute the total difference in all distances
            val totalDifference = sqrt(xDifference * xDifference + yDifference * yDifference + zDifference * zDifference)
            // If we haven't gotten to the final position update the entities motion to be in the correct direction
            if (totalDifference != 0.0)
            {
                entity.motionX += xDifference / totalDifference * speed
                entity.motionY += yDifference / totalDifference * speed
                entity.motionZ += zDifference / totalDifference * speed
            }
        }
    }
}