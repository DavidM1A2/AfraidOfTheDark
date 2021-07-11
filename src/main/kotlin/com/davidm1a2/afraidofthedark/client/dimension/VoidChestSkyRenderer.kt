package com.davidm1a2.afraidofthedark.client.dimension

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.Vector3f
import net.minecraft.client.renderer.WorldVertexBufferUploader
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.SkyRenderHandler
import org.lwjgl.opengl.GL11

/**
 * Class that renders the void chest 'sky' texture
 */
class VoidChestSkyRenderer : SkyRenderHandler {
    @OnlyIn(Dist.CLIENT)
    override fun render(ticks: Int, partialTicks: Float, matrixStack: MatrixStack, world: ClientWorld, mc: Minecraft) {
        ///
        /// Code below is similar to WorldRenderer::renderSky()
        ///

        RenderSystem.disableFog()
        RenderSystem.disableAlphaTest()
        RenderSystem.enableBlend()
        RenderSystem.blendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO
        )
        RenderHelper.disableStandardItemLighting()
        RenderSystem.depthMask(false)
        val tessellator = Tessellator.getInstance()
        val bufferBuilder = tessellator.buffer

        for (i in 0..5) {
            matrixStack.push()

            when (i) {
                1 -> {
                    mc.textureManager.bindTexture(VOID_CHEST_SKY_SIDE_2)
                    matrixStack.rotate(Vector3f.XP.rotationDegrees(90.0f))
                }
                2 -> {
                    mc.textureManager.bindTexture(VOID_CHEST_SKY_SIDE_4)
                    matrixStack.rotate(Vector3f.XP.rotationDegrees(-90.0f))
                }
                3 -> {
                    mc.textureManager.bindTexture(VOID_CHEST_SKY_TOP)
                    matrixStack.rotate(Vector3f.XP.rotationDegrees(180.0f))
                }
                4 -> {
                    mc.textureManager.bindTexture(VOID_CHEST_SKY_SIDE_3)
                    matrixStack.rotate(Vector3f.ZP.rotationDegrees(90.0f))
                }
                5 -> {
                    mc.textureManager.bindTexture(VOID_CHEST_SKY_SIDE_1)
                    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-90.0f))
                }
                else -> mc.textureManager.bindTexture(VOID_CHEST_SKY_BOTTOM)
            }

            val matrix = matrixStack.last.matrix
            bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX)
            bufferBuilder.pos(matrix, -100.0f, -100.0f, -100.0f).tex(0.0f, 0.0f).endVertex()
            bufferBuilder.pos(matrix, -100.0f, -100.0f, 100.0f).tex(0.0f, 1.0f).endVertex()
            bufferBuilder.pos(matrix, 100.0f, -100.0f, 100.0f).tex(1.0f, 1.0f).endVertex()
            bufferBuilder.pos(matrix, 100.0f, -100.0f, -100.0f).tex(1.0f, 0.0f).endVertex()
            bufferBuilder.finishDrawing()
            WorldVertexBufferUploader.draw(bufferBuilder)

            matrixStack.pop()
        }

        RenderSystem.depthMask(true)
        GL11.glEnable(GL11.GL_TEXTURE_2D)
        RenderSystem.enableAlphaTest()
    }

    companion object {
        // Textures used by the 6 sides of the skybox
        private val VOID_CHEST_SKY_TOP = ResourceLocation(Constants.MOD_ID, "textures/skybox/void_chest_top.png")
        private val VOID_CHEST_SKY_BOTTOM = ResourceLocation(Constants.MOD_ID, "textures/skybox/void_chest_bottom.png")
        private val VOID_CHEST_SKY_SIDE_1 = ResourceLocation(Constants.MOD_ID, "textures/skybox/void_chest_side_1.png")
        private val VOID_CHEST_SKY_SIDE_2 = ResourceLocation(Constants.MOD_ID, "textures/skybox/void_chest_side_2.png")
        private val VOID_CHEST_SKY_SIDE_3 = ResourceLocation(Constants.MOD_ID, "textures/skybox/void_chest_side_3.png")
        private val VOID_CHEST_SKY_SIDE_4 = ResourceLocation(Constants.MOD_ID, "textures/skybox/void_chest_side_4.png")
    }
}
