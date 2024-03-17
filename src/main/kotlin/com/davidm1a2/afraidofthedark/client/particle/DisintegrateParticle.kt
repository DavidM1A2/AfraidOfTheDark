package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.AOTDParticle
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.SimpleParticleType

class DisintegrateParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double
) : AOTDParticle(world, x, y, z) {
    init {
        scale(3f)
        // 1 second lifespan
        lifetime = 20
        xd = 0.0
        yd = 0.0
        zd = 0.0
    }

    override fun updateMotionXYZ() {
        scale((lifetime - age) / lifetime.toFloat())
        setAlphaFadeInLastTicks(lifetime.toFloat())
    }

    class Factory(private val spriteSet: SpriteSet) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            particle: SimpleParticleType,
            world: ClientLevel,
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