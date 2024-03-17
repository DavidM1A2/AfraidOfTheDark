package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.DelayedAOTDParticle
import com.davidm1a2.afraidofthedark.common.particle.HealParticleData
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.world.phys.Vec3
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class HealParticle(
    world: ClientLevel,
    private val startX: Double,
    private val startY: Double,
    private val startZ: Double,
    entityId: Int,
    private val offsetDegrees: Float
) : DelayedAOTDParticle(world, startX, startY, startZ, delayTicks = Random.nextInt(17), fadeTicks = 3) {
    private val entity = world.getEntity(entityId)
    private val width = entity?.bbWidth ?: 1f
    private val height = entity?.bbHeight ?: 1f
    private var targetOffsetX = 0.0
    private var targetOffsetY = 0.0
    private var targetOffsetZ = 0.0
    private val targetVelocityX = (random.nextFloat() - 0.5) * 0.02
    private val targetVelocityY = random.nextFloat() * (height / 25) + height / 25
    private val targetVelocityZ = (random.nextFloat() - 0.5) * 0.02
    private val stickiness = random.nextDouble() * 5 + 1.0

    init {
        // 0.5-1.0 second lifespan
        setLifetime(random.nextInt(10) + 10)
        // Start invis
        alpha = 0f
    }

    override fun onDelayOver() {
        val targetPosition = computeTargetPosition()
        setPos(targetPosition.x, targetPosition.y, targetPosition.z)
    }

    override fun tickPostDelay() {
        super.tickPostDelay()
        val targetPosition = computeTargetPosition()
        targetOffsetX = targetOffsetX + targetVelocityX
        targetOffsetY = targetOffsetY + targetVelocityY
        targetOffsetZ = targetOffsetZ + targetVelocityZ
        xd = (targetPosition.x - x) / stickiness
        yd = (targetPosition.y - y) / stickiness
        zd = (targetPosition.z - z) / stickiness
    }

    private fun computeTargetPosition(): Vec3 {
        val centerX = entity?.x ?: startX
        val centerY = entity?.y ?: startY
        val centerZ = entity?.z ?: startZ
        val targetX = centerX + targetOffsetX + sin(Math.toRadians(offsetDegrees.toDouble())) * width / 2 * 1.5
        val targetY = centerY + targetOffsetY
        val targetZ = centerZ + targetOffsetZ + cos(Math.toRadians(offsetDegrees.toDouble())) * width / 2 * 1.5
        return Vec3(targetX, targetY, targetZ)
    }

    class Factory(private val spriteSet: SpriteSet) : ParticleProvider<HealParticleData> {
        override fun createParticle(
            particle: HealParticleData,
            world: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            xSpeed: Double,
            ySpeed: Double,
            zSpeed: Double
        ): Particle {
            return HealParticle(world, x, y, z, particle.entityId, particle.offsetDegrees).apply {
                pickSprite(spriteSet)
            }
        }
    }
}