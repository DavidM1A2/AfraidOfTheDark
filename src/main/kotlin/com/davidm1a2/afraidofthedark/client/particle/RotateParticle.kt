package com.davidm1a2.afraidofthedark.client.particle

import com.davidm1a2.afraidofthedark.client.particle.base.RotatedAOTDParticle
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.computeRotationTo
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Quaternion
import com.mojang.math.Vector3f
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.world.phys.Vec3

class RotateParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    xDir: Double,
    yDir: Double,
    zDir: Double
) : RotatedAOTDParticle(world, x, y, z) {
    private val baseRotation = BASE_DIRECTION.computeRotationTo(Vec3(xDir, yDir, zDir))
    private var ninetyDegreeRotatedRotation = Quaternion.ONE

    init {
        // 0.5 second lifespan
        lifetime = 10
        scale(1f)
        // No movement
        xd = 0.0
        yd = 0.0
        zd = 0.0
    }

    override fun render(vertexBuilder: VertexConsumer, activeRenderInfo: Camera, partialTicks: Float) {
        // Render the particle twice, once at the standard rotation, and once 90 degrees more rotated.
        super.render(vertexBuilder, activeRenderInfo, partialTicks)
        render(vertexBuilder, activeRenderInfo, partialTicks, ninetyDegreeRotatedRotation)
    }

    override fun getRenderType(): ParticleRenderType {
        return PARTICLE_SHEET_TRANSLUCENT_NO_CULL
    }

    override fun updateMotionXYZ() {
        setAlphaFadeInLastTicks(2f)
        val newRotation = baseRotation.copy()
        newRotation.mul(Vector3f.XP.rotationDegrees(age.toFloat() * 10f))
        rotation = newRotation

        ninetyDegreeRotatedRotation = newRotation.copy()
        ninetyDegreeRotatedRotation.mul(Vector3f.XP.rotationDegrees(90f))
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
            return RotateParticle(world, x, y, z, xSpeed, ySpeed, zSpeed).apply {
                pickSprite(spriteSet)
            }
        }
    }

    companion object {
        private val BASE_DIRECTION = Vec3(1.0, 0.0, 0.0)
    }
}