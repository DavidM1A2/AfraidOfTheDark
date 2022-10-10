package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.DelayedAOTDParticle
import com.davidm1a2.afraidofthedark.common.particle.FlyParticleData
import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.client.world.ClientWorld
import kotlin.math.PI
import kotlin.math.sin

class FlyParticle(
    world: ClientWorld,
    private val startX: Double,
    private val startY: Double,
    private val startZ: Double,
    xSpeed: Double,
    ySpeed: Double,
    zSpeed: Double,
    entityId: Int,
    delayTicks: Int
) : DelayedAOTDParticle(world, startX, startY, startZ, xSpeed, ySpeed, zSpeed, delayTicks, 8) {
    private val entity = world.getEntity(entityId)
    private val sinOffset1 = random.nextDouble() * PI * 2
    private val sinOffset2 = random.nextDouble() * PI * 2
    private val sideSpeed = random.nextDouble() * 0.05 + 0.15
    private val sideDistance = random.nextDouble() * 0.05 + 0.05
    private val dropSpeed = random.nextDouble() * 0.004 + 0.002

    init {
        // 2-3 second lifespan
        setLifetime(40 + random.nextInt(20))
        setPos(entity?.x ?: startX, entity?.y ?: startY, entity?.z ?: startZ)
    }

    override fun onDelayOver() {
        setPos(entity?.x ?: startX, entity?.y ?: startY, entity?.z ?: startZ)
        if (entity?.isOnGround == true) {
            remove()
        }
    }

    override fun tickPostDelay() {
        super.tickPostDelay()
        xd = sin(age * sideSpeed + sinOffset1) * sideDistance
        yd = (yd - dropSpeed).coerceAtMost(0.1)
        zd = sin(age * sideSpeed + sinOffset2) * sideDistance
    }

    class Factory(private val spriteSet: IAnimatedSprite) : IParticleFactory<FlyParticleData> {
        override fun createParticle(
            particle: FlyParticleData,
            world: ClientWorld,
            x: Double,
            y: Double,
            z: Double,
            xSpeed: Double,
            ySpeed: Double,
            zSpeed: Double
        ): Particle {
            return FlyParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, particle.entityId, particle.delayTicks).apply {
                pickSprite(spriteSet)
            }
        }
    }
}