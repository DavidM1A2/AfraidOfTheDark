package com.DavidM1A2.afraidofthedark.client.dimension;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Class that renders the void chest 'sky' texture
 */
public class VoidChestSkyRenderer extends IRenderHandler
{
    // Textures used by the 6 sides of the skybox
    private static final ResourceLocation VOID_CHEST_SKY_TOP = new ResourceLocation(Constants.MOD_ID, "textures/skybox/void_chest_top.png");
    private static final ResourceLocation VOID_CHEST_SKY_BOTTOM = new ResourceLocation(Constants.MOD_ID, "textures/skybox/void_chest_bottom.png");
    private static final ResourceLocation VOID_CHEST_SKY_SIDE_1 = new ResourceLocation(Constants.MOD_ID, "textures/skybox/void_chest_side_1.png");
    private static final ResourceLocation VOID_CHEST_SKY_SIDE_2 = new ResourceLocation(Constants.MOD_ID, "textures/skybox/void_chest_side_2.png");
    private static final ResourceLocation VOID_CHEST_SKY_SIDE_3 = new ResourceLocation(Constants.MOD_ID, "textures/skybox/void_chest_side_3.png");
    private static final ResourceLocation VOID_CHEST_SKY_SIDE_4 = new ResourceLocation(Constants.MOD_ID, "textures/skybox/void_chest_side_4.png");

    /**
     * Called to render the sky
     *
     * @param partialTicks The number of partial ticks since the last tick
     * @param world        The world to render in
     * @param mc           The minecraft instance
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void render(float partialTicks, WorldClient world, Minecraft mc)
    {
        ///
        /// Code below found online and modified slightly
        ///

        GlStateManager.disableFog();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.depthMask(false);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        for (int i = 0; i < 6; ++i)
        {
            GlStateManager.pushMatrix();

            if (i == 1)
            {
                Minecraft.getMinecraft().renderEngine.bindTexture(VOID_CHEST_SKY_SIDE_2);
                GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            }
            else if (i == 2)
            {
                Minecraft.getMinecraft().renderEngine.bindTexture(VOID_CHEST_SKY_SIDE_4);
                GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            }
            else if (i == 3)
            {
                Minecraft.getMinecraft().renderEngine.bindTexture(VOID_CHEST_SKY_TOP);
                GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
            }
            else if (i == 4)
            {
                Minecraft.getMinecraft().renderEngine.bindTexture(VOID_CHEST_SKY_SIDE_3);
                GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
            }
            else if (i == 5)
            {
                Minecraft.getMinecraft().renderEngine.bindTexture(VOID_CHEST_SKY_SIDE_1);
                GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
            }
            else
            {
                Minecraft.getMinecraft().renderEngine.bindTexture(VOID_CHEST_SKY_BOTTOM);
            }

            bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferBuilder.pos(-100.0D, -100.0D, -100.0D).tex(0.0D, 0.0D).endVertex();
            bufferBuilder.pos(-100.0D, -100.0D, 100.0D).tex(0.0D, 1.0D).endVertex();
            bufferBuilder.pos(100.0D, -100.0D, 100.0D).tex(1.0D, 1.0D).endVertex();
            bufferBuilder.pos(100.0D, -100.0D, -100.0D).tex(1.0D, 0.0D).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
    }
}
