package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.AOTDParticle
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.SimpleParticleType

class DelayParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    scale: Float,
    private val spriteSet: SpriteSet
) : AOTDParticle(world, x, y, z) {
    init {
        // 1 second lifespan
        lifetime = 20
        scale(scale)
        // Moves upwards based on how big it is
        xd = 0.0
        yd = 0.12 * scale
        zd = 0.0
    }

    override fun tick() {
        super.tick()
        setSpriteFromAge(spriteSet)
        setAlphaFadeInLastTicks(15f)
    }

    override fun updateMotionXYZ() {
        super.updateMotionXYZ()
        yd = yd * 0.9
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
            return DelayParticle(world, x, y, z, xSpeed.toFloat(), spriteSet).apply {
                setSpriteFromAge(spriteSet)
            }
        }
    }
}