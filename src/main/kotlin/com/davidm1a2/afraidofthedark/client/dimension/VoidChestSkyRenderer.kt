package com.davidm1a2.afraidofthedark.client.dimension

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldVertexBufferUploader
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Vector3f
import net.minecraftforge.client.ISkyRenderHandler
import org.lwjgl.opengl.GL11

/**
 * Class that renders the void chest 'sky' texture
 */
class VoidChestSkyRenderer : ISkyRenderHandler {
    override fun render(ticks: Int, partialTicks: Float, matrixStack: MatrixStack, world: ClientWorld, mc: Minecraft) {
        ///
        /// Code below is similar to WorldRenderer::renderSky()
        ///

        RenderSystem.disableAlphaTest()
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.depthMask(false)

        val tessellator = Tessellator.getInstance()
        val bufferBuilder = tessellator.builder

        for (i in 0..5) {
            matrixStack.pushPose()

            when (i) {
                1 -> {
                    mc.textureManager.bind(VOID_CHEST_SKY_SIDE_2)
                    matrixStack.mulPose(Vector3f.XP.rotationDegrees(90.0f))
                }
                2 -> {
                    mc.textureManager.bind(VOID_CHEST_SKY_SIDE_4)
                    matrixStack.mulPose(Vector3f.XP.rotationDegrees(-90.0f))
                }
                3 -> {
                    mc.textureManager.bind(VOID_CHEST_SKY_TOP)
                    matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0f))
                }
                4 -> {
                    mc.textureManager.bind(VOID_CHEST_SKY_SIDE_3)
                    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(90.0f))
                }
                5 -> {
                    mc.textureManager.bind(VOID_CHEST_SKY_SIDE_1)
                    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-90.0f))
                }
                else -> mc.textureManager.bind(VOID_CHEST_SKY_BOTTOM)
            }

            val matrix = matrixStack.last().pose()
            bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)
            bufferBuilder.vertex(matrix, -100.0f, -100.0f, -100.0f).uv(0.0f, 0.0f).endVertex()
            bufferBuilder.vertex(matrix, -100.0f, -100.0f, 100.0f).uv(0.0f, 1.0f).endVertex()
            bufferBuilder.vertex(matrix, 100.0f, -100.0f, 100.0f).uv(1.0f, 1.0f).endVertex()
            bufferBuilder.vertex(matrix, 100.0f, -100.0f, -100.0f).uv(1.0f, 0.0f).endVertex()
            bufferBuilder.end()
            WorldVertexBufferUploader.end(bufferBuilder)

            matrixStack.popPose()
        }

        RenderSystem.depthMask(true)
        RenderSystem.enableTexture()
        RenderSystem.disableBlend()
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
