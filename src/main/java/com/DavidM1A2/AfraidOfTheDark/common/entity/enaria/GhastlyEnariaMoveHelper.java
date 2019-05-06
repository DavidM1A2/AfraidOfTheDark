package com.DavidM1A2.afraidofthedark.common.entity.enaria;

import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.MathHelper;

/**
 * Utility class to allow enaria to path through walls
 */
public class GhastlyEnariaMoveHelper extends EntityMoveHelper
{
    /**
     * Sets the enaria reference by calling super
     */
    public GhastlyEnariaMoveHelper(EntityGhastlyEnaria enaria)
    {
        super(enaria);
    }

    /**
     * Gets called when the move helper should update and move the entity
     */
    @Override
    public void onUpdateMoveHelper()
    {
        // Only update if updating is true
        if (this.isUpdating())
        {
            // Code copied from super, moves the entity in the direction enaria wants to move to
            this.action = Action.WAIT;

            // Compute the distance between the entities current position and the position the entity wants to be in
            double xDifference = this.posX - this.entity.posX;
            double yDifference = this.posY - this.entity.posY;
            double zDifference = this.posZ - this.entity.posZ;
            // Compute the total difference in all distances
            double totalDifference = MathHelper.sqrt(xDifference * xDifference + yDifference * yDifference + zDifference * zDifference);

            // If we haven't gotten to the final position update the entities motion to be in the correct direction
            if (totalDifference != 0)
            {
                this.entity.motionX += xDifference / totalDifference * this.speed;
                this.entity.motionY += yDifference / totalDifference * this.speed;
                this.entity.motionZ += zDifference / totalDifference * this.speed;
            }
        }
    }
}
