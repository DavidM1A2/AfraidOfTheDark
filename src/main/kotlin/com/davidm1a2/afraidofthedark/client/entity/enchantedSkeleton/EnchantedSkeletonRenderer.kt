package com.davidm1a2.afraidofthedark.client.entity.enchantedSkeleton

import com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton.EnchantedSkeletonEntity
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Vector3f

/**
 * Renderer class for the enchanted skeleton entity
 *
 * @constructor just initializes the render living renderer
 * @param renderManager The render manager to pass down
 */
class EnchantedSkeletonRenderer(renderManager: EntityRendererManager) :
    MobRenderer<EnchantedSkeletonEntity, EnchantedSkeletonModel>(renderManager, ENCHANTED_SKELETON_MODEL, MODEL_SHADOW_SIZE) {

    override fun setupRotations(enchantedSkeleton: EnchantedSkeletonEntity, matrixStack: MatrixStack, bob: Float, yOffset: Float, partialTicks: Float) {
        super.setupRotations(enchantedSkeleton, matrixStack, bob, yOffset, partialTicks)
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180f))
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180f))
        matrixStack.translate(0.0, -MODEL_HEIGHT, 0.0)
    }

    override fun render(
        enchantedSkeleton: EnchantedSkeletonEntity,
        entityYaw: Float,
        partialTicks: Float,
        matrixStack: MatrixStack,
        renderTypeBuffer: IRenderTypeBuffer,
        packedLight: Int
    ) {
        enchantedSkeleton.getAnimationHandler().update()

        super.render(enchantedSkeleton, entityYaw, partialTicks, matrixStack, renderTypeBuffer, packedLight)
    }

    override fun getTextureLocation(entity: EnchantedSkeletonEntity): ResourceLocation {
        return ENCHANTED_SKELETON_TEXTURE
    }

    companion object {
        // The texture used by the model
        private val ENCHANTED_SKELETON_TEXTURE =
            ResourceLocation("afraidofthedark:textures/entity/enchanted_skeleton.png")

        // The skeleton model
        private val ENCHANTED_SKELETON_MODEL = EnchantedSkeletonModel()

        // The height of the skeleton model
        private const val MODEL_HEIGHT = 2.9

        // The size of the shadow of the model
        private const val MODEL_SHADOW_SIZE = 0.5f
    }
}