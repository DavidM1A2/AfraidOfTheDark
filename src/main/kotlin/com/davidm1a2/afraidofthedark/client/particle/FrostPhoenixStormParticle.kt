package com.davidm1a2.afraidofthedark.client.particle

import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.client.world.ClientWorld
import net.minecraft.particles.BasicParticleType
import kotlin.math.cos
import kotlin.math.sin

class FrostPhoenixStormParticle(
    world: ClientWorld,
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
        scale(random.nextFloat() * 0.6f + 0.4f)

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

    class Factory(private val spriteSet: IAnimatedSprite) : IParticleFactory<BasicParticleType> {
        override fun createParticle(
            particle: BasicParticleType,
            world: ClientWorld,
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