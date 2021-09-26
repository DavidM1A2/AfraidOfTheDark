package com.davidm1a2.afraidofthedark.client.particle

import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.client.world.ClientWorld
import net.minecraft.particles.BasicParticleType

/**
 * Particle representing a player's spell cast
 *
 * @constructor takes the x,y,z position of the particle and the world
 * @param world  The world the particle is at
 * @param x The x position of the spell cast effect
 * @param y The y position of the spell cast effect
 * @param z The z position of the spell cast effect
 */
class ExplosionParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double
) : AOTDParticle(world, x, y, z) {
    init {
        // 1 second lifespan
        lifetime = 20
        // Random motion
        xd = (random.nextFloat() - 0.5) * 0.5
        yd = (random.nextFloat() - 0.5) * 0.5
        zd = (random.nextFloat() - 0.5) * 0.5
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
            return ExplosionParticle(world, x, y, z).apply {
                pickSprite(spriteSet)
            }
        }
    }
}