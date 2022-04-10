package com.davidm1a2.afraidofthedark.client.entity.frostPhoenix

import com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.FrostPhoenixEntity
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Vector3f

class FrostPhoenixRenderer(renderManager: EntityRendererManager) :
    MobRenderer<FrostPhoenixEntity, FrostPhoenixModel>(renderManager, FROST_PHOENIX_MODEL, MODEL_SHADOW_SIZE) {
    override fun setupRotations(entity: FrostPhoenixEntity, matrixStack: MatrixStack, bob: Float, yOffset: Float, partialTicks: Float) {
        super.setupRotations(entity, matrixStack, bob, yOffset, partialTicks)
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180f - 90f))
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180f))
        matrixStack.scale(2f, 2f, 2f)
        matrixStack.translate(0.0, -MODEL_HEIGHT, 0.0)
    }

    override fun render(
        entity: FrostPhoenixEntity,
        entityYaw: Float,
        partialTicks: Float,
        matrixStack: MatrixStack,
        renderTypeBuffer: IRenderTypeBuffer,
        packedLight: Int
    ) {
        entity.getAnimationHandler().update()

        super.render(entity, entityYaw, partialTicks, matrixStack, renderTypeBuffer, packedLight)
    }

    override fun getTextureLocation(entity: FrostPhoenixEntity): ResourceLocation {
        return FROST_PHOENIX_TEXTURE
    }

    companion object {
        // The texture used by the model
        private val FROST_PHOENIX_TEXTURE =
            ResourceLocation("afraidofthedark:textures/entity/frost_phoenix.png")

        // The phoenix model
        private val FROST_PHOENIX_MODEL = FrostPhoenixModel()

        // The height of the phoenix model
        private const val MODEL_HEIGHT = 2.13

        // The size of the shadow of the model
        private const val MODEL_SHADOW_SIZE = 0.8f
    }
}