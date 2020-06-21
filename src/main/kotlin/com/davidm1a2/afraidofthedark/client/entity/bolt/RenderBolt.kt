package com.davidm1a2.afraidofthedark.client.entity.bolt

import com.davidm1a2.afraidofthedark.common.entity.bolt.EntityBolt
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.Render
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation

/**
 * Base class for all bolt renderers
 *
 * @constructor Takes a render manager and bolt texture as input
 * @param renderManager The manager given to us by Minecraft
 * @property boltTexture A resource location containing the bolt texture
 */
abstract class RenderBolt<T : EntityBolt>(renderManager: RenderManager) : Render<T>(renderManager) {
    internal abstract val boltTexture: ResourceLocation

    /**
     * Renders the bolt, all code here is copied from EntitySnowball
     *
     * @param entity       The entity to render
     * @param x            The X position to render at
     * @param y            The Y position to render at
     * @param z            The Z position to render at
     * @param entityYaw    The yaw of the entity to render
     * @param partialTicks Partial ticks between the last and next tick
     */
    override fun doRender(entity: T, x: Double, y: Double, z: Double, entityYaw: Float, partialTicks: Float) {
        ///
        /// Code copied from ArrowRender
        ///

        this.bindEntityTexture(entity)
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f)
        GlStateManager.pushMatrix()
        GlStateManager.disableLighting()
        GlStateManager.translatef(x.toFloat(), y.toFloat(), z.toFloat())
        GlStateManager.rotatef(
            entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0f,
            0.0f,
            1.0f,
            0.0f
        )
        GlStateManager.rotatef(
            entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks,
            0.0f,
            0.0f,
            1.0f
        )
        val tessellator = Tessellator.getInstance()
        val bufferbuilder = tessellator.buffer
        GlStateManager.enableRescaleNormal()

        GlStateManager.rotatef(45.0f, 1.0f, 0.0f, 0.0f)
        GlStateManager.scalef(0.05625f, 0.05625f, 0.05625f)
        GlStateManager.translatef(-4.0f, 0.0f, 0.0f)

        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial()
            GlStateManager.enableOutlineMode(this.getTeamColor(entity))
        }

        GlStateManager.normal3f(0.05625f, 0.0f, 0.0f)
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX)
        bufferbuilder.pos(-7.0, -2.0, -2.0).tex(0.0, 0.15625).endVertex()
        bufferbuilder.pos(-7.0, -2.0, 2.0).tex(0.15625, 0.15625).endVertex()
        bufferbuilder.pos(-7.0, 2.0, 2.0).tex(0.15625, 0.3125).endVertex()
        bufferbuilder.pos(-7.0, 2.0, -2.0).tex(0.0, 0.3125).endVertex()
        tessellator.draw()
        GlStateManager.normal3f(-0.05625f, 0.0f, 0.0f)
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX)
        bufferbuilder.pos(-7.0, 2.0, -2.0).tex(0.0, 0.15625).endVertex()
        bufferbuilder.pos(-7.0, 2.0, 2.0).tex(0.15625, 0.15625).endVertex()
        bufferbuilder.pos(-7.0, -2.0, 2.0).tex(0.15625, 0.3125).endVertex()
        bufferbuilder.pos(-7.0, -2.0, -2.0).tex(0.0, 0.3125).endVertex()
        tessellator.draw()

        for (j in 0..3) {
            GlStateManager.rotatef(90.0f, 1.0f, 0.0f, 0.0f)
            GlStateManager.normal3f(0.0f, 0.0f, 0.05625f)
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX)
            bufferbuilder.pos(-8.0, -2.0, 0.0).tex(0.0, 0.0).endVertex()
            bufferbuilder.pos(8.0, -2.0, 0.0).tex(0.5, 0.0).endVertex()
            bufferbuilder.pos(8.0, 2.0, 0.0).tex(0.5, 0.15625).endVertex()
            bufferbuilder.pos(-8.0, 2.0, 0.0).tex(0.0, 0.15625).endVertex()
            tessellator.draw()
        }

        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode()
            GlStateManager.disableColorMaterial()
        }

        GlStateManager.disableRescaleNormal()
        GlStateManager.enableLighting()
        GlStateManager.popMatrix()
        super.doRender(entity, x, y, z, entityYaw, partialTicks)
    }

    /**
     * Returns the location of an entity's texture.
     *
     * @param entity The entity to get the texture for
     * @return The resource location representing the entity texture
     */
    override fun getEntityTexture(entity: T): ResourceLocation {
        return boltTexture
    }
}
