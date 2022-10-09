package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.DelayedAOTDParticle
import com.davidm1a2.afraidofthedark.common.particle.HealParticleData
import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.client.world.ClientWorld
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class HealParticle(
    world: ClientWorld,
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
        val centerX = entity?.x ?: startX
        val centerY = entity?.y ?: startY
        val centerZ = entity?.z ?: startZ
        val targetX = centerX + targetOffsetX + sin(Math.toRadians(offsetDegrees.toDouble())) * width / 2 * 1.5
        val targetY = centerY + targetOffsetY
        val targetZ = centerZ + targetOffsetZ + cos(Math.toRadians(offsetDegrees.toDouble())) * width / 2 * 1.5
        setPos(targetX, targetY, targetZ)
    }

    override fun tickPostDelay() {
        super.tickPostDelay()
        val centerX = entity?.x ?: startX
        val centerY = entity?.y ?: startY
        val centerZ = entity?.z ?: startZ
        val targetX = centerX + targetOffsetX + sin(Math.toRadians(offsetDegrees.toDouble())) * width / 2 * 1.5
        val targetY = centerY + targetOffsetY
        val targetZ = centerZ + targetOffsetZ + cos(Math.toRadians(offsetDegrees.toDouble())) * width / 2 * 1.5
        targetOffsetX = targetOffsetX + targetVelocityX
        targetOffsetY = targetOffsetY + targetVelocityY
        targetOffsetZ = targetOffsetZ + targetVelocityZ
        xd = (targetX - x) / stickiness
        yd = (targetY - y) / stickiness
        zd = (targetZ - z) / stickiness
    }

    class Factory(private val spriteSet: IAnimatedSprite) : IParticleFactory<HealParticleData> {
        override fun createParticle(
            particle: HealParticleData,
            world: ClientWorld,
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