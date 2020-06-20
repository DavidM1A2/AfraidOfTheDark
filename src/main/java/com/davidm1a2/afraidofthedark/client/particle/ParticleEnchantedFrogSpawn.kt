package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.common.constants.ModSprites
import net.minecraft.world.World

/**
 * Particle representing an enchanted frog's spawn animation
 *
 * @constructor takes the x,y,z position of the particle and the world
 * @param world  The world the particle is at
 * @param x The x position of the basic attack
 * @param y The y position of the basic attack
 * @param z The z position of the basic attack
 */
class ParticleEnchantedFrogSpawn(
    world: World,
    x: Double,
    y: Double,
    z: Double,
    xSpeed: Double,
    zSpeed: Double
) : AOTDParticle(world, ModSprites.ENCHANTED_FROG_SPAWN, x, y, z) {
    init {
        // 2 second lifespan
        maxAge = 40
        // Scale is random
        particleScale = rand.nextInt(3) + 2f
        // Slow up motion
        motionX = xSpeed
        motionY = 0.01
        motionZ = zSpeed
    }

    /**
     * Slowly fade the particle
     */
    override fun tick() {
        super.tick()
        setAlphaF((maxAge - age).toFloat() / maxAge.toFloat())
        motionX = motionX * 0.7
        motionZ = motionZ * 0.7
    }
}