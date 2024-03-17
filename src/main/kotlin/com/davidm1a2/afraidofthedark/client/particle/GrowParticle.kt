package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.AOTDParticle
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.SimpleParticleType
import kotlin.random.Random

/**
 * Particle representing a player's spell cast
 *
 * @constructor takes the x,y,z position of the particle and the world
 * @param world  The world the particle is at
 * @param x The x position of the spell cast effect
 * @param y The y position of the spell cast effect
 * @param z The z position of the spell cast effect
 */
class GrowParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double
) : AOTDParticle(world, x, y, z) {
    init {
        scale(0.3f + Random.nextFloat())

        // 1-1.5 second lifespan
        lifetime = 20 + random.nextInt(10)
        // Drift Upwards
        xd = (random.nextDouble() - 0.5) * 0.3
        yd = random.nextDouble() * 0.3
        zd = (random.nextDouble() - 0.5) * 0.3

        rCol = 0.5f
        gCol = 0.5f
        bCol = 0.5f
    }

    override fun updateMotionXYZ() {
        xd *= 0.9
        yd *= 0.95
        zd *= 0.9
        setAlphaFadeInLastTicks(3f)
        if (age <= 6) {
            rCol = age / 6f
            gCol = age / 6f
            bCol = age / 6f
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
            return GrowParticle(world, x, y, z).apply {
                pickSprite(spriteSet)
            }
        }
    }
}