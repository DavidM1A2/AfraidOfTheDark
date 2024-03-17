package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.DelayedAOTDParticle
import com.davidm1a2.afraidofthedark.common.particle.SelfParticleData
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class SelfParticle(
    world: ClientLevel,
    private val startX: Double,
    private val startY: Double,
    private val startZ: Double,
    entityId: Int,
    private val offsetDegrees: Float
) : DelayedAOTDParticle(world, startX, startY, startZ, fadeTicks = 5, delayTicks = offsetDegrees.roundToInt() / 20 + 5) {
    private val entity = world.getEntity(entityId)
    private val width = entity?.bbWidth ?: 1f
    private val height = entity?.bbHeight?.div(2) ?: 0f

    init {
        // 1 second lifespan
        lifetime = 20 + offsetDegrees.roundToInt() / 20
        scale(entity?.bbWidth?.times(2) ?: 1f)
    }

    override fun updateMotionXYZ() {
        super.updateMotionXYZ()
        val centerX = entity?.x ?: startX
        val centerY = entity?.y ?: startY
        val centerZ = entity?.z ?: startZ
        setPos(
            centerX + sin(Math.toRadians(offsetDegrees + age * 5.0)) * width / 2 * 1.5,
            centerY + height,
            centerZ + cos(Math.toRadians(offsetDegrees + age * 5.0)) * width / 2 * 1.5
        )
    }

    class Factory(private val spriteSet: SpriteSet) : ParticleProvider<SelfParticleData> {
        override fun createParticle(
            particle: SelfParticleData,
            world: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            xSpeed: Double,
            ySpeed: Double,
            zSpeed: Double
        ): Particle {
            return SelfParticle(world, x, y, z, particle.entityId, particle.offsetDegrees).apply {
                pickSprite(spriteSet)
            }
        }
    }
}