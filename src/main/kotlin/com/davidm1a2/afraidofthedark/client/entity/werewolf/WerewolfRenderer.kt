package com.davidm1a2.afraidofthedark.client.entity.werewolf

import com.davidm1a2.afraidofthedark.common.entity.werewolf.WerewolfEntity
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Vector3f
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.resources.ResourceLocation

/**
 * Renderer class for the werewolf entity
 *
 * @constructor just passes down fields and the render manager
 * @param renderManager The render manager to pass down
 */
class WerewolfRenderer(renderManager: EntityRendererProvider.Context) : MobRenderer<WerewolfEntity, WerewolfModel>(renderManager, WEREWOLF_MODEL, MODEL_SHADOW_SIZE) {
    override fun setupRotations(werewolf: WerewolfEntity, matrixStack: PoseStack, bob: Float, yOffset: Float, partialTicks: Float) {
        super.setupRotations(werewolf, matrixStack, bob, yOffset, partialTicks)
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180f))
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180f))
        matrixStack.translate(0.0, -MODEL_HEIGHT, 0.0)
    }

    override fun render(
        werewolf: WerewolfEntity,
        entityYaw: Float,
        partialTicks: Float,
        matrixStack: PoseStack,
        renderTypeBuffer: MultiBufferSource,
        packedLight: Int
    ) {
        werewolf.getAnimationHandler().update()

        super.render(werewolf, entityYaw, partialTicks, matrixStack, renderTypeBuffer, packedLight)
    }

    /**
     * Gets the texture for the entity
     *
     * @param entity The entity to get the texture for
     * @return The texture to use for this entity
     */
    override fun getTextureLocation(entity: WerewolfEntity): ResourceLocation {
        return WEREWOLF_TEXTURE
    }

    companion object {
        // The texture used by the model
        private val WEREWOLF_TEXTURE = ResourceLocation("afraidofthedark:textures/entity/werewolf.png")

        // The werewolf model
        private val WEREWOLF_MODEL = WerewolfModel()

        // The height of the werewolf model
        private const val MODEL_HEIGHT = 2.5

        // The size of the shadow of the model
        private const val MODEL_SHADOW_SIZE = 0.6f
    }
}