package com.davidm1a2.afraidofthedark.common.dimension

import net.minecraft.entity.Entity
import net.minecraft.util.math.Vec3d
import net.minecraft.world.Teleporter
import net.minecraft.world.server.ServerWorld

/**
 * Teleporter class that does nothing except reset entity motion
 */
class NoopTeleporter(world: ServerWorld) : Teleporter(world) {
    /**
     * Spawns the entity in the world
     *
     * @param entity The entity that is teleporting
     * @param yaw    The yaw of the entity
     */
    override fun func_222268_a(entity: Entity, yaw: Float): Boolean {
        // Reset the entity's motion so they don't die of fall damage
        entity.motion = Vec3d(0.0, 0.0, 0.0)
        return true
    }
}