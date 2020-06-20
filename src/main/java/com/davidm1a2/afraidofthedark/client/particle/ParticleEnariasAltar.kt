package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.common.constants.ModSprites
import net.minecraft.world.World

/**
 * Particle spawned when enaria's altar spins
 *
 * @constructor takes the x,y,z position of the particle and the world
 * @param world  The world the particle is at
 * @param x The x position of the particle
 * @param y The y position of the particle
 * @param z The z position of the particle
 */
class ParticleEnariasAltar(
    world: World,
    x: Double,
    y: Double,
    z: Double
) : AOTDParticle(world, ModSprites.ENARIAS_ALTAR, x, y, z) {
    init {
        // 2-3 second lifespan
        maxAge = rand.nextInt(20) + 40
        particleScale = 1.0f

        motionX = 0.0
        motionY = 0.01
        motionZ = 0.0
    }

    /**
     * Called before the particle is moved, update the motionXYZ here
     */
    override fun updateMotionXYZ() {
        // Slowly increase y motion
        motionY = motionY * 1.02
        // Slowly make the particle fade
        particleAlpha = (maxAge - age) / maxAge.toFloat()
    }
}