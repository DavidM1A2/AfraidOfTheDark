package com.davidm1a2.afraidofthedark.client.particle

import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.client.world.ClientWorld
import net.minecraft.particles.BasicParticleType

/**
 * Particle used to create a smoke screen
 *
 * @constructor takes the x,y,z position of the particle and the world
 * @param world  The world the particle is at
 * @param x The x position of the smoke screen particle
 * @param y The y position of the smoke screen particle
 * @param z The z position of the smoke screen particle
 */
class SmokeScreenParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double
) : AOTDParticle(world, x, y, z, 0.0, 0.0, 0.0) {
    init {
        // 10-20 second lifespan
        lifetime = random.nextInt(200) + 200

        // Blinding size teleport particles
        scale(1f + random.nextFloat() * 2f)

        // Particle moves outwards
        xd = random.nextFloat() * 0.07
        xd = if (random.nextBoolean()) -xd - 0.07 else xd + 0.07
        yd = random.nextFloat() * 0.07
        yd = if (random.nextBoolean()) -yd - 0.07 else yd + 0.07
        zd = random.nextFloat() * 0.07
        zd = if (random.nextBoolean()) -zd - 0.07 else zd + 0.07
    }

    /**
     * Called before the particle is moved, update the motionXYZ here
     */
    override fun updateMotionXYZ() {
        // Slowly reduce motion
        xd = xd * 0.95
        yd = yd * 0.95
        zd = zd * 0.95
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
            return SmokeScreenParticle(world, x, y, z).apply {
                pickSprite(spriteSet)
            }
        }
    }
}