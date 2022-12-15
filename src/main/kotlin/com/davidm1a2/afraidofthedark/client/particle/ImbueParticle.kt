package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.AOTDParticle
import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.client.world.ClientWorld
import net.minecraft.particles.BasicParticleType

class ImbueParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double
) : AOTDParticle(world, x, y, z) {
    init {
        // 1.5 second lifespan
        lifetime = 30
        scale(1.5f)
        xd = (random.nextDouble() - 0.5) * 0.02
        yd = (random.nextDouble() - 0.5) * 0.02
        zd = (random.nextDouble() - 0.5) * 0.02
    }

    override fun tick() {
        super.tick()
        setAlphaFadeInLastTicks(4f)
    }

    override fun updateMotionXYZ() {
        super.updateMotionXYZ()
        xd = xd * 1.05
        yd = yd * 1.05
        zd = zd * 1.05
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
            return ImbueParticle(world, x, y, z).apply {
                pickSprite(spriteSet)
            }
        }
    }
}