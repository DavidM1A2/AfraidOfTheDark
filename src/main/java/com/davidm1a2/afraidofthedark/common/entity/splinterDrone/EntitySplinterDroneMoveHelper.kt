package com.davidm1a2.afraidofthedark.common.entity.splinterDrone

import net.minecraft.entity.ai.EntityMoveHelper
import kotlin.math.sqrt

/**
 * Move helper makes the splinter drone move like a ghast
 *
 * @constructor Sets the splinter drone entity reference
 * @param splinterDrone The splinter drone entity to move
 * @property ticksUntilNextUpdate The number of ticks until we should re-compute pathing
 */
class EntitySplinterDroneMoveHelper(splinterDrone: EntitySplinterDrone) : EntityMoveHelper(splinterDrone)
{
    private var ticksUntilNextUpdate = 0

    /**
     * Called to update movement of the entity, code copied from the ghast
     */
    override fun onUpdateMoveHelper()
    {
        // Test if this move helper is updating and doesn't have a target, fly around
        if (action == Action.MOVE_TO && entity.attackTarget == null)
        {
            // If we should update perform a motion update
            if (ticksUntilNextUpdate-- <= 0)
            {
                // Update this entity again in 2 - 7 ticks
                ticksUntilNextUpdate = ticksUntilNextUpdate + entity.rng.nextInt(5) + 2

                // Compute the x, y, and z motion vectors as well as the magnitude to normalize the vector
                val xDir = posX - entity.posX
                val yDir = posY - entity.posY
                val zDir = posZ - entity.posZ
                val dirMagnitude = sqrt(xDir * xDir + yDir * yDir + zDir * zDir)

                // If we can move to our position update the entity's motion to move to the position
                if (isNotColliding(posX, posY, posZ, dirMagnitude))
                {
                    entity.motionX = entity.motionX + xDir / dirMagnitude * speed
                    entity.motionY = entity.motionY + yDir / dirMagnitude * speed
                    entity.motionZ = entity.motionZ + zDir / dirMagnitude * speed
                }
                else
                {
                    action = Action.WAIT
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
    private fun isNotColliding(posX: Double, posY: Double, posZ: Double, dirMagnitude: Double): Boolean
    {
        // Compute the x,y,z components of the direction vector
        val x = (posX - entity.posX) / dirMagnitude
        val y = (posY - entity.posY) / dirMagnitude
        val z = (posZ - entity.posZ) / dirMagnitude

        // Grab the entity bounding box
        var axisalignedbb = entity.entityBoundingBox

        // Test if this entity can fit in each position as it moves to the position
        var i = 1
        while (i < dirMagnitude)
        {
            // Move the bounding box in the direction of movement
            axisalignedbb = axisalignedbb.offset(x, y, z)
            // Test if the entity would fit
            if (entity.world.getCollisionBoxes(entity, axisalignedbb).isNotEmpty())
            {
                // False, there is no path
                return false
            }
            i++
        }
        // True, we can make the move
        return true
    }
}