package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.AOTDParticle
import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.client.world.ClientWorld
import net.minecraft.particles.BasicParticleType

class SelfFizzleParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    private val spriteSet: IAnimatedSprite
) : AOTDParticle(world, x, y, z) {
    init {
        // 1 second lifespan
        lifetime = 20
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

    class Factory(private val spriteSet: IAnimatedSprite) : IParticleFactory<BasicParticleType> {
        override fun createParticle(
            particle: BasicParticleType,
            world: ClientWorld,
            x: Double,
            y: Double,
            z: Double,
            xSpeed: Double,
            ySpeed: Double,
            zSpeed: Double
        ): Particle {
            return SelfFizzleParticle(world, x, y, z, spriteSet).apply {
                setSpriteFromAge(spriteSet)
            }
        }
    }
}