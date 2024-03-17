package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.AOTDParticle
import com.davidm1a2.afraidofthedark.common.spell.component.effect.ExplosionSpellEffect
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.Mth
import kotlin.math.PI
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Particle representing a player's spell cast
 *
 * @constructor takes the x,y,z position of the particle and the world
 * @param world  The world the particle is at
 * @param x The x position of the spell cast effect
 * @param y The y position of the spell cast effect
 * @param z The z position of the spell cast effect
 */
class ExplosionParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    explosionRadius: Double
) : AOTDParticle(world, x, y, z) {
    private val fadeSpeed = FADE_SPEED_MIN + random.nextDouble() * (FADE_SPEED_MAX - FADE_SPEED_MIN)
    private val sinOffset = random.nextDouble() * 2 * PI

    init {
        scale(random.nextFloat() * 3f + 1f)

        // 3-8 second lifespan based on radius
        lifetime = Mth.lerp(explosionRadius / ExplosionSpellEffect.MAX_RADIUS, 60.0, 160.0).toInt()

        // Random motion
        xd = (random.nextFloat() - 0.5) * 0.8 * sqrt(explosionRadius)
        yd = (random.nextFloat() - 0.1) * 0.8 * sqrt(explosionRadius)
        zd = (random.nextFloat() - 0.5) * 0.8 * sqrt(explosionRadius)
    }

    override fun updateMotionXYZ() {
        xd = xd * 0.95
        yd = (yd - 0.08).coerceAtLeast(-2.0)
        zd = zd * 0.95

        val oscillation = (sin(sinOffset + age * fadeSpeed).toFloat() + 1f) / 2f
        val fadeAlpha = oscillation * 0.5f + 0.5f
        setAlphaFadeInLastTicks(40f)
        alpha = alpha * fadeAlpha
        setColor(fadeAlpha, fadeAlpha, fadeAlpha)
    }

    class Factory(private val spriteSet: SpriteSet) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            particle: SimpleParticleType,
            world: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            // Technically 'xSpeed', but actually used as explosion radius
            xSpeed: Double,
            ySpeed: Double,
            zSpeed: Double
        ): Particle {
            return ExplosionParticle(world, x, y, z, xSpeed).apply {
                pickSprite(spriteSet)
            }
        }
    }

    companion object {
        private const val FADE_SPEED_MIN = 0.24
        private const val FADE_SPEED_MAX = 0.32
    }
}