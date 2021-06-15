package com.davidm1a2.afraidofthedark.client.particle

import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.particles.BasicParticleType
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

/**
 * Particle representing the second part of enaria's spell cast
 *
 * @constructor takes the x,y,z position of the particle and the world
 * @param world  The world the particle is at
 * @param x The x position of the spell cast attack
 * @param y The y position of the spell cast attack
 * @param z The z position of the spell cast attack
 * @param xSpeed The x speed of the spell cast attack
 * @param zSpeed The z speed of the spell cast attack
 */
@OnlyIn(Dist.CLIENT)
class EnariaSpellCast2Particle(
    world: World,
    x: Double,
    y: Double,
    z: Double,
    xSpeed: Double,
    zSpeed: Double
) : AOTDParticle(world, x, y, z, xSpeed, 0.0, zSpeed) {
    init {
        // 2-3 second lifespan
        maxAge = rand.nextInt(20) + 40

        // Make the particles huge when she casts a spell
        particleScale = 0.4f

        // speed will be the same as motion for this particle
        motionX = xSpeed
        motionY = 0.0
        motionZ = zSpeed
    }

    /**
     * Called before the particle is moved, update the motionXYZ here
     */
    override fun updateMotionXYZ() {
        // Slowly increase y motion
        motionY = motionY - 0.02
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
            return EnariaSpellCast2Particle(world, x, y, z, xSpeed, zSpeed).apply {
                selectSpriteRandomly(spriteSet)
            }
        }
    }
}