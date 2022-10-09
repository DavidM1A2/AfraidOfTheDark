package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.AOTDParticle
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
class WeaknessParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double
) : AOTDParticle(world, x, y, z) {
    init {
        // 1 second lifespan
        lifetime = 20
        // Gradually Descend
        xd = 0.0
        yd = -0.3
        zd = 0.0
    }

    override fun updateMotionXYZ() {
        yd *= 0.8
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
            return WeaknessParticle(world, x, y, z).apply {
                pickSprite(spriteSet)
            }
        }
    }
}