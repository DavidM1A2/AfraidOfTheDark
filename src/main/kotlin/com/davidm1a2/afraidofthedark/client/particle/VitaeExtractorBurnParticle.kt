package com.davidm1a2.afraidofthedark.client.particle

import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.client.world.ClientWorld
import net.minecraft.particles.BasicParticleType

class VitaeExtractorBurnParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double
) : AOTDParticle(world, x, y, z) {
    init {
        // 0.5-1.5 second lifespan
        lifetime = random.nextInt(30) + 10

        // Make the particles noticable
        scale(0.2f + random.nextFloat() * .1f)
    }

    override fun updateMotionXYZ() {
        // Slowly make the particle fade
        alpha = (lifetime - age) / lifetime.toFloat()
        xd = random.nextFloat() * 0.02 - 0.01
        yd = random.nextFloat() * 0.02 - 0.01
        zd = random.nextFloat() * 0.02 - 0.01
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
            return VitaeExtractorBurnParticle(world, x, y, z).apply {
                pickSprite(spriteSet)
            }
        }
    }
}