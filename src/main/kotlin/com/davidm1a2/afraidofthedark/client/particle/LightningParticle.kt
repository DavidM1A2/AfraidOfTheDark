package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.AOTDParticle
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.renderer.LightTexture
import net.minecraft.core.particles.SimpleParticleType

class LightningParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double
) : AOTDParticle(world, x, y, z) {
    private var bounceCount = 0

    init {
        scale(0.8f)
        // 10 second lifespan, or 3 bounces
        lifetime = 10 * 20
        // Random outwards motion
        xd = (random.nextDouble() - 0.5) * 0.4
        yd = random.nextDouble() + 0.2
        zd = (random.nextDouble() - 0.5) * 0.4
    }

    override fun updateMotionXYZ() {
        super.updateMotionXYZ()

        if (onGround) {
            xd = xd * (1 + (random.nextFloat() - 0.5) * 0.2)
            yd = -yd * 0.4
            zd = zd * (1 + (random.nextFloat() - 0.5) * 0.2)
            bounceCount = bounceCount + 1
        } else {
            yd -= 0.14
        }

        if (bounceCount == 4) {
            remove()
        }
    }

    override fun getLightColor(partialTicks: Float): Int {
        return FULLBRIGHT
    }

    class Factory(private val spriteSet: SpriteSet) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            particle: SimpleParticleType,
            world: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            xSpeed: Double,
            ySpeed: Double,
            zSpeed: Double
        ): Particle {
            return LightningParticle(world, x, y, z).apply {
                pickSprite(spriteSet)
            }
        }
    }

    companion object {
        private val FULLBRIGHT = LightTexture.pack(15, 15)
    }
}