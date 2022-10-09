package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.common.particle.HealParticleData
import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.client.world.ClientWorld
import kotlin.math.cos
import kotlin.math.sin

class HealParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    entityId: Int,
    private val offsetDegrees: Float
) : AOTDParticle(world, x, y, z) {
    private val entity = world.getEntity(entityId)
    private val width = entity?.bbWidth ?: 1f
    private val height = entity?.bbHeight ?: 1f
    private val spawnDelayTicks = random.nextInt(17)
    private val startX = x
    private val startY = y
    private val startZ = z
    private var offsetX = 0.0
    private var offsetY = 0.0
    private var offsetZ = 0.0
    private val targetVelocityX = (random.nextFloat() - 0.5) * 0.02
    private val targetVelocityY = random.nextFloat() * (height / 25) + height / 25
    private val targetVelocityZ = (random.nextFloat() - 0.5) * 0.02
    private val stickiness = random.nextDouble() * 5 + 1.0

    init {
        // 0.5-1.0 second lifespan
        lifetime = spawnDelayTicks + FADE_TICKS + random.nextInt(10) + 10
        // Start invis
        alpha = 0f
    }

    override fun tick() {
        super.tick()
        if (isDelaying()) {
            return
        }
        if (age <= spawnDelayTicks + FADE_TICKS) {
            alpha = (age - spawnDelayTicks).toFloat() / FADE_TICKS
        } else {
            setAlphaFadeInLastTicks(FADE_TICKS.toFloat())
        }
    }

    override fun updateMotionXYZ() {
        val centerX = entity?.x ?: startX
        val centerY = entity?.y ?: startY
        val centerZ = entity?.z ?: startZ
        val targetX = centerX + offsetX + sin(Math.toRadians(offsetDegrees.toDouble())) * width / 2 * 1.5
        val targetY = centerY + offsetY
        val targetZ = centerZ + offsetZ + cos(Math.toRadians(offsetDegrees.toDouble())) * width / 2 * 1.5
        if (age == spawnDelayTicks) {
            setPos(targetX, targetY, targetZ)
        }
        if (!isDelaying()) {
            offsetX = offsetX + targetVelocityX
            offsetY = offsetY + targetVelocityY
            offsetZ = offsetZ + targetVelocityZ
            xd = (targetX - x) / stickiness
            yd = (targetY - y) / stickiness
            zd = (targetZ - z) / stickiness
        }
    }

    private fun isDelaying(): Boolean {
        return age <= spawnDelayTicks
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

    companion object {
        private const val FADE_TICKS = 3
    }
}