package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.common.particle.FeedParticleData
import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.MathHelper
import kotlin.math.cos
import kotlin.math.sin

class FeedParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    entityId: Int,
    private val offsetDegrees: Float,
    private val radius: Float
) : AOTDParticle(world, x, y, z) {
    private val entity = world.getEntity(entityId)
    private val baseX = x
    private val baseY = y
    private val baseZ = z
    private val height = entity?.bbHeight ?: 1f

    init {
        scale(1.6f)

        // 1 second lifespan
        lifetime = 20

        xd = 0.0
        yd = 0.0
        zd = 0.0

        alpha = 0f
    }

    override fun updateMotionXYZ() {
        val centerX = entity?.x ?: baseX
        val centerY = entity?.y ?: baseY
        val centerZ = entity?.z ?: baseZ
        val newX = centerX + sin(offsetDegrees) * MathHelper.lerp(age / lifetime.toFloat(), 2.5f * radius, 0f)
        val newY = centerY + MathHelper.lerp(age / lifetime.toFloat(), 0f, height * 0.6f)
        val newZ = centerZ + cos(offsetDegrees) * MathHelper.lerp(age / lifetime.toFloat(), 2.5f * radius, 0f)
        setPos(newX, newY, newZ)
        setAlphaFadeInLastTicks(5f)
        if (age < 15) {
            alpha = alpha * age / 15
        }
    }

    class Factory(private val spriteSet: IAnimatedSprite) : IParticleFactory<FeedParticleData> {
        override fun createParticle(
            particle: FeedParticleData,
            world: ClientWorld,
            x: Double,
            y: Double,
            z: Double,
            xSpeed: Double,
            ySpeed: Double,
            zSpeed: Double
        ): Particle {
            return FeedParticle(world, x, y, z, particle.entityId, particle.offsetDegrees, particle.radius).apply {
                pickSprite(spriteSet)
            }
        }
    }
}