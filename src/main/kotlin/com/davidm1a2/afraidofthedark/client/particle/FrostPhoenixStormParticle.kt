package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.AOTDParticle
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.SimpleParticleType
import kotlin.math.cos
import kotlin.math.sin

class FrostPhoenixStormParticle(
    world: ClientLevel,
    private val centerX: Double,
    private val centerY: Double,
    private val centerZ: Double,
    private val centerOffset: Double
) : AOTDParticle(world, centerX, centerY, centerZ, 0.0, 0.0, 0.0) {
    private var rotationOffset = random.nextDouble() * 2 * Math.PI
    private var circleSpeed = random.nextDouble() * 0.05 + 0.05

    init {
        // 4-6 second lifespan
        lifetime = random.nextInt(40) + 80
        hasPhysics = false
        alpha = 0.0f

        // Make the particles huge when she casts a spell
        scale(random.nextFloat() * 3f + 2f)

        updatePositionByAge()
    }

    override fun tick() {
        super.tick()
        // Fade in and out
        if (age < 20) {
            alpha = (alpha + 0.05f).coerceAtMost(1f)
        }
        if (lifetime - age < 20) {
            alpha = (alpha - 0.05f).coerceAtMost(1f)
        }
        updatePositionByAge()
    }

    private fun updatePositionByAge() {
        // Circle around the center
        setPos(
            centerX + sin(age * circleSpeed + rotationOffset) * centerOffset,
            centerY,
            centerZ + cos(age * circleSpeed + rotationOffset) * centerOffset
        )
    }

    class Factory(private val spriteSet: SpriteSet) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            particle: SimpleParticleType,
            world: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            centerOffset: Double,
            ignored1: Double,
            ignored2: Double
        ): Particle {
            return FrostPhoenixStormParticle(world, x, y, z, centerOffset).apply {
                pickSprite(spriteSet)
            }
        }
    }
}