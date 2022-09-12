package com.davidm1a2.afraidofthedark.client.particle

import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.client.world.ClientWorld
import net.minecraft.particles.BasicParticleType

/**
 * A special particle spell hit effect
 *
 * @constructor takes the x,y,z position of the particle and the world
 * @param world  The world the particle is at
 * @param x The x position of the spell hit effect
 * @param y The y position of the spell hit effect
 * @param z The z position of the spell hit effect
 */
class SpellHitParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double
) : AOTDParticle(world, x, y, z) {
    init {
        // 0.5-1.5 second lifespan
        lifetime = random.nextInt(30) + 10

        // Make the particles noticable
        scale(2.5f + random.nextFloat() * 10)

        // Random motion
        xd = (random.nextFloat() - 0.5) * 0.05
        yd = random.nextFloat() * 0.02
        zd = (random.nextFloat() - 0.5) * 0.05
    }

    /**
     * Called before the particle is moved, update the motionXYZ here
     */
    override fun updateMotionXYZ() {
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
            return SpellHitParticle(world, x, y, z).apply {
                pickSprite(spriteSet)
            }
        }
    }
}