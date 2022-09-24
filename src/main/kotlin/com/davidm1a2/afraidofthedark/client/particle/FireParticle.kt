package com.davidm1a2.afraidofthedark.client.particle

import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.client.world.ClientWorld
import net.minecraft.particles.BasicParticleType
import kotlin.math.PI
import kotlin.math.sin

class FireParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    xSpeed: Double,
    ySpeed: Double,
    zSpeed: Double
) : AOTDParticle(world, x, y, z, xSpeed, ySpeed, zSpeed) {
    private val fadeOffset = random.nextDouble() * 2 * PI

    init {
        // 2-4 second lifespan
        lifetime = 40 + random.nextInt(40)
    }

    override fun updateMotionXYZ() {
        xd *= 0.9
        yd *= 0.95
        zd *= 0.9
        val oscillation = (sin(fadeOffset + age * FADE_SPEED).toFloat() + 1f) / 2f
        val fadeAlpha = oscillation * 0.8f + 0.2f
        setAlphaFadeInLastTicks(40f)
        alpha = alpha * fadeAlpha
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
            return FireParticle(world, x, y, z, xSpeed, ySpeed, zSpeed).apply {
                pickSprite(spriteSet)
            }
        }
    }

    companion object {
        private const val FADE_SPEED = 0.2
    }
}