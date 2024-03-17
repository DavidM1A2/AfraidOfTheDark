package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.AOTDParticle
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.SimpleParticleType

/**
 * Particle representing an enchanted frog's spawn animation
 *
 * @constructor takes the x,y,z position of the particle and the world
 * @param world  The world the particle is at
 * @param x The x position of the basic attack
 * @param y The y position of the basic attack
 * @param z The z position of the basic attack
 */
class EnchantedFrogSpawnParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    xSpeed: Double,
    zSpeed: Double
) : AOTDParticle(world, x, y, z) {
    init {
        // 2 second lifespan
        lifetime = 40
        // Scale is random
        scale(random.nextFloat() * 2.5f + 2.5f)
        // Slow up motion
        xd = xSpeed
        yd = 0.01
        zd = zSpeed
    }

    /**
     * Slowly fade the particle
     */
    override fun tick() {
        super.tick()
        alpha = (lifetime - age).toFloat() / lifetime.toFloat()
        xd = xd * 0.7
        zd = zd * 0.7
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
            return EnchantedFrogSpawnParticle(world, x, y, z, xSpeed, zSpeed).apply {
                pickSprite(spriteSet)
            }
        }
    }
}