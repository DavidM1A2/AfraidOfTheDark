package com.davidm1a2.afraidofthedark.client.particle.base

import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Quaternion
import com.mojang.math.Vector3f
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.util.Mth

abstract class RotatedAOTDParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    internal var rotation: Quaternion = Quaternion.ONE,
    xSpeed: Double = 0.0,
    ySpeed: Double = 0.0,
    zSpeed: Double = 0.0
) : AOTDParticle(world, x, y, z, xSpeed, ySpeed, zSpeed) {
    override fun render(vertexBuilder: VertexConsumer, activeRenderInfo: Camera, partialTicks: Float) {
        render(vertexBuilder, activeRenderInfo, partialTicks, rotation)
    }

    open fun render(vertexBuilder: VertexConsumer, activeRenderInfo: Camera, partialTicks: Float, rotation: Quaternion) {
        // Copy & pasted from super::render except rotation is fixed to a direction

        val vector3d = activeRenderInfo.position
        val x = (Mth.lerp(partialTicks.toDouble(), xo, x) - vector3d.x()).toFloat()
        val y = (Mth.lerp(partialTicks.toDouble(), yo, y) - vector3d.y()).toFloat()
        val z = (Mth.lerp(partialTicks.toDouble(), zo, z) - vector3d.z()).toFloat()
        // Changed this line
        val quaternion = rotation.copy()
        if (roll != 0.0f) {
            val f3 = Mth.lerp(partialTicks, oRoll, roll)
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
}