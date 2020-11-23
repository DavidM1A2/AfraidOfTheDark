package com.davidm1a2.afraidofthedark.client.entity.enchantedSkeleton

import com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton.EnchantedSkeletonEntity
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.entity.LivingRenderer
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

/**
 * Renderer class for the enchanted skeleton entity
 *
 * @constructor just initializes the render living renderer
 * @param renderManager The render manager to pass down
 */
class EnchantedSkeletonRenderer(renderManager: EntityRendererManager) :
    LivingRenderer<EnchantedSkeletonEntity, EnchantedSkeletonModel>(renderManager, ENCHANTED_SKELETON_MODEL, MODEL_SHADOW_SIZE) {
    /**
     * Renders the entity at a given position, yaw, and partial ticks parameter
     *
     * @param entity       The entity to render
     * @param posX         The X position of the entity to render at
     * @param posY         The Y position of the entity to render at
     * @param posZ         The Z position of the entity to render at
     * @param entityYaw    The yaw of the entity to render
     * @param partialTicks The partial ticks that have gone by since the last frame
     */
    override fun doRender(
        entity: EnchantedSkeletonEntity,
        posX: Double,
        posY: Double,
        posZ: Double,
        entityYaw: Float,
        partialTicks: Float
    ) {
        // Disable culling and render the model
        GL11.glPushMatrix()
        GL11.glDisable(GL11.GL_CULL_FACE)
        super.doRender(entity, posX, posY, posZ, entityYaw, partialTicks)
        GL11.glEnable(GL11.GL_CULL_FACE)
        GL11.glPopMatrix()
    }

    /**
     * Before rendering the entity transform the rendering openGL state
     *
     * @param entityLiving    The entity to render
     * @param partialTickTime The partial ticks that have gone by since the last frame
     */
    override fun preRenderCallback(entityLiving: EnchantedSkeletonEntity, partialTickTime: Float) {
        GL11.glRotatef(180f, 0f, 1f, 0f)
        GL11.glRotatef(180f, 0f, 0f, 1f)
        GL11.glTranslatef(0f, MODEL_HEIGHT, 0f)
    }

    /**
     * Gets the texture for the entity
     *
     * @param entity The entity to get the texture for
     * @return The texture to use for this entity
     */
    override fun getEntityTexture(entity: EnchantedSkeletonEntity): ResourceLocation {
        return ENCHANTED_SKELETON_TEXTURE
    }

    companion object {
        // The texture used by the model
        private val ENCHANTED_SKELETON_TEXTURE =
            ResourceLocation("afraidofthedark:textures/entity/enchanted_skeleton.png")

        // The skeleton model
        private val ENCHANTED_SKELETON_MODEL = EnchantedSkeletonModel()

        // The height of the skeleton model
        private const val MODEL_HEIGHT = 2.9f

        // The size of the shadow of the model
        private const val MODEL_SHADOW_SIZE = 0.5f
    }
}