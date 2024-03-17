package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.AOTDParticle
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.SimpleParticleType

class ImbueFizzleParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    private val spriteSet: SpriteSet
) : AOTDParticle(world, x, y, z) {
    init {
        // 1.5 second lifespan
        lifetime = 30
        scale(1.5f)
        // Moves upwards
        xd = 0.0
        yd = 0.05
        zd = 0.0
    }

    override fun tick() {
        super.tick()
        setSpriteFromAge(spriteSet)
        setAlphaFadeInLastTicks(4f)
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
            return ImbueFizzleParticle(world, x, y, z, spriteSet).apply {
                setSpriteFromAge(spriteSet)
            }
        }
    }
}