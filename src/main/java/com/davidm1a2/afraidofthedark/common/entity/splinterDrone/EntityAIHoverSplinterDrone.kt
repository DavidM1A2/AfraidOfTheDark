package com.davidm1a2.afraidofthedark.common.entity.splinterDrone

import net.minecraft.block.BlockAir
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper

/**
 * AI task that makes the splinter drone randomly fly around like a ghast. Code from EntityGhast
 *
 * @constructor Initializes the entity
 * @property splinterDrone The splinter drone entity that is flying around
 */
class EntityAIHoverSplinterDrone(private val splinterDrone: EntitySplinterDrone) : EntityAIBase()
{
    init
    {
        mutexBits = 1
    }

    /**
     * @return True if the ai task should be executed, false otherwise
     */
    override fun shouldExecute(): Boolean
    {
        // The move helper to move the splinter drone
        val entityMoveHelper = splinterDrone.moveHelper
        // Randomly fly if the move helper isn't updating and the drone doesn't have a target
        return if (!entityMoveHelper.isUpdating && splinterDrone.attackTarget == null)
        {
            true
        }
        else
        {
            // Compute the distance from the move position to the drone, if it's more than 60 or less than 1 then
            // execute, otherwise don't
            val xDistance = entityMoveHelper.x - splinterDrone.posX
            val yDistance = entityMoveHelper.y - splinterDrone.posY
            val zDistance = entityMoveHelper.z - splinterDrone.posZ
            val overallDistance = MathHelper.sqrt(xDistance * xDistance + yDistance * yDistance + zDistance * zDistance).toDouble()
            overallDistance < 1 || overallDistance > 60
        }
    }

    /**
     * @return False, this task only executes when the splinter drone needs a new position to go to
     */
    override fun shouldContinueExecuting(): Boolean
    {
        return false
    }

    /**
     * Executes the task and finds a new place to go
     */
    override fun startExecuting()
    {
        // Get the random object off of the splinter drone
        val random = splinterDrone.rng
        // Compute random x and z positions to go to, also ensure the splinter drone stays y=3 blocks above the ground
        val xToGoTo = splinterDrone.posX + (random.nextFloat() * 2.0f - 1.0f) * 16.0f
        val zToGoTo = splinterDrone.posZ + (random.nextFloat() * 2.0f - 1.0f) * 16.0f
        val yToGoTo = getHeightToMoveTo(xToGoTo, zToGoTo)
        // Move the the x,y,z position
        splinterDrone.moveHelper.setMoveTo(xToGoTo, yToGoTo, zToGoTo, splinterDrone.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).attributeValue)
    }

    /**
     * Gets the y value 3 blocks above the ground at x,z
     *
     * @param x The x position to get the y coordinate from
     * @param z The z position to get the y coordinate from
     * @return The y position to move to
     */
    private fun getHeightToMoveTo(x: Double, z: Double): Double
    {
        // Go from the splinter drone's y position downwards until we find a solid block
        for (y in MathHelper.floor(splinterDrone.posY) downTo 1)
        {
            // If the block is non-air (so solid) move 3 blocks above it
            if (splinterDrone.world.getBlockState(BlockPos(x, y.toDouble(), z)).block !is BlockAir)
            {
                return MathHelper.clamp(y + 3.toDouble(), 0.0, 255.0)
            }
        }
        // If we can't find a position just stay at the same height
        return splinterDrone.posY
    }
}