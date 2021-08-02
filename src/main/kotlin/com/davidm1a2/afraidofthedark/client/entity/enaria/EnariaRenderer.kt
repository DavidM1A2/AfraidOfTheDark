package com.davidm1a2.afraidofthedark.client.entity.enaria

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.entity.enaria.EnariaEntity
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Vector3f

/**
 * Renders the enaria model
 *
 * @constructor just initializes the render living renderer
 * @param renderManager The render manager to pass down
 */
class EnariaRenderer(renderManager: EntityRendererManager) : MobRenderer<EnariaEntity, EnariaModel<EnariaEntity>>(renderManager, ENARIA_MODEL, 0f) {
    override fun setupRotations(enaria: EnariaEntity, matrixStack: MatrixStack, bob: Float, yOffset: Float, partialTicks: Float) {
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180f))
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180f))
        matrixStack.translate(0.0, MODEL_HEIGHT, 0.0)
    }

    override fun render(
        enaria: EnariaEntity,
        entityYaw: Float,
        partialTicks: Float,
        matrixStack: MatrixStack,
        renderTypeBuffer: IRenderTypeBuffer,
        packedLight: Int
    ) {
        enaria.getAnimationHandler().update()

        super.render(enaria, entityYaw, partialTicks, matrixStack, renderTypeBuffer, packedLight)
    }

    override fun getTextureLocation(entity: EnariaEntity): ResourceLocation {
        return ENARIA_TEXTURE
    }

    companion object {
        // The texture to apply to the model
        private val ENARIA_TEXTURE = ResourceLocation(Constants.MOD_ID, "textures/entity/enaria.png")

        // The model to render
        private val ENARIA_MODEL = EnariaModel<EnariaEntity>(false, RenderType::entityCutoutNoCull)

        // The height of the model to render at
        private const val MODEL_HEIGHT = 2.8
    }
}
