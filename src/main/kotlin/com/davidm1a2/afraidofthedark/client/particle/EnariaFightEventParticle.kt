package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.AOTDParticle
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.SimpleParticleType

/**
 * Particle representing the second part of enaria's spell cast
 *
 * @constructor takes the x,y,z position of the particle and the world
 * @param world  The world the particle is at
 * @param x The x position of the spell cast attack
 * @param y The y position of the spell cast attack
 * @param z The z position of the spell cast attack
 * @param xSpeed The x speed of the spell cast attack
 * @param zSpeed The z speed of the spell cast attack
 */
class EnariaFightEventParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    xSpeed: Double,
    zSpeed: Double
) : AOTDParticle(world, x, y, z, xSpeed, 0.0, zSpeed) {
    init {
        // 2-3 second lifespan
        lifetime = random.nextInt(20) + 40

        // Make the particles huge when she casts a spell
        scale(2f)

        // speed will be the same as motion for this particle
        xd = xSpeed
        yd = 0.0
        zd = zSpeed
    }

    /**
     * Called before the particle is moved, update the motionXYZ here
     */
    override fun updateMotionXYZ() {
        // Slowly increase y motion
        yd = yd - 0.02
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
            return EnariaFightEventParticle(world, x, y, z, xSpeed, zSpeed).apply {
                pickSprite(spriteSet)
            }
        }
    }
}