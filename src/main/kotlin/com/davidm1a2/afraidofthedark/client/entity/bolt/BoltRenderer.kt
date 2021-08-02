package com.davidm1a2.afraidofthedark.client.entity.bolt

import com.davidm1a2.afraidofthedark.common.entity.bolt.BoltEntity
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.vector.Matrix3f
import net.minecraft.util.math.vector.Matrix4f
import net.minecraft.util.math.vector.Vector3f

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

        matrixStack.popPose()
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entity.yRotO, entity.yRot) - 90.0f))
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entity.xRotO, entity.xRot)))

        val arrowShakeRemaining = entity.shakeTime.toFloat() - partialTicks
        if (arrowShakeRemaining > 0.0f) {
            val currentRotation = -MathHelper.sin(arrowShakeRemaining * 3.0f) * arrowShakeRemaining
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(currentRotation))
        }

        matrixStack.mulPose(Vector3f.XP.rotationDegrees(45.0f))
        matrixStack.scale(0.05625f, 0.05625f, 0.05625f)
        matrixStack.translate(-4.0, 0.0, 0.0)
        val buffer = iRenderTypeBuffer.getBuffer(RenderType.entityCutout(getTextureLocation(entity)))
        val rotationMatrix = matrixStack.last().pose()
        val normalMatrix = matrixStack.last().normal()

        drawVertex(rotationMatrix, normalMatrix, buffer, -7, -2, -2, 0.0f, 0.15625f, -1, 0, 0, packedLightIn)
        drawVertex(rotationMatrix, normalMatrix, buffer, -7, -2, 2, 0.15625f, 0.15625f, -1, 0, 0, packedLightIn)
        drawVertex(rotationMatrix, normalMatrix, buffer, -7, 2, 2, 0.15625f, 0.3125f, -1, 0, 0, packedLightIn)
        drawVertex(rotationMatrix, normalMatrix, buffer, -7, 2, -2, 0.0f, 0.3125f, -1, 0, 0, packedLightIn)
        drawVertex(rotationMatrix, normalMatrix, buffer, -7, 2, -2, 0.0f, 0.15625f, 1, 0, 0, packedLightIn)
        drawVertex(rotationMatrix, normalMatrix, buffer, -7, 2, 2, 0.15625f, 0.15625f, 1, 0, 0, packedLightIn)
        drawVertex(rotationMatrix, normalMatrix, buffer, -7, -2, 2, 0.15625f, 0.3125f, 1, 0, 0, packedLightIn)
        drawVertex(rotationMatrix, normalMatrix, buffer, -7, -2, -2, 0.0f, 0.3125f, 1, 0, 0, packedLightIn)

        for (ignored in 0..3) {
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(90.0f))
            drawVertex(rotationMatrix, normalMatrix, buffer, -8, -2, 0, 0.0f, 0.0f, 0, 1, 0, packedLightIn)
            drawVertex(rotationMatrix, normalMatrix, buffer, 8, -2, 0, 0.5f, 0.0f, 0, 1, 0, packedLightIn)
            drawVertex(rotationMatrix, normalMatrix, buffer, 8, 2, 0, 0.5f, 0.15625f, 0, 1, 0, packedLightIn)
            drawVertex(rotationMatrix, normalMatrix, buffer, -8, 2, 0, 0.0f, 0.15625f, 0, 1, 0, packedLightIn)
        }

        matrixStack.popPose()
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
            .vertex(rotationMatrix, x.toFloat(), y.toFloat(), z.toFloat())
            .color(255, 255, 255, 255)
            .uv(u, v)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(packedLightIn)
            .normal(normalMatrix, normalX.toFloat(), normalY.toFloat(), normalZ.toFloat())
            .endVertex()
    }

    /**
     * Returns the location of an entity's texture.
     *
     * @param entity The entity to get the texture for
     * @return The resource location representing the entity texture
     */
    override fun getTextureLocation(entity: T): ResourceLocation {
        return boltTexture
    }
}
