/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.dimension.voidChest;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class VoidChestSkyRenderer extends IRenderHandler
{
	private static final ResourceLocation[] VOID_CHEST = new ResourceLocation[]
	{       new ResourceLocation("afraidofthedark:textures/skybox/void_chest_top.png"),
			new ResourceLocation("afraidofthedark:textures/skybox/void_chest_bottom.png"),
			new ResourceLocation("afraidofthedark:textures/skybox/void_chest_side_1.png"),
			new ResourceLocation("afraidofthedark:textures/skybox/void_chest_side_2.png"),
			new ResourceLocation("afraidofthedark:textures/skybox/void_chest_side_3.png"),
			new ResourceLocation("afraidofthedark:textures/skybox/void_chest_side_4.png") };

	@Override
	@SideOnly(Side.CLIENT)
	public void render(float partialTicks, WorldClient world, Minecraft mc)
	{
		GlStateManager.disableFog();
		GlStateManager.disableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.depthMask(false);
		Tessellator tessellator = Tessellator.getInstance();
		net.minecraft.client.renderer.VertexBuffer worldrenderer = tessellator.getBuffer();

		for (int i = 0; i < 6; ++i)
		{
			GlStateManager.pushMatrix();

			if (i == 1)
			{
				// Correct
				Minecraft.getMinecraft().renderEngine.bindTexture(VOID_CHEST[3]);
				GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
			}
			else if (i == 2)
			{
				Minecraft.getMinecraft().renderEngine.bindTexture(VOID_CHEST[5]);
				GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
			}
			else if (i == 3)
			{
				// Correct
				Minecraft.getMinecraft().renderEngine.bindTexture(VOID_CHEST[0]);
				GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
			}
			else if (i == 4)
			{
				// Correct
				Minecraft.getMinecraft().renderEngine.bindTexture(VOID_CHEST[4]);
				GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
			}
			else if (i == 5)
			{
				Minecraft.getMinecraft().renderEngine.bindTexture(VOID_CHEST[2]);
				GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
			}
			else
			{
				// Correct
				Minecraft.getMinecraft().renderEngine.bindTexture(VOID_CHEST[1]);
			}

			worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
			// worldrenderer.func_178991_c(2631720);
			worldrenderer.pos(-100.0D, -100.0D, -100.0D).tex(0.0D, 0.0D).endVertex();
			worldrenderer.pos(-100.0D, -100.0D, 100.0D).tex(0.0D, 1.0D).endVertex();
			worldrenderer.pos(100.0D, -100.0D, 100.0D).tex(1.0D, 1.0D).endVertex();
			worldrenderer.pos(100.0D, -100.0D, -100.0D).tex(1.0D, 0.0D).endVertex();
			tessellator.draw();
			GlStateManager.popMatrix();
		}

		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
		GlStateManager.enableAlpha();
	}
}
