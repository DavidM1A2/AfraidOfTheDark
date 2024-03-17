package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.AOTDParticle
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.SimpleParticleType

/**
 * Particle representing the laser delivery spell's laser
 *
 * @constructor takes the x,y,z position of the particle and the world
 * @param world  The world the particle is at
 * @param x The x position of the laser spell attack
 * @param y The y position of the laser spell attack
 * @param z The z position of the laser spell attack
 */
class SpellLaserParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double
) : AOTDParticle(world, x, y, z) {
    init {
        // 1 second lifespan
        lifetime = 20

        // No motion
        xd = 0.0
        yd = 0.0
        zd = 0.0
    }

    /**
     * Called before the particle is moved, update the motionXYZ here
     */
    override fun updateMotionXYZ() {
        // Set scale to be based on time alive
        scale((lifetime - age) / lifetime.toFloat() * 0.5f + 0.005f)
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
            return SpellLaserParticle(world, x, y, z).apply {
                pickSprite(spriteSet)
            }
        }
    }
}