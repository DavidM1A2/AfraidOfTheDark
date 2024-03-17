package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.DelayedAOTDParticle
import com.davidm1a2.afraidofthedark.common.particle.ArrowTrailParticleData
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet

class ArrowTrailParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    entityId: Int,
    delayTicks: Int
) : DelayedAOTDParticle(world, x, y, z, delayTicks = delayTicks, fadeTicks = 2) {
    private val entity = world.getEntity(entityId)
    private val baseX = x
    private val baseY = y
    private val baseZ = z

    init {
        scale(1.2f)

        // 0.4 second lifespan
        setLifetime(8)

        xd = 0.0
        yd = 0.0
        zd = 0.0
    }

    override fun onDelayOver() {
        val centerX = entity?.x ?: baseX
        val centerY = entity?.y ?: baseY
        val centerZ = entity?.z ?: baseZ
        setPos(centerX, centerY, centerZ)
    }

    class Factory(private val spriteSet: SpriteSet) : ParticleProvider<ArrowTrailParticleData> {
        override fun createParticle(
            particle: ArrowTrailParticleData,
            world: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            xSpeed: Double,
            ySpeed: Double,
            zSpeed: Double
        ): Particle {
            return ArrowTrailParticle(world, x, y, z, particle.entityId, particle.delayTicks).apply {
                pickSprite(spriteSet)
            }
        }
    }
}