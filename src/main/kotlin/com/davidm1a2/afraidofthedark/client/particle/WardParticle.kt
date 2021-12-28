package com.davidm1a2.afraidofthedark.client.particle

import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.client.renderer.ActiveRenderInfo
import net.minecraft.client.world.ClientWorld
import net.minecraft.particles.BasicParticleType
import net.minecraft.util.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.vector.Quaternion
import net.minecraft.util.math.vector.Vector3f
import kotlin.math.roundToInt

class WardParticle(
    private val direction: Direction,
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double
) : AOTDParticle(world, x, y, z) {
    init {
        // 1.25 second lifespan
        lifetime = 25
        scale(0.5f)
        quadSize = 0.5f
        // No motion
        xd = 0.0
        yd = 0.0
        zd = 0.0
    }

    override fun render(vertexBuilder: IVertexBuilder, activeRenderInfo: ActiveRenderInfo, partialTicks: Float) {
        // Copy & pasted from super::render except rotation is fixed to a direction

        val vector3d = activeRenderInfo.position
        val x = (MathHelper.lerp(partialTicks.toDouble(), xo, x) - vector3d.x()).toFloat()
        val y = (MathHelper.lerp(partialTicks.toDouble(), yo, y) - vector3d.y()).toFloat()
        val z = (MathHelper.lerp(partialTicks.toDouble(), zo, z) - vector3d.z()).toFloat()
        val quaternion: Quaternion
        if (roll == 0.0f) {
            // Changed this line
            quaternion = direction.rotation.apply { mul(Vector3f.XP.rotationDegrees(90f)) }
        } else {
            // Changed this line
            quaternion = direction.rotation.apply { mul(Vector3f.XP.rotationDegrees(90f)) }
            val f3 = MathHelper.lerp(partialTicks, oRoll, roll)
            quaternion.mul(Vector3f.ZP.rotation(f3))
        }

        val baseRotation = Vector3f(-1.0f, -1.0f, 0.0f)
        baseRotation.transform(quaternion)
        val positions = arrayOf(
            Vector3f(-1.0f, -1.0f, 0.0f),
            Vector3f(-1.0f, 1.0f, 0.0f),
            Vector3f(1.0f, 1.0f, 0.0f),
            Vector3f(1.0f, -1.0f, 0.0f)
        )

        val quadSize = getQuadSize(partialTicks)
        for (i in 0..3) {
            val position = positions[i]
            position.transform(quaternion)
            position.mul(quadSize)
            position.add(x, y, z)
        }

        val lightColor = getLightColor(partialTicks)
        vertexBuilder.vertex(positions[0].x().toDouble(), positions[0].y().toDouble(), positions[0].z().toDouble()).uv(u1, v1).color(rCol, gCol, bCol, alpha).uv2(lightColor).endVertex()
        vertexBuilder.vertex(positions[1].x().toDouble(), positions[1].y().toDouble(), positions[1].z().toDouble()).uv(u1, v0).color(rCol, gCol, bCol, alpha).uv2(lightColor).endVertex()
        vertexBuilder.vertex(positions[2].x().toDouble(), positions[2].y().toDouble(), positions[2].z().toDouble()).uv(u0, v0).color(rCol, gCol, bCol, alpha).uv2(lightColor).endVertex()
        vertexBuilder.vertex(positions[3].x().toDouble(), positions[3].y().toDouble(), positions[3].z().toDouble()).uv(u0, v1).color(rCol, gCol, bCol, alpha).uv2(lightColor).endVertex()
    }

    class Factory(private val spriteSet: IAnimatedSprite) : IParticleFactory<BasicParticleType> {
        override fun createParticle(
            particle: BasicParticleType,
            world: ClientWorld,
            x: Double,
            y: Double,
            z: Double,
            xSpeed: Double,
            ySpeed: Double,
            zSpeed: Double
        ): Particle {
            val direction = xSpeed.roundToInt().coerceIn(0, Direction.values().size - 1)
            return WardParticle(Direction.values()[direction], world, x, y, z).apply {
                pickSprite(spriteSet)
            }
        }
    }
}