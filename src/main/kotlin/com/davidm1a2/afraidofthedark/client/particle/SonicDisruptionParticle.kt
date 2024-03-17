package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.AOTDParticle
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.Mth

class SonicDisruptionParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    sizePercent: Float
) : AOTDParticle(world, x, y, z) {
    init {
        scale(Mth.lerp(sizePercent, MIN_SIZE, MAX_SIZE))
        lifetime = 20
        // No motion
        xd = 0.0
        yd = 0.0
        zd = 0.0
    }

    override fun tick() {
        super.tick()
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
            return SonicDisruptionParticle(world, x, y, z, xSpeed.toFloat()).apply {
                pickSprite(spriteSet)
            }
        }
    }

    companion object {
        private const val MIN_SIZE = 0.5f
        private const val MAX_SIZE = 2f
    }
}