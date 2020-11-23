package com.davidm1a2.afraidofthedark.client.particle

import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.particles.BasicParticleType
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

/**
 * Particle representing an enchanted frog's spawn animation
 *
 * @constructor takes the x,y,z position of the particle and the world
 * @param world  The world the particle is at
 * @param x The x position of the basic attack
 * @param y The y position of the basic attack
 * @param z The z position of the basic attack
 */
@OnlyIn(Dist.CLIENT)
class EnchantedFrogSpawnParticle(
    world: World,
    x: Double,
    y: Double,
    z: Double,
    xSpeed: Double,
    zSpeed: Double
) : AOTDParticle(world, x, y, z) {
    init {
        // 2 second lifespan
        maxAge = 40
        // Scale is random
        particleScale = rand.nextInt(3) + 2f
        // Slow up motion
        motionX = xSpeed
        motionY = 0.01
        motionZ = zSpeed
    }

    /**
     * Slowly fade the particle
     */
    override fun tick() {
        super.tick()
        setAlphaF((maxAge - age).toFloat() / maxAge.toFloat())
        motionX = motionX * 0.7
        motionZ = motionZ * 0.7
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
            return EnchantedFrogSpawnParticle(world, x, y, z, xSpeed, zSpeed).apply {
                selectSpriteRandomly(spriteSet)
            }
        }
    }
}