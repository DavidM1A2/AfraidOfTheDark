package com.davidm1a2.afraidofthedark.common.entity.splinterDrone;

import net.minecraft.block.BlockAir;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

/**
 * AI task that makes the splinter drone randomly fly around like a ghast. Code from EntityGhast
 */
public class EntityAIHoverSplinterDrone extends EntityAIBase
{
    // The splinter drone entity that is flying around
    private EntitySplinterDrone splinterDrone;

    /**
     * Constructor initializes the entity
     */
    public EntityAIHoverSplinterDrone(EntitySplinterDrone splinterDrone)
    {
        this.splinterDrone = splinterDrone;
        this.setMutexBits(1);
    }

    /**
     * @return True if the ai task should be executed, false otherwise
     */
    @Override
    public boolean shouldExecute()
    {
        // The move helper to move the splinter drone
        EntityMoveHelper entityMoveHelper = this.splinterDrone.getMoveHelper();

        // Randomly fly if the move helper isn't updating and the drone doesn't have a target
        if (!entityMoveHelper.isUpdating() && this.splinterDrone.getAttackTarget() == null)
        {
            return true;
        }
        else
        {
            // Compute the distance from the move position to the drone, if it's more than 60 or less than 1 then
            // execute, otherwise don't
            double xDistance = entityMoveHelper.getX() - this.splinterDrone.posX;
            double yDistance = entityMoveHelper.getY() - this.splinterDrone.posY;
            double zDistance = entityMoveHelper.getZ() - this.splinterDrone.posZ;
            double overallDistance = MathHelper.sqrt(xDistance * xDistance + yDistance * yDistance + zDistance * zDistance);
            return overallDistance < 1 || overallDistance > 60;
        }
    }

    /**
     * @return False, this task only executes when the splinter drone needs a new position to go to
     */
    @Override
    public boolean shouldContinueExecuting()
    {
        return false;
    }

    /**
     * Executes the task and finds a new place to go
     */
    @Override
    public void startExecuting()
    {
        // Get the random object off of the splinter drone
        Random random = this.splinterDrone.getRNG();

        // Compute random x and z positions to go to, also ensure the splinter drone stays y=3 blocks above the ground
        double xToGoTo = this.splinterDrone.posX + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
        double zToGoTo = this.splinterDrone.posZ + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
        double yToGoTo = getHeightToMoveTo(xToGoTo, zToGoTo);
        // Move the the x,y,z position
        this.splinterDrone.getMoveHelper().setMoveTo(xToGoTo, yToGoTo, zToGoTo, this.splinterDrone.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
    }

    /**
     * Gets the y value 3 blocks above the ground at x,z
     *
     * @param x The x position to get the y coordinate from
     * @param z The z position to get the y coordinate from
     * @return The y position to move to
     */
    private double getHeightToMoveTo(double x, double z)
    {
        // Go from the splinter drone's y position downwards until we find a solid block
        for (int y = MathHelper.floor(splinterDrone.posY); y > 0; y--)
        {
            // If the block is non-air (so solid) move 3 blocks above it
            if (!(this.splinterDrone.world.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof BlockAir))
            {
                return MathHelper.clamp(y + 3, 0.0D, 255D);
            }
        }
        // If we can't find a position just stay at the same height
        return splinterDrone.posY;
    }
}
