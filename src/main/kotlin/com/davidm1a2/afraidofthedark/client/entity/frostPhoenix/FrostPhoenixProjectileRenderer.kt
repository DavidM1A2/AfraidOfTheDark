package com.davidm1a2.afraidofthedark.client.entity.frostPhoenix

import com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.FrostPhoenixProjectileEntity
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.util.ResourceLocation

/**
 * Renderer class for the frost phoenix projectile entity
 *
 * @constructor just passes down fields and the render manager
 * @param renderManager The render manager to pass down
 */
class FrostPhoenixProjectileRenderer(renderManager: EntityRendererManager) :
    EntityRenderer<FrostPhoenixProjectileEntity>(renderManager) {
    override fun render(
        entity: FrostPhoenixProjectileEntity,
        entityYaw: Float,
        partialTicks: Float,
        matrixStack: MatrixStack,
        renderTypeBuffer: IRenderTypeBuffer,
        packedLight: Int
    ) {
        entity.getAnimationHandler().update()
        FROST_PHOENIX_PROJECTILE_MODEL.setupAnim(entity, 0f, 0f, 0f, 0f, 0f)

        matrixStack.pushPose()
        matrixStack.translate(0.0, entity.boundingBox.ysize / 2, 0.0)
        FROST_PHOENIX_PROJECTILE_MODEL.renderToBuffer(
            matrixStack,
            renderTypeBuffer.getBuffer(RENDER_TYPE),
            packedLight,
            OverlayTexture.NO_OVERLAY,
            1.0f,
            1.0f,
            1.0f,
            1.0f
        )
        matrixStack.popPose()
    }

    override fun getTextureLocation(entity: FrostPhoenixProjectileEntity): ResourceLocation {
        return FROST_PHOENIX_PROJECTILE_TEXTURE
    }

    companion object {
        // The texture used by the model
        private val FROST_PHOENIX_PROJECTILE_TEXTURE =
            ResourceLocation("afraidofthedark:textures/entity/frost_phoenix_projectile.png")

        // The frost phoenix projectile model
        private val FROST_PHOENIX_PROJECTILE_MODEL = FrostPhoenixProjectileModel()

        private val RENDER_TYPE = RenderType.entityCutoutNoCull(FROST_PHOENIX_PROJECTILE_TEXTURE)
    }
}
