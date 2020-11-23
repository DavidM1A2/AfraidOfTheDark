package com.davidm1a2.afraidofthedark.client.particle

import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.particles.BasicParticleType
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

/**
 * Particle used to create a smoke screen
 *
 * @constructor takes the x,y,z position of the particle and the world
 * @param world  The world the particle is at
 * @param x The x position of the smoke screen particle
 * @param y The y position of the smoke screen particle
 * @param z The z position of the smoke screen particle
 */
@OnlyIn(Dist.CLIENT)
class SmokeScreenParticle(
    world: World,
    x: Double,
    y: Double,
    z: Double
) : AOTDParticle(world, x, y, z, 0.0, 0.0, 0.0) {
    init {
        // 10-20 second lifespan
        maxAge = rand.nextInt(200) + 200

        // Blinding size teleport particles
        particleScale = 3f + rand.nextFloat() * 13f

        // Particle moves outwards
        motionX = rand.nextFloat() * 0.07
        motionX = if (rand.nextBoolean()) -motionX - 0.07 else motionX + 0.07
        motionY = rand.nextFloat() * 0.07
        motionY = if (rand.nextBoolean()) -motionY - 0.07 else motionY + 0.07
        motionZ = rand.nextFloat() * 0.07
        motionZ = if (rand.nextBoolean()) -motionZ - 0.07 else motionZ + 0.07
    }

    /**
     * Called before the particle is moved, update the motionXYZ here
     */
    override fun updateMotionXYZ() {
        // Slowly reduce motion
        motionX = motionX * 0.95
        motionY = motionY * 0.95
        motionZ = motionZ * 0.95
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
            return SmokeScreenParticle(world, x, y, z).apply {
                selectSpriteRandomly(spriteSet)
            }
        }
    }
}