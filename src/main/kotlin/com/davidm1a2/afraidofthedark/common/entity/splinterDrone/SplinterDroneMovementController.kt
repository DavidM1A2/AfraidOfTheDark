package com.davidm1a2.afraidofthedark.common.entity.splinterDrone

import net.minecraft.entity.ai.controller.MovementController
import net.minecraft.util.math.Vec3d
import kotlin.math.sqrt

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

                // Compute the x, y, and z motion vectors as well as the magnitude to normalize the vector
                val xDir = posX - mob.posX
                val yDir = posY - mob.posY
                val zDir = posZ - mob.posZ
                val dirMagnitude = sqrt(xDir * xDir + yDir * yDir + zDir * zDir)

                // If we can move to our position update the entity's motion to move to the position
                if (isNotColliding(posX, posY, posZ, dirMagnitude)) {
                    mob.motion = Vec3d(
                        mob.motion.x + xDir / dirMagnitude * speed,
                        mob.motion.y + yDir / dirMagnitude * speed,
                        mob.motion.z + zDir / dirMagnitude * speed
                    )
                } else {
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
    private fun isNotColliding(posX: Double, posY: Double, posZ: Double, dirMagnitude: Double): Boolean {
        // Compute the x,y,z components of the direction vector
        val x = (posX - mob.posX) / dirMagnitude
        val y = (posY - mob.posY) / dirMagnitude
        val z = (posZ - mob.posZ) / dirMagnitude

        // Grab the entity bounding box
        var axisalignedbb = mob.boundingBox

        // Test if this entity can fit in each position as it moves to the position
        var i = 1
        while (i < dirMagnitude) {
            // Move the bounding box in the direction of movement
            axisalignedbb = axisalignedbb.offset(x, y, z)
            // Test if the entity would fit
            if (mob.world.isCollisionBoxesEmpty(mob, axisalignedbb)) {
                // False, there is no path
                return false
            }
            i++
        }
        // True, we can make the move
        return true
    }
}