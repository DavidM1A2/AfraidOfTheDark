package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.AOTDParticle
import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.client.world.ClientWorld
import net.minecraft.particles.BasicParticleType

class DelayParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    scale: Float,
    private val spriteSet: IAnimatedSprite
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
            return DelayParticle(world, x, y, z, xSpeed.toFloat(), spriteSet).apply {
                setSpriteFromAge(spriteSet)
            }
        }
    }
}