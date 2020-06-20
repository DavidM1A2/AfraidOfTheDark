package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.common.constants.ModSprites
import net.minecraft.world.World

/**
 * Particle representing the laser delivery spell's laser
 *
 * @constructor takes the x,y,z position of the particle and the world
 * @param world  The world the particle is at
 * @param x The x position of the laser spell attack
 * @param y The y position of the laser spell attack
 * @param z The z position of the laser spell attack
 */
class ParticleSpellLaser(
    world: World,
    x: Double,
    y: Double,
    z: Double
) : AOTDParticle(world, ModSprites.SPELL_LASER, x, y, z) {
    init {
        // 1 second lifespan
        maxAge = 20

        // No motion
        motionX = 0.0
        motionY = 0.0
        motionZ = 0.0

        // Scale starts at 1.0
        particleScale = 1.0f
    }

    /**
     * Called before the particle is moved, update the motionXYZ here
     */
    override fun updateMotionXYZ() {
        // Set scale to be based on time alive
        particleScale = (maxAge - age) / maxAge.toFloat()
    }
}