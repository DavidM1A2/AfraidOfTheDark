package com.davidm1a2.afraidofthedark.client.particle

import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.particles.BasicParticleType
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

/**
 * A special particle spell hit effect
 *
 * @constructor takes the x,y,z position of the particle and the world
 * @param world  The world the particle is at
 * @param x The x position of the spell hit effect
 * @param y The y position of the spell hit effect
 * @param z The z position of the spell hit effect
 */
@OnlyIn(Dist.CLIENT)
class SpellHitParticle(
    world: World,
    x: Double,
    y: Double,
    z: Double
) : AOTDParticle(world, x, y, z) {
    init {
        // 0.5-1.5 second lifespan
        maxAge = rand.nextInt(30) + 10

        // Make the particles noticable
        particleScale = 1.0f + rand.nextFloat() * 4

        // Random motion
        motionX = (rand.nextFloat() - 0.5) * 0.05
        motionY = rand.nextFloat() * 0.02
        motionZ = (rand.nextFloat() - 0.5) * 0.05
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
            return SpellHitParticle(world, x, y, z).apply {
                selectSpriteRandomly(spriteSet)
            }
        }
    }
}