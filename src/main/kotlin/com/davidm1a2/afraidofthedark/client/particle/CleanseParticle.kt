package com.davidm1a2.afraidofthedark.client.particle

import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.client.world.ClientWorld
import net.minecraft.particles.BasicParticleType

class CleanseParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double
) : AOTDParticle(world, x, y, z) {
    init {
        // 1-1.5 second lifespan
        lifetime = 20 + random.nextInt(10)
        // Drift Upwards
        xd = if (random.nextBoolean()) -0.05 else 0.05
        yd = 0.2
        zd = if (random.nextBoolean()) -0.05 else 0.05
    }

    override fun updateMotionXYZ() {
        xd *= 0.9
        yd *= 0.9
        zd *= 0.9
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
            return CleanseParticle(world, x, y, z).apply {
                pickSprite(spriteSet)
            }
        }
    }
}