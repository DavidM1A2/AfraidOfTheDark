package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.AOTDParticle
import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.client.world.ClientWorld
import net.minecraft.particles.BasicParticleType
import kotlin.random.Random

/**
 * The Freeze Particle
 */
class FreezeParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    freezeDurationTicks: Int
) : AOTDParticle(world, x, y, z) {
    init {
        scale(Random.nextFloat() + 1f)
        // up to 1 second longer than the freeze duration
        lifetime = freezeDurationTicks + random.nextInt(20)
        // No motion
        xd = 0.0
        yd = 0.0
        zd = 0.0
    }

    override fun updateMotionXYZ() {
        super.updateMotionXYZ()
        setAlphaFadeInLastTicks(20f)
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
            return FreezeParticle(world, x, y, z, xSpeed.toInt()).apply {
                pickSprite(spriteSet)
            }
        }
    }
}