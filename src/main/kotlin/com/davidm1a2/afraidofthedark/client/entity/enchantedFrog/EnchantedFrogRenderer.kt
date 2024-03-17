package com.davidm1a2.afraidofthedark.client.entity.enchantedFrog

import com.davidm1a2.afraidofthedark.common.entity.enchantedFrog.EnchantedFrogEntity
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Vector3f
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.resources.ResourceLocation

/**
 * Renderer class for the enchanted frog entity
 *
 * @constructor just initializes the render living renderer
 * @param renderManager The render manager to pass down
 */
class EnchantedFrogRenderer(renderManager: EntityRendererProvider.Context) :
    MobRenderer<EnchantedFrogEntity, EnchantedFrogModel>(renderManager, ENCHANTED_FROG_MODEL, MODEL_SHADOW_SIZE) {
    override fun setupRotations(enchantedFrogEntity: EnchantedFrogEntity, matrixStack: PoseStack, bob: Float, yOffset: Float, partialTicks: Float) {
        super.setupRotations(enchantedFrogEntity, matrixStack, bob, yOffset, partialTicks)
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180f))
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180f))
        matrixStack.translate(0.0, -MODEL_HEIGHT, 0.0)
    }

    override fun render(
        frog: EnchantedFrogEntity,
        entityYaw: Float,
        partialTicks: Float,
        matrixStack: PoseStack,
        renderTypeBuffer: MultiBufferSource,
        packedLight: Int
    ) {
        frog.getAnimationHandler().update()

        super.render(frog, entityYaw, partialTicks, matrixStack, renderTypeBuffer, packedLight)
    }

    override fun getTextureLocation(entity: EnchantedFrogEntity): ResourceLocation {
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