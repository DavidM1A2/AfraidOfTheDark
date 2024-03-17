package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.AOTDParticle
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.SimpleParticleType

class FizzleParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    xSpeed: Double,
    ySpeed: Double,
    zSpeed: Double,
    private val spriteSet: SpriteSet
) : AOTDParticle(world, x, y, z, xSpeed, ySpeed, zSpeed) {
    init {
        scale(2f)

        lifetime = 20
    }

    override fun tick() {
        super.tick()

        yd = yd * 0.8

        scale(SCALE_FACTOR_PER_TICK)

        if (!removed) {
            setSpriteFromAge(spriteSet)
        }
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
            return FizzleParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet).apply {
                setSpriteFromAge(spriteSet)
            }
        }
    }

    companion object {
        private const val SCALE_FACTOR_PER_TICK = 0.98f
    }
}