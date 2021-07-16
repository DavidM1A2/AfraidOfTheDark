package com.davidm1a2.afraidofthedark.client.entity.werewolf

import com.davidm1a2.afraidofthedark.common.entity.werewolf.WerewolfEntity
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.Vector3f
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.util.ResourceLocation

/**
 * Renderer class for the werewolf entity
 *
 * @constructor just passes down fields and the render manager
 * @param renderManager The render manager to pass down
 */
class WerewolfRenderer(renderManager: EntityRendererManager) : MobRenderer<WerewolfEntity, WerewolfModel>(renderManager, WEREWOLF_MODEL, MODEL_SHADOW_SIZE) {
    override fun preRenderCallback(werewolf: WerewolfEntity, matrixStack: MatrixStack, partialTicks: Float) {
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180f))
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(180f))
        matrixStack.translate(0.0, MODEL_HEIGHT, 0.0)
    }

    override fun render(
        werewolf: WerewolfEntity,
        entityYaw: Float,
        partialTicks: Float,
        matrixStack: MatrixStack,
        renderTypeBuffer: IRenderTypeBuffer,
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
    override fun getEntityTexture(entity: WerewolfEntity): ResourceLocation {
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