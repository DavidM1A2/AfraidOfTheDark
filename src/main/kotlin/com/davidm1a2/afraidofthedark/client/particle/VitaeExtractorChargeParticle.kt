package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.AOTDParticle
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.SimpleParticleType
import kotlin.math.cos
import kotlin.math.sin

class VitaeExtractorChargeParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double
) : AOTDParticle(world, x, y, z) {
    private val startAngle = random.nextInt(360)
    private val startX = x
    private val startZ = z

    init {
        // 2 second lifespan
        lifetime = 40

        // Make the particles noticable
        scale(1f)

        xd = 0.0
        yd = 0.03
        zd = 0.0
    }

    override fun updateMotionXYZ() {
        // Slowly make the particle fade
        alpha = (lifetime - age) / lifetime.toFloat()
        val angle = (age * 10.0 + startAngle) % 360
        val xOffset = ORBIT_DISTANCE * sin(Math.toRadians(angle))
        val zOffset = ORBIT_DISTANCE * cos(Math.toRadians(angle))
        setPos(startX + xOffset, y, startZ + zOffset)
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
            return VitaeExtractorChargeParticle(world, x, y, z).apply {
                pickSprite(spriteSet)
            }
        }
    }

    companion object {
        private const val ORBIT_DISTANCE = 0.25
    }
}