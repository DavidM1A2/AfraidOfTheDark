package com.davidm1a2.afraidofthedark.client.particle

import net.minecraft.world.World

/**
 * Particle representing enaria's spell cast
 *
 * @constructor takes the x,y,z position of the particle and the world
 * @param world  The world the particle is at
 * @param x The x position of the enaria spell cast attack
 * @param y The y position of the enaria spell cast attack
 * @param z The z position of the enaria spell cast attack
 */
class EnariaSpellCastParticle(
    world: World,
    x: Double,
    y: Double,
    z: Double
) : AOTDParticle(world, x, y, z) {
    init {
        // 2-3 second lifespan
        maxAge = rand.nextInt(20) + 40

        // Make the particles huge when she casts a spell
        particleScale = 5.0f + rand.nextFloat() * 2

        // Random motion
        motionX = (rand.nextFloat() - 0.5) * 0.5
        motionY = rand.nextFloat() * 0.1
        motionZ = (rand.nextFloat() - 0.5) * 0.5
    }

    /**
     * Called before the particle is moved, update the motionXYZ here
     */
    override fun updateMotionXYZ() {
        // Slowly increase y motion
        motionY = motionY + 0.02
    }
}