package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.AOTDParticle
import com.davidm1a2.afraidofthedark.common.particle.FeyParticleData
import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.IParticleRenderType
import net.minecraft.client.particle.Particle
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.MathHelper

class ProjectileParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    xSpeed: Double,
    ySpeed: Double,
    zSpeed: Double,
    private val scale: Float,
    red: Float,
    green: Float,
    blue: Float
) : AOTDParticle(world, x, y, z) {
    private val baseQuadSize: Float

    init {
        // 0.5 second lifespan
        lifetime = 10

        scale(scale)
        baseQuadSize = quadSize

        xd = xSpeed
        yd = ySpeed
        zd = zSpeed

        rCol = red
        gCol = green
        bCol = blue
    }

    override fun tick() {
        super.tick()

        // Shrink the particle over time
        val newScale = MathHelper.lerp(age.toFloat() / lifetime, scale, scale / 2)
        // For whatever reason "scale" does quadSize *= newScale, so reset it to avoid exponential quad size growth
        scale(newScale)
        quadSize = baseQuadSize * newScale
    }

    override fun updateMotionXYZ() {
        setAlphaFadeInLastTicks(5f)
        xd = xd * 0.99
        yd = yd - 0.01
        zd = zd * 0.99
    }

    override fun getRenderType(): IParticleRenderType {
        return PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH_MASK
    }

    class Factory(private val spriteSet: IAnimatedSprite) : IParticleFactory<FeyParticleData> {
        override fun createParticle(
            particle: FeyParticleData,
            world: ClientWorld,
            x: Double,
            y: Double,
            z: Double,
            xSpeed: Double,
            ySpeed: Double,
            zSpeed: Double
        ): Particle {
            return ProjectileParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, particle.scale, particle.red, particle.green, particle.blue).apply {
                pickSprite(spriteSet)
            }
        }
    }
}