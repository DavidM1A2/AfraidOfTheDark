package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.AOTDParticle
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.SimpleParticleType
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class FireParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    ySpeed: Double
) : AOTDParticle(world, x, y, z, 0.0, ySpeed, 0.0) {
    private val fadeSpeed = FADE_SPEED_MIN + random.nextDouble() * (FADE_SPEED_MAX - FADE_SPEED_MIN)
    private val sinOffset = random.nextDouble() * 2 * PI

    init {
        // 2-4 second lifespan
        lifetime = 40 + random.nextInt(40)
    }

    override fun updateMotionXYZ() {
        xd = xd + WIGGLE_SPEED * sin(sinOffset + age * 0.2)
        yd = yd * 0.95
        zd = zd + WIGGLE_SPEED * cos(sinOffset + age * 0.2)
        val oscillation = (sin(sinOffset + age * fadeSpeed).toFloat() + 1f) / 2f
        val fadeAlpha = oscillation * 0.8f + 0.2f
        setAlphaFadeInLastTicks(40f)
        alpha = alpha * fadeAlpha
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
            return FireParticle(world, x, y, z, ySpeed).apply {
                pickSprite(spriteSet)
            }
        }
    }

    companion object {
        private const val FADE_SPEED_MIN = 0.24
        private const val FADE_SPEED_MAX = 0.32
        private const val WIGGLE_SPEED = 0.004
    }
}