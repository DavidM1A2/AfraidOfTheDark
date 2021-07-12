package com.davidm1a2.afraidofthedark.client.entity.bolt

import com.davidm1a2.afraidofthedark.common.entity.bolt.BoltEntity
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.Matrix3f
import net.minecraft.client.renderer.Matrix4f
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.Vector3f
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.MathHelper

/**
 * Base class for all bolt renderers
 *
 * @constructor Takes a render manager and bolt texture as input
 * @param renderManager The manager given to us by Minecraft
 * @property boltTexture A resource location containing the bolt texture
 */
abstract class BoltRenderer<T : BoltEntity>(renderManager: EntityRendererManager) : EntityRenderer<T>(renderManager) {
    internal abstract val boltTexture: ResourceLocation

    override fun render(entity: T, entityYaw: Float, partialTicks: Float, matrixStack: MatrixStack, iRenderTypeBuffer: IRenderTypeBuffer, packedLightIn: Int) {
        ///
        /// Copied from "ArrowRenderer" and adapted for bolts
        ///

        matrixStack.push()
        matrixStack.rotate(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entity.prevRotationYaw, entity.rotationYaw) - 90.0f))
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch)))

        val arrowShakeRemaining = entity.arrowShake.toFloat() - partialTicks
        if (arrowShakeRemaining > 0.0f) {
            val currentRotation = -MathHelper.sin(arrowShakeRemaining * 3.0f) * arrowShakeRemaining
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(currentRotation))
        }

        matrixStack.rotate(Vector3f.XP.rotationDegrees(45.0f))
        matrixStack.scale(0.05625f, 0.05625f, 0.05625f)
        matrixStack.translate(-4.0, 0.0, 0.0)
        val buffer = iRenderTypeBuffer.getBuffer(RenderType.getEntityCutout(getEntityTexture(entity)))
        val rotationMatrix = matrixStack.last.matrix
        val normalMatrix = matrixStack.last.normal

        drawVertex(rotationMatrix, normalMatrix, buffer, -7, -2, -2, 0.0f, 0.15625f, -1, 0, 0, packedLightIn)
        drawVertex(rotationMatrix, normalMatrix, buffer, -7, -2, 2, 0.15625f, 0.15625f, -1, 0, 0, packedLightIn)
        drawVertex(rotationMatrix, normalMatrix, buffer, -7, 2, 2, 0.15625f, 0.3125f, -1, 0, 0, packedLightIn)
        drawVertex(rotationMatrix, normalMatrix, buffer, -7, 2, -2, 0.0f, 0.3125f, -1, 0, 0, packedLightIn)
        drawVertex(rotationMatrix, normalMatrix, buffer, -7, 2, -2, 0.0f, 0.15625f, 1, 0, 0, packedLightIn)
        drawVertex(rotationMatrix, normalMatrix, buffer, -7, 2, 2, 0.15625f, 0.15625f, 1, 0, 0, packedLightIn)
        drawVertex(rotationMatrix, normalMatrix, buffer, -7, -2, 2, 0.15625f, 0.3125f, 1, 0, 0, packedLightIn)
        drawVertex(rotationMatrix, normalMatrix, buffer, -7, -2, -2, 0.0f, 0.3125f, 1, 0, 0, packedLightIn)

        for (ignored in 0..3) {
            matrixStack.rotate(Vector3f.XP.rotationDegrees(90.0f))
            drawVertex(rotationMatrix, normalMatrix, buffer, -8, -2, 0, 0.0f, 0.0f, 0, 1, 0, packedLightIn)
            drawVertex(rotationMatrix, normalMatrix, buffer, 8, -2, 0, 0.5f, 0.0f, 0, 1, 0, packedLightIn)
            drawVertex(rotationMatrix, normalMatrix, buffer, 8, 2, 0, 0.5f, 0.15625f, 0, 1, 0, packedLightIn)
            drawVertex(rotationMatrix, normalMatrix, buffer, -8, 2, 0, 0.0f, 0.15625f, 0, 1, 0, packedLightIn)
        }

        matrixStack.pop()
        super.render(entity, entityYaw, partialTicks, matrixStack, iRenderTypeBuffer, packedLightIn)
    }

    private fun drawVertex(
        rotationMatrix: Matrix4f,
        normalMatrix: Matrix3f,
        vertexBuilder: IVertexBuilder,
        x: Int,
        y: Int,
        z: Int,
        u: Float,
        v: Float,
        normalX: Int,
        normalZ: Int,
        normalY: Int,
        packedLightIn: Int
    ) {
        vertexBuilder
            .pos(rotationMatrix, x.toFloat(), y.toFloat(), z.toFloat())
            .color(255, 255, 255, 255)
            .tex(u, v)
            .overlay(OverlayTexture.NO_OVERLAY)
            .lightmap(packedLightIn)
            .normal(normalMatrix, normalX.toFloat(), normalY.toFloat(), normalZ.toFloat())
            .endVertex()
    }

    /**
     * Returns the location of an entity's texture.
     *
     * @param entity The entity to get the texture for
     * @return The resource location representing the entity texture
     */
    override fun getEntityTexture(entity: T): ResourceLocation {
        return boltTexture
    }
}
