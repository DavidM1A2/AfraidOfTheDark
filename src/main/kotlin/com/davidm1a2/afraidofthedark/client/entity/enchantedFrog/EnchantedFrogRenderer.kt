package com.davidm1a2.afraidofthedark.client.entity.enchantedFrog

import com.davidm1a2.afraidofthedark.common.entity.enchantedFrog.EnchantedFrogEntity
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.Quaternion
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.util.ResourceLocation

/**
 * Renderer class for the enchanted frog entity
 *
 * @constructor just initializes the render living renderer
 * @param renderManager The render manager to pass down
 */
class EnchantedFrogRenderer(renderManager: EntityRendererManager) :
    MobRenderer<EnchantedFrogEntity, EnchantedFrogModel>(renderManager, ENCHANTED_FROG_MODEL, MODEL_SHADOW_SIZE) {
    override fun preRenderCallback(frog: EnchantedFrogEntity, matrixStack: MatrixStack, partialTicks: Float) {
        matrixStack.rotate(Quaternion(180f, 0f, 1f, 0f))
        matrixStack.rotate(Quaternion(180f, 0f, 0f, 1f))
        matrixStack.translate(0.0, MODEL_HEIGHT, 0.0)
    }

    override fun render(
        frog: EnchantedFrogEntity,
        entityYaw: Float,
        partialTicks: Float,
        matrixStack: MatrixStack,
        renderTypeBuffer: IRenderTypeBuffer,
        packedLight: Int
    ) {
        frog.getAnimationHandler().update()

        super.render(frog, entityYaw, partialTicks, matrixStack, renderTypeBuffer, packedLight)
    }

    override fun getEntityTexture(entity: EnchantedFrogEntity): ResourceLocation {
        return ENCHANTED_FROG_TEXTURE
    }

    companion object {
        // The texture used by the model
        private val ENCHANTED_FROG_TEXTURE =
            ResourceLocation("afraidofthedark:textures/entity/enchanted_frog.png")

        // The frog model
        private val ENCHANTED_FROG_MODEL = EnchantedFrogModel()

        // The height of the frog model
        private const val MODEL_HEIGHT = 1.5

        // The size of the shadow of the model
        private const val MODEL_SHADOW_SIZE = 0.3f
    }
}