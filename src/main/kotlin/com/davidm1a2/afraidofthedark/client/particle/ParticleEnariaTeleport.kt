package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.common.constants.ModSprites
import net.minecraft.world.World

/**
 * Particle representing enaria's teleport
 *
 * @constructor takes the x,y,z position of the particle and the world
 * @param world  The world the particle is at
 * @param x The x position of the teleport
 * @param y The y position of the teleport
 * @param z The z position of the teleport
 */
class ParticleEnariaTeleport(
    world: World,
    x: Double,
    y: Double,
    z: Double
) : AOTDParticle(world, ModSprites.ENARIA_TELEPORT, x, y, z) {
    init {
        // 2-3 second lifespan
        maxAge = rand.nextInt(20) + 40

        // Blinding size teleport particles
        particleScale = 10.0f + rand.nextFloat() * 5

        // Upwards motion only
        motionX = 0.0
        motionY = rand.nextFloat() * 0.1 + 0.3
        motionZ = 0.0
    }
}