package com.davidm1a2.afraidofthedark.common.dimension

import net.minecraft.entity.Entity
import net.minecraft.world.World
import net.minecraftforge.common.util.ITeleporter

/**
 * Teleporter class that does nothing except reset entity motion
 */
class NoopTeleporter : ITeleporter {
    /**
     * Spawns the entity in the world
     *
     * @param world  The world to spawn the entity in
     * @param entity The entity that is teleporting
     * @param yaw    The yaw of the entity
     */
    override fun placeEntity(world: World, entity: Entity, yaw: Float) {
        // Reset the entity's motion so they don't die of fall damage
        entity.motionX = 0.0
        entity.motionY = 0.0
        entity.motionZ = 0.0
    }

    /**
     * @return False, not a vanilla teleporter
     */
    override fun isVanilla(): Boolean {
        return false
    }
}