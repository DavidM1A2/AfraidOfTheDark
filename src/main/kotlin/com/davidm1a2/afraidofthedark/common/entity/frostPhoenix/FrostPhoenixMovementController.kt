package com.davidm1a2.afraidofthedark.common.entity.frostPhoenix

import net.minecraft.entity.ai.controller.MovementController
import net.minecraft.util.math.vector.Vector3d

class FrostPhoenixMovementController(phoenix: FrostPhoenixEntity) : MovementController(phoenix) {
    private var ticksUntilNextUpdate = 0

    override fun tick() {
        // Test if this move helper is updating and doesn't have a target, fly around
        if (operation == Action.MOVE_TO) {
            // If we should update perform a motion update
            if (ticksUntilNextUpdate-- <= 0) {
                // Update this entity again in 2 - 7 ticks
                ticksUntilNextUpdate = ticksUntilNextUpdate + mob.random.nextInt(5) + 2

                if (mob.position().distanceToSqr(wantedX, wantedY, wantedZ) < 2 * 2) {
                    operation = Action.WAIT
                } else {
                    val dir = Vector3d(wantedX - mob.x, wantedY - mob.y, wantedZ - mob.z)
                    mob.deltaMovement = mob.deltaMovement.add(dir.normalize().scale(speedModifier))
                }
            }
        }
    }
}