package com.davidm1a2.afraidofthedark.client.particle

import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.particles.BasicParticleType
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

/**
 * Particle representing a player's spell cast
 *
 * @constructor takes the x,y,z position of the particle and the world
 * @param world  The world the particle is at
 * @param x The x position of the spell cast effect
 * @param y The y position of the spell cast effect
 * @param z The z position of the spell cast effect
 */
@OnlyIn(Dist.CLIENT)
class FireParticle(
    world: World,
    x: Double,
    y: Double,
    z: Double
) : AOTDParticle(world, x, y, z) {
    init {
        // 1-1.5 second lifespan
        maxAge = 20 + rand.nextInt(10)
        // Drift Upwards
        motionX = (rand.nextDouble() - 0.5) * 0.3
        motionY = rand.nextDouble() * 0.3
        motionZ = (rand.nextDouble() - 0.5) * 0.3
    }

    override fun updateMotionXYZ() {
        motionX *= 0.9
        motionY *= 0.95
        motionZ *= 0.9
    }

    @OnlyIn(Dist.CLIENT)
    class Factory(private val spriteSet: IAnimatedSprite) : IParticleFactory<BasicParticleType> {
        override fun makeParticle(
            particle: BasicParticleType,
            world: World,
            x: Double,
            y: Double,
            z: Double,
            xSpeed: Double,
            ySpeed: Double,
            zSpeed: Double
        ): Particle {
            return FireParticle(world, x, y, z).apply {
                selectSpriteRandomly(spriteSet)
            }
        }
    }
}