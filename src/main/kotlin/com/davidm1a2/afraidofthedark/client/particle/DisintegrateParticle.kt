package com.davidm1a2.afraidofthedark.client.particle

import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.client.world.ClientWorld
import net.minecraft.particles.BasicParticleType

class DisintegrateParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double
) : AOTDParticle(world, x, y, z) {
    init {
        // 0.5 second lifespan
        lifetime = 10
        // Random outwards motion
        xd = (random.nextDouble() - 0.5) * 0.5
        yd = (random.nextDouble() - 0.5) * 1.0
        zd = (random.nextDouble() - 0.5) * 0.5
    }

    override fun updateMotionXYZ() {
        yd -= 0.08
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
            return DisintegrateParticle(world, x, y, z).apply {
                pickSprite(spriteSet)
            }
        }
    }
}