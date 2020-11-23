package com.davidm1a2.afraidofthedark.client.entity.splinterDrone

import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.SplinterDroneEntity
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

/**
 * Renderer class for the splinter drone entity
 *
 * @constructor just passes down fields and the render manager
 * @param renderManager The render manager to pass down
 */
class SplinterDroneRenderer(renderManager: EntityRendererManager) :
    MobRenderer<SplinterDroneEntity, SplinterDroneModel>(renderManager, SPLINTER_DRONE_MODEL, MODEL_SHADOW_SIZE) {
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
        entity: SplinterDroneEntity,
        posX: Double,
        posY: Double,
        posZ: Double,
        entityYaw: Float,
        partialTicks: Float
    ) {
        GL11.glPushMatrix()
        GL11.glDisable(GL11.GL_CULL_FACE)
        super.doRender(entity, posX, posY, posZ, entityYaw, partialTicks)
        GL11.glEnable(GL11.GL_CULL_FACE)
        GL11.glPopMatrix()
    }

    /**
     * Before rendering the entity transform the rendering openGL state
     *
     * @param entityliving    The entity to render
     * @param partialTickTime The partial ticks that have gone by since the last frame
     */
    override fun preRenderCallback(entityliving: SplinterDroneEntity, partialTickTime: Float) {
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
    override fun getEntityTexture(entity: SplinterDroneEntity): ResourceLocation {
        return SPLINTER_DRONE_TEXTURE
    }

    companion object {
        // The texture used by the model
        private val SPLINTER_DRONE_TEXTURE = ResourceLocation("afraidofthedark:textures/entity/splinter_drone.png")

        // The splinter drone model
        private val SPLINTER_DRONE_MODEL = SplinterDroneModel()

        // The height of the splinter drone model
        private const val MODEL_HEIGHT = 3.1f

        // The size of the shadow of the model
        private const val MODEL_SHADOW_SIZE = 0.3f
    }
}
