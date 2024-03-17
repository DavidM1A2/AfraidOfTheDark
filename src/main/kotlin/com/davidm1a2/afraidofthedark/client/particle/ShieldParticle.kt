package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.RotatedAOTDParticle
import com.davidm1a2.afraidofthedark.common.particle.ShieldParticleData
import com.mojang.math.Vector3f
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.particle.SpriteSet
import kotlin.math.cos
import kotlin.math.sin

class ShieldParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    entityId: Int,
    duration: Int,
    private val offsetDegrees: Float,
    private val radius: Float
) : RotatedAOTDParticle(world, x, y, z) {
    private val entity = world.getEntity(entityId)

    init {
        lifetime = duration
        scale(radius * 2)
        // No motion
        xd = 0.0
        yd = 0.0
        zd = 0.0
    }

    override fun getRenderType(): ParticleRenderType {
        return PARTICLE_SHEET_TRANSLUCENT_NO_CULL
    }

    override fun tick() {
        super.tick()
        if (entity?.isAlive == false) {
            remove()
        }
    }

    override fun updateMotionXYZ() {
        if (entity != null) {
            val centerX = entity.x
            val centerY = entity.y
            val centerZ = entity.z
            val newX = centerX + radius * sin(Math.toRadians(offsetDegrees.toDouble()) + age * SPIN_SPEED)
            val newY = centerY + entity.bbHeight / 2
            val newZ = centerZ + radius * cos(Math.toRadians(offsetDegrees.toDouble()) + age * SPIN_SPEED)
            setPos(newX, newY, newZ)
            val fade = FADE_MAX.coerceAtMost(lifetime / 2f)
            setAlphaFadeInLastTicks(fade)
            if (age <= fade) {
                alpha = alpha * age / fade
            }
            rotation = Vector3f.YP.rotationDegrees(offsetDegrees - 180f + age * Math.toDegrees(SPIN_SPEED).toFloat())
        }
    }

    class Factory(private val spriteSet: SpriteSet) : ParticleProvider<ShieldParticleData> {
        override fun createParticle(
            particle: ShieldParticleData,
            world: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            xSpeed: Double,
            ySpeed: Double,
            zSpeed: Double
        ): Particle {
            return ShieldParticle(world, x, y, z, particle.entityId, particle.duration, particle.offsetDegrees, particle.radius).apply {
                pickSprite(spriteSet)
            }
        }
    }

    companion object {
        private const val SPIN_SPEED = 0.15
        private const val FADE_MAX = 10f
    }
}