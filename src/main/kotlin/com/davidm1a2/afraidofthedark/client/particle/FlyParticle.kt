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
class FlyParticle(
    world: World,
    x: Double,
    y: Double,
    z: Double
) : AOTDParticle(world, x, y, z) {
    init {
        // 0.5-1.5 second lifespan
        maxAge = rand.nextInt(10) + 30
        // Random motion
        motionX = (rand.nextFloat() - 0.5) * 0.2
        motionY = 0.2
        motionZ = (rand.nextFloat() - 0.5) * 0.2
    }

    override fun updateMotionXYZ() {
        motionY -= 0.05
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
            return FlyParticle(world, x, y, z).apply {
                selectSpriteRandomly(spriteSet)
            }
        }
    }
}