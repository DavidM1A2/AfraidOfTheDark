package com.davidm1a2.afraidofthedark.client.entity.enchantedFrog

import com.davidm1a2.afraidofthedark.common.entity.enchantedFrog.EnchantedFrogEntity
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.util.math.vector.Vector3f

/**
 * Renderer class for the enchanted frog entity
 *
 * @constructor just initializes the render living renderer
 * @param renderManager The render manager to pass down
 */
class EnchantedFrogRenderer(renderManager: EntityRendererManager) :
    MobRenderer<EnchantedFrogEntity, EnchantedFrogModel>(renderManager, ENCHANTED_FROG_MODEL, MODEL_SHADOW_SIZE) {
    override fun setupRotations(enchantedFrogEntity: EnchantedFrogEntity, matrixStack: MatrixStack, bob: Float, yOffset: Float, partialTicks: Float) {
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180f))
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180f))
    }

    override fun getRenderOffset(enchantedFrogEntity: EnchantedFrogEntity, partialTicks: Float): Vector3d {
        return Vector3d(0.0, MODEL_HEIGHT, 0.0)
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