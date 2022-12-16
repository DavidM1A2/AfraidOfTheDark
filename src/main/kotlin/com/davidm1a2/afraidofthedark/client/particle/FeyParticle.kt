package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.AOTDParticle
import com.davidm1a2.afraidofthedark.common.particle.FeyParticleData
import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.IParticleRenderType
import net.minecraft.client.particle.Particle
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.vector.Vector3d
import kotlin.math.cos
import kotlin.math.sin

class FeyParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    offsetDegrees: Float,
    red: Float,
    green: Float,
    blue: Float
) : AOTDParticle(world, x, y, z) {
    init {
        lifetime = 20 + random.nextInt(10)

        scale(random.nextFloat() * 0.5f + 0.5f)

        val startOffsetDistance = random.nextDouble() * 0.2 + 0.3
        val xOffset = sin(Math.toRadians(offsetDegrees.toDouble())) * startOffsetDistance
        val zOffset = cos(Math.toRadians(offsetDegrees.toDouble())) * startOffsetDistance
        setPos(x + xOffset, y, z + zOffset)
        xo = x + xOffset
        yo = y
        zo = z + zOffset

        val motionDir = Vector3d(xOffset, 0.0, zOffset)
            .vectorTo(Vector3d(0.0, 0.0, 0.0))
            .normalize()
            .scale(0.06)

        xd = motionDir.x
        yd = 0.01
        zd = motionDir.z

        rCol = red
        gCol = green
        bCol = blue
    }

    override fun getRenderType(): IParticleRenderType {
        return PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH_MASK
    }

    override fun tick() {
        super.tick()
        setAlphaFadeInLastTicks(10f)
    }

    override fun updateMotionXYZ() {
        xd = xd * 0.9
        yd = yd * 1.1
        zd = zd * 0.9
    }

    class Factory(private val spriteSet: IAnimatedSprite) : IParticleFactory<FeyParticleData> {
        override fun createParticle(
            particle: FeyParticleData,
            world: ClientWorld,
            x: Double,
            y: Double,
            z: Double,
            xSpeed: Double,
            ySpeed: Double,
            zSpeed: Double
        ): Particle {
            return FeyParticle(world, x, y, z, particle.offsetDegrees, particle.red, particle.green, particle.blue).apply {
                pickSprite(spriteSet)
            }
        }
    }
}