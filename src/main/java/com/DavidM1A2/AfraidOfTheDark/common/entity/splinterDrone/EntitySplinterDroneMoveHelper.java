package com.DavidM1A2.afraidofthedark.common.entity.splinterDrone;

import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

/**
 * Move helper makes the splinter drone move like a ghast
 */
public class EntitySplinterDroneMoveHelper extends EntityMoveHelper
{
    // The number of ticks until we should re-compute pathing
    private int ticksUntilNextUpdate;

    /**
     * Constructor sets the splinter drone entity reference
     *
     * @param splinterDrone The splinter drone entity to move
     */
    public EntitySplinterDroneMoveHelper(EntitySplinterDrone splinterDrone)
    {
        super(splinterDrone);
    }

    /**
     * Called to update movement of the entity, code copied from the ghast
     */
    @Override
    public void onUpdateMoveHelper()
    {
        // Test if this move helper is updating and doesn't have a target, fly around
        if (this.action == EntityMoveHelper.Action.MOVE_TO && this.entity.getAttackTarget() == null)
        {
            // If we should update perform a motion update
            if (this.ticksUntilNextUpdate-- <= 0)
            {
                // Update this entity again in 2 - 7 ticks
                this.ticksUntilNextUpdate = this.ticksUntilNextUpdate + this.entity.getRNG().nextInt(5) + 2;

                // Compute the x, y, and z motion vectors as well as the magnitude to normalize the vector
                double xDir = this.posX - this.entity.posX;
                double yDir = this.posY - this.entity.posY;
                double zDir = this.posZ - this.entity.posZ;
                double dirMagnitude = MathHelper.sqrt(xDir * xDir + yDir * yDir + zDir * zDir);

                // If we can move to our position update the entity's motion to move to the position
                if (this.isNotColliding(this.posX, this.posY, this.posZ, dirMagnitude))
                {
                    this.entity.motionX = this.entity.motionX + xDir / dirMagnitude * this.speed;
                    this.entity.motionY = this.entity.motionY + yDir / dirMagnitude * this.speed;
                    this.entity.motionZ = this.entity.motionZ + zDir / dirMagnitude * this.speed;
                }
                // Otherwise just wait for now
                else
                {
                    this.action = Action.WAIT;
                }
            }
        }
    }

    /**
     * Tests if we can move to a given x,y,z position given a direction magnitude
     *
     * @param posX         The x position to move to
     * @param posY         The y position to move to
     * @param posZ         The z position to move to
     * @param dirMagnitude The magnitude of the vector
     * @return True if the splinter can move to the pos x,y,z, false otherwise
     */
    private boolean isNotColliding(double posX, double posY, double posZ, double dirMagnitude)
    {
        // Compute the x,y,z components of the direction vector
        double x = (posX - this.entity.posX) / dirMagnitude;
        double y = (posY - this.entity.posY) / dirMagnitude;
        double z = (posZ - this.entity.posZ) / dirMagnitude;
        // Grab the entity bounding box
        AxisAlignedBB axisalignedbb = this.entity.getEntityBoundingBox();

        // Test if this entity can fit in each position as it moves to the position
        for (int i = 1; i < dirMagnitude; i++)
        {
            // Move the bounding box in the direction of movement
            axisalignedbb = axisalignedbb.offset(x, y, z);

            // Test if the entity would fit
            if (!this.entity.world.getCollisionBoxes(this.entity, axisalignedbb).isEmpty())
            {
                // False, there is no path
                return false;
            }
        }

        // True, we can make the move
        return true;
    }
}
