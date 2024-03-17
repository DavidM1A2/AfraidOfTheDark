package com.davidm1a2.afraidofthedark.client.entity.splinterDrone

import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.SplinterDroneProjectileEntity
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.ResourceLocation

/**
 * Renderer class for the splinter drone projectile entity
 *
 * @constructor just passes down fields and the render manager
 * @param renderManager The render manager to pass down
 */
class SplinterDroneProjectileRenderer(renderManager: EntityRendererProvider.Context) :
    EntityRenderer<SplinterDroneProjectileEntity>(renderManager) {
    override fun render(
        splinterDroneProjectile: SplinterDroneProjectileEntity,
        entityYaw: Float,
        partialTicks: Float,
        matrixStack: PoseStack,
        renderTypeBuffer: MultiBufferSource,
        packedLight: Int
    ) {
        splinterDroneProjectile.getAnimationHandler().update()
        SPLINTER_DRONE_PROJECTILE_MODEL.setupAnim(splinterDroneProjectile, 0f, 0f, 0f, 0f, 0f)

        SPLINTER_DRONE_PROJECTILE_MODEL.renderToBuffer(
            matrixStack,
            renderTypeBuffer.getBuffer(RENDER_TYPE),
            packedLight,
            OverlayTexture.NO_OVERLAY,
            1.0f,
            1.0f,
            1.0f,
            1.0f
        )
    }

    override fun getTextureLocation(entity: SplinterDroneProjectileEntity): ResourceLocation {
        return SPLINTER_DRONE_PROJECTILE_TEXTURE
    }

    companion object {
        // The texture used by the model
        private val SPLINTER_DRONE_PROJECTILE_TEXTURE =
            ResourceLocation("afraidofthedark:textures/entity/splinter_drone_projectile.png")

        // The splinter drone projectile model
        private val SPLINTER_DRONE_PROJECTILE_MODEL = SplinterDroneProjectileModel()

        private val RENDER_TYPE = RenderType.entityCutoutNoCull(SPLINTER_DRONE_PROJECTILE_TEXTURE)
    }
}
