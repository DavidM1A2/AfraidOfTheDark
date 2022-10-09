package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.AOTDParticle
import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.client.world.ClientWorld
import net.minecraft.particles.BasicParticleType

class FlyParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    xSpeed: Double,
    ySpeed: Double,
    zSpeed: Double
) : AOTDParticle(world, x, y, z, xSpeed, ySpeed, zSpeed) {
    init {
        // 0.5-1.5 second lifespan
        lifetime = random.nextInt(10) + 30
    }

    override fun updateMotionXYZ() {
        xd = xd * 0.9
        yd = yd * 0.9
        zd = zd * 0.9
        setAlphaFadeInLastTicks(4f)
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
            return FlyParticle(world, x, y, z, xSpeed, ySpeed, zSpeed).apply {
                pickSprite(spriteSet)
            }
        }
    }
}