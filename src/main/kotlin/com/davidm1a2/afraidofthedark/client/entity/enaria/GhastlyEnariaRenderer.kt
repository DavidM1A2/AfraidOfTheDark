package com.davidm1a2.afraidofthedark.client.entity.enaria

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.entity.enaria.GhastlyEnariaEntity
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.Quaternion
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.util.ResourceLocation

/**
 * Renders the enaria model
 *
 * @constructor just initializes the render living renderer
 * @param renderManager The render manager to pass down
 */
class GhastlyEnariaRenderer(renderManager: EntityRendererManager) :
    MobRenderer<GhastlyEnariaEntity, EnariaModel<GhastlyEnariaEntity>>(renderManager, ENARIA_MODEL, 0f) {
    override fun preRenderCallback(enaria: GhastlyEnariaEntity, matrixStack: MatrixStack, partialTicks: Float) {
        matrixStack.rotate(Quaternion(180f, 0f, 1f, 0f))
        matrixStack.rotate(Quaternion(180f, 0f, 0f, 1f))
        matrixStack.translate(0.0, MODEL_HEIGHT, 0.0)
    }

    override fun render(
        enaria: GhastlyEnariaEntity,
        entityYaw: Float,
        partialTicks: Float,
        matrixStack: MatrixStack,
        renderTypeBuffer: IRenderTypeBuffer,
        packedLight: Int
    ) {
        enaria.getAnimationHandler().update()

        super.render(enaria, entityYaw, partialTicks, matrixStack, renderTypeBuffer, packedLight)
    }

    override fun getEntityTexture(entity: GhastlyEnariaEntity): ResourceLocation {
        return ENARIA_TEXTURE
    }

    companion object {
        // The texture to apply to the model
        private val ENARIA_TEXTURE = ResourceLocation(Constants.MOD_ID, "textures/entity/enaria.png")

        // The model to render
        private val ENARIA_MODEL = EnariaModel<GhastlyEnariaEntity>(RenderType::getEntityTranslucent)

        // The height of the model to render at
        private const val MODEL_HEIGHT = 2.8
    }
}
