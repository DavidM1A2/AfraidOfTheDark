package com.davidm1a2.afraidofthedark.client.dimension

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.*
import com.mojang.math.Vector3f
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.client.ISkyRenderHandler

/**
 * Class that renders the void chest 'sky' texture
 */
class VoidChestSkyRenderer : ISkyRenderHandler {
    override fun render(ticks: Int, partialTicks: Float, matrixStack: PoseStack, world: ClientLevel?, mc: Minecraft?) {
        ///
        /// Code below is similar to WorldRenderer::renderSky()
        ///

        RenderSystem.disableDepthTest()
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.depthMask(false)

        val tessellator = Tesselator.getInstance()
        val bufferBuilder = tessellator.builder

        for (i in 0..5) {
            matrixStack.pushPose()

            when (i) {
                1 -> {
                    RenderSystem.setShaderTexture(0, VOID_CHEST_SKY_SIDE_2)
                    matrixStack.mulPose(Vector3f.XP.rotationDegrees(90.0f))
                }
                2 -> {
                    RenderSystem.setShaderTexture(0, VOID_CHEST_SKY_SIDE_4)
                    matrixStack.mulPose(Vector3f.XP.rotationDegrees(-90.0f))
                }
                3 -> {
                    RenderSystem.setShaderTexture(0, VOID_CHEST_SKY_TOP)
                    matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0f))
                }
                4 -> {
                    RenderSystem.setShaderTexture(0, VOID_CHEST_SKY_SIDE_3)
                    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(90.0f))
                }
                5 -> {
                    RenderSystem.setShaderTexture(0, VOID_CHEST_SKY_SIDE_1)
                    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-90.0f))
                }
                else -> RenderSystem.setShaderTexture(0, VOID_CHEST_SKY_BOTTOM)
            }

            val matrix = matrixStack.last().pose()
            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX)
            bufferBuilder.vertex(matrix, -100.0f, -100.0f, -100.0f).uv(0.0f, 0.0f).endVertex()
            bufferBuilder.vertex(matrix, -100.0f, -100.0f, 100.0f).uv(0.0f, 1.0f).endVertex()
            bufferBuilder.vertex(matrix, 100.0f, -100.0f, 100.0f).uv(1.0f, 1.0f).endVertex()
            bufferBuilder.vertex(matrix, 100.0f, -100.0f, -100.0f).uv(1.0f, 0.0f).endVertex()
            bufferBuilder.end()
            BufferUploader.end(bufferBuilder)

            matrixStack.popPose()
        }

        RenderSystem.depthMask(true)
        RenderSystem.enableTexture()
        RenderSystem.disableBlend()
        RenderSystem.enableDepthTest()
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
