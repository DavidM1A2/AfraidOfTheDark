package com.davidm1a2.afraidofthedark.client.particle

import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.client.world.ClientWorld
import net.minecraft.particles.BasicParticleType

class FizzleParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    xSpeed: Double,
    ySpeed: Double,
    zSpeed: Double
) : AOTDParticle(world, x, y, z, xSpeed, ySpeed, zSpeed) {
    init {
        scale(2f)

        lifetime = 20
    }

    override fun tick() {
        super.tick()

        yd = yd * 0.8

        scale(SCALE_FACTOR_PER_TICK)

        setAlphaFadeInLastTicks(20f)
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
            return FizzleParticle(world, x, y, z, xSpeed, ySpeed, zSpeed).apply {
                pickSprite(spriteSet)
            }
        }
    }

    companion object {
        private const val SCALE_FACTOR_PER_TICK = 0.95f
    }
}