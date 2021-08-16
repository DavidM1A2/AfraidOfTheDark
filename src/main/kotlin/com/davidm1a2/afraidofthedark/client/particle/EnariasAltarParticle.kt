package com.davidm1a2.afraidofthedark.client.particle

import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.client.world.ClientWorld
import net.minecraft.particles.BasicParticleType

/**
 * Particle spawned when enaria's altar spins
 *
 * @constructor takes the x,y,z position of the particle and the world
 * @param world  The world the particle is at
 * @param x The x position of the particle
 * @param y The y position of the particle
 * @param z The z position of the particle
 */
class EnariasAltarParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double
) : AOTDParticle(world, x, y, z) {
    init {
        // 2-3 second lifespan
        lifetime = random.nextInt(20) + 40
        scale(0.2f)

        xd = 0.0
        yd = 0.01
        zd = 0.0
    }

    /**
     * Called before the particle is moved, update the motionXYZ here
     */
    override fun updateMotionXYZ() {
        // Slowly increase y motion
        yd = yd * 1.02
        // Slowly make the particle fade
        alpha = (lifetime - age) / lifetime.toFloat()
    }

    class Factory(private val spriteSet: IAnimatedSprite) : IParticleFactory<BasicParticleType> {
        override fun createParticle(
            particle: BasicParticleType,
            world: ClientWorld,
            x: Double,
            y: Double,
            z: Double,
            xSpeed: Double,
            ySpeed: Double,
            zSpeed: Double
        ): Particle {
            return EnariasAltarParticle(world, x, y, z).apply {
                pickSprite(spriteSet)
            }
        }
    }
}