package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.AOTDParticle
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.Mth

/**
 * Particle used to create a smoke screen
 *
 * @constructor takes the x,y,z position of the particle and the world
 * @param world  The world the particle is at
 * @param x The x position of the smoke screen particle
 * @param y The y position of the smoke screen particle
 * @param z The z position of the smoke screen particle
 */
class SmokeScreenParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double
) : AOTDParticle(world, x, y, z, 0.0, 0.0, 0.0) {
    private val minScale = 3.75f + random.nextFloat() * 1.25f
    private val maxScale = 7.5f + minScale
    private val baseQuadSize: Float

    init {
        // 15-20 second lifespan
        lifetime = random.nextInt(100) + 300

        // Blinding size teleport particles
        scale(minScale)
        baseQuadSize = quadSize / 5

        // Particle moves outwards
        xd = (random.nextDouble() - 0.5) * SPEED
        yd = (random.nextDouble() - 0.5) * SPEED
        zd = (random.nextDouble() - 0.5) * SPEED
    }

    /**
     * Called before the particle is moved, update the motionXYZ here
     */
    override fun updateMotionXYZ() {
        // Slowly reduce motion
        xd = xd * 0.95
        yd = yd * 0.95
        zd = zd * 0.95
        // Expand the particle over time
        val newScale = Mth.lerp(age.toFloat() / lifetime, minScale, maxScale)
        // For whatever reason "scale" does quadSize *= newScale, so reset it to avoid exponential quad size growth
        scale(newScale)
        quadSize = baseQuadSize * newScale
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
            return SmokeScreenParticle(world, x, y, z).apply {
                pickSprite(spriteSet)
            }
        }
    }

    companion object {
        private const val SPEED = 0.05
    }
}