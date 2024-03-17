package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.AOTDParticle
import com.davidm1a2.afraidofthedark.common.particle.CleanseParticleData
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import kotlin.math.cos
import kotlin.math.sin

class CleanseParticle(
    world: ClientLevel,
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
        scale(1.2f)

        // 1 second lifespan
        lifetime = 20

        xd = 0.0
        yd = 0.0
        zd = 0.0
    }

    override fun updateMotionXYZ() {
        val centerX = entity?.x ?: baseX
        val centerY = entity?.y ?: baseY
        val centerZ = entity?.z ?: baseZ
        val newX = centerX + radius * sin(Math.toRadians(offsetDegrees.toDouble()) + age * 0.2)
        val newY = centerY + (age.toDouble() / lifetime) * height
        val newZ = centerZ + radius * cos(Math.toRadians(offsetDegrees.toDouble()) + age * 0.2)
        setPos(newX, newY, newZ)
        setAlphaFadeInLastTicks(14f)
    }

    class Factory(private val spriteSet: SpriteSet) : ParticleProvider<CleanseParticleData> {
        override fun createParticle(
            particle: CleanseParticleData,
            world: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            xSpeed: Double,
            ySpeed: Double,
            zSpeed: Double
        ): Particle {
            return CleanseParticle(world, x, y, z, particle.entityId, particle.offsetDegrees, particle.radius).apply {
                pickSprite(spriteSet)
            }
        }
    }
}