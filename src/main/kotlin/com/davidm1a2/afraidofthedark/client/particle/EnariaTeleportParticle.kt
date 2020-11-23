package com.davidm1a2.afraidofthedark.client.particle

import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.particles.BasicParticleType
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

/**
 * Particle representing enaria's teleport
 *
 * @constructor takes the x,y,z position of the particle and the world
 * @param world  The world the particle is at
 * @param x The x position of the teleport
 * @param y The y position of the teleport
 * @param z The z position of the teleport
 */
@OnlyIn(Dist.CLIENT)
class EnariaTeleportParticle(
    world: World,
    x: Double,
    y: Double,
    z: Double
) : AOTDParticle(world, x, y, z) {
    init {
        // 2-3 second lifespan
        maxAge = rand.nextInt(20) + 40

        // Blinding size teleport particles
        particleScale = 10.0f + rand.nextFloat() * 5

        // Upwards motion only
        motionX = 0.0
        motionY = rand.nextFloat() * 0.1 + 0.3
        motionZ = 0.0
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
            return EnariaTeleportParticle(world, x, y, z).apply {
                selectSpriteRandomly(spriteSet)
            }
        }
    }
}