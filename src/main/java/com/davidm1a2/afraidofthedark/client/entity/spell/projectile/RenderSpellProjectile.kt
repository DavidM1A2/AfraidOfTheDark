package com.davidm1a2.afraidofthedark.client.entity.spell.projectile

import com.davidm1a2.afraidofthedark.common.entity.spell.projectile.EntitySpellProjectile
import net.minecraft.client.renderer.entity.Render
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

/**
 * Renderer class for the spell projectile entity
 *
 * @constructor just passes down fields and the render manager
 * @param renderManager The render manager to pass down
 */
class RenderSpellProjectile(renderManager: RenderManager) : Render<EntitySpellProjectile>(renderManager) {
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
        entity: EntitySpellProjectile,
        posX: Double,
        posY: Double,
        posZ: Double,
        entityYaw: Float,
        partialTicks: Float
    ) {
        GL11.glPushMatrix()
        GL11.glDisable(GL11.GL_CULL_FACE)
        GL11.glTranslatef(posX.toFloat(), posY.toFloat() + 0.2f, posZ.toFloat())
        this.bindEntityTexture(entity)
        SPELL_PROJECTILE_MODEL.render(entity, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f)
        GL11.glEnable(GL11.GL_CULL_FACE)
        GL11.glPopMatrix()
    }

    /**
     * Gets the texture for the entity
     *
     * @param entity The entity to get the texture for
     * @return The texture to use for this entity
     */
    override fun getEntityTexture(entity: EntitySpellProjectile): ResourceLocation {
        return SPELL_PROJECTILE_TEXTURE
    }

    companion object {
        // The texture used by the model
        private val SPELL_PROJECTILE_TEXTURE = ResourceLocation("afraidofthedark:textures/entity/spell/projectile.png")
        // The spell projectile model
        private val SPELL_PROJECTILE_MODEL = ModelSpellProjectile()
    }
}
