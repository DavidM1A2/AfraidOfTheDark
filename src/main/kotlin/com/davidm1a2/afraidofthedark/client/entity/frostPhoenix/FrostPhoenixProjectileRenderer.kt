package com.davidm1a2.afraidofthedark.client.entity.frostPhoenix

import com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.FrostPhoenixProjectileEntity
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.ResourceLocation

/**
 * Renderer class for the frost phoenix projectile entity
 *
 * @constructor just passes down fields and the render manager
 * @param renderManager The render manager to pass down
 */
class FrostPhoenixProjectileRenderer(renderManager: EntityRendererProvider.Context) :
    EntityRenderer<FrostPhoenixProjectileEntity>(renderManager) {
    override fun render(
        entity: FrostPhoenixProjectileEntity,
        entityYaw: Float,
        partialTicks: Float,
        matrixStack: PoseStack,
        renderTypeBuffer: MultiBufferSource,
        packedLight: Int
    ) {
        entity.getAnimationHandler().update()
        FROST_PHOENIX_PROJECTILE_MODEL.setupAnim(entity, 0f, 0f, 0f, 0f, 0f)

        matrixStack.pushPose()
        matrixStack.translate(0.0, entity.boundingBox.ysize / 2, 0.0)
        matrixStack.scale(3f, 3f, 3f)
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
