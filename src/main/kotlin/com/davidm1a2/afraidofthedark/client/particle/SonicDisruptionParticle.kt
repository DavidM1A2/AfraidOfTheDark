package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.AOTDParticle
import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.client.world.ClientWorld
import net.minecraft.particles.BasicParticleType
import net.minecraft.util.math.MathHelper

class SonicDisruptionParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    sizePercent: Float
) : AOTDParticle(world, x, y, z) {
    init {
        scale(MathHelper.lerp(sizePercent, MIN_SIZE, MAX_SIZE))
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