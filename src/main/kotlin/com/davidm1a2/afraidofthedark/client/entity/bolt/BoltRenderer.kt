package com.davidm1a2.afraidofthedark.client.entity.bolt

import com.davidm1a2.afraidofthedark.common.entity.bolt.BoltEntity
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Matrix3f
import com.mojang.math.Matrix4f
import com.mojang.math.Vector3f
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth

/**
 * Base class for all bolt renderers
 *
 * @constructor Takes a render manager and bolt texture as input
 * @param renderManager The manager given to us by Minecraft
 * @property boltTexture A resource location containing the bolt texture
 */
abstract class BoltRenderer<T : BoltEntity>(renderContext: EntityRendererProvider.Context) : EntityRenderer<T>(renderContext) {
    internal abstract val boltTexture: ResourceLocation

    override fun render(entity: T, entityYaw: Float, partialTicks: Float, matrixStack: PoseStack, multiBufferSource: MultiBufferSource, packedLightIn: Int) {
        ///
        /// Copied from "ArrowRenderer" and adapted for bolts
        ///

        matrixStack.pushPose()
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.yRot) - 90.0f))
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.xRot)))

        val arrowShakeRemaining = entity.shakeTime.toFloat() - partialTicks
        if (arrowShakeRemaining > 0.0f) {
            val currentRotation = -Mth.sin(arrowShakeRemaining * 3.0f) * arrowShakeRemaining
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(currentRotation))
        }

        matrixStack.mulPose(Vector3f.XP.rotationDegrees(45.0f))
        matrixStack.scale(0.05625f, 0.05625f, 0.05625f)
        matrixStack.translate(-4.0, 0.0, 0.0)
        val buffer = multiBufferSource.getBuffer(RenderType.entityCutout(getTextureLocation(entity)))
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
        super.render(entity, entityYaw, partialTicks, matrixStack, multiBufferSource, packedLightIn)
    }

    private fun drawVertex(
        rotationMatrix: Matrix4f,
        normalMatrix: Matrix3f,
        vertexConsumer: VertexConsumer,
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
        vertexConsumer
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
