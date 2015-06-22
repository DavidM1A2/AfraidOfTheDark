/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.dimension;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

public class NightmareSkyRenderer extends IRenderHandler
{

	@Override
	@SideOnly(Side.CLIENT)
	public void render(float partialTicks, WorldClient world, Minecraft mc)
	{
		int strangeVariableInt = 2;
		boolean someBooleanField = true;
		VertexFormat vertexFormat = new VertexFormat();
		vertexFormat.func_177349_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
		VertexBuffer someVertexBuffer = new VertexBuffer(vertexFormat);
		VertexBuffer someVertexBuffer2 = new VertexBuffer(vertexFormat);
		VertexBuffer someVertexBuffer3 = new VertexBuffer(vertexFormat);
		int starGLCallList = GLAllocation.generateDisplayLists(3);
		int glSkyList = starGLCallList + 1;
		int glSkyList2 = starGLCallList + 2;

		GlStateManager.func_179090_x();
		Vec3 vec3 = world.getSkyColor(mc.getRenderViewEntity(), partialTicks);
		float f1 = (float) vec3.xCoord;
		float f2 = (float) vec3.yCoord;
		float f3 = (float) vec3.zCoord;

		if (strangeVariableInt != 2)
		{
			float f4 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
			float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
			float f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
			f1 = f4;
			f2 = f5;
			f3 = f6;
		}

		GlStateManager.color(f1, f2, f3);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.depthMask(false);
		GlStateManager.enableFog();
		GlStateManager.color(f1, f2, f3);

		if (someBooleanField)
		{
			someVertexBuffer.func_177359_a();
			GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
			GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0L);
			someVertexBuffer.func_177358_a(7);
			someVertexBuffer.func_177361_b();
			GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		}
		else
		{
			GlStateManager.callList(glSkyList);
		}

		GlStateManager.disableFog();
		GlStateManager.disableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		RenderHelper.disableStandardItemLighting();
		float[] afloat = world.provider.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks);
		float f7;
		float f8;
		float f9;
		float f10;
		float f11;

		if (afloat != null)
		{
			GlStateManager.func_179090_x();
			GlStateManager.shadeModel(7425);
			GlStateManager.pushMatrix();
			GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
			f7 = afloat[0];
			f8 = afloat[1];
			f9 = afloat[2];
			float f12;

			if (strangeVariableInt != 2)
			{
				f10 = (f7 * 30.0F + f8 * 59.0F + f9 * 11.0F) / 100.0F;
				f11 = (f7 * 30.0F + f8 * 70.0F) / 100.0F;
				f12 = (f7 * 30.0F + f9 * 70.0F) / 100.0F;
				f7 = f10;
				f8 = f11;
				f9 = f12;
			}

			worldrenderer.startDrawing(6);
			worldrenderer.func_178960_a(f7, f8, f9, afloat[3]);
			worldrenderer.addVertex(0.0D, 100.0D, 0.0D);
			boolean flag = true;
			worldrenderer.func_178960_a(afloat[0], afloat[1], afloat[2], 0.0F);

			for (int j = 0; j <= 16; ++j)
			{
				f12 = (float) j * (float) Math.PI * 2.0F / 16.0F;
				float f13 = MathHelper.sin(f12);
				float f14 = MathHelper.cos(f12);
				worldrenderer.addVertex((double) (f13 * 120.0F), (double) (f14 * 120.0F), (double) (-f14 * 40.0F * afloat[3]));
			}

			tessellator.draw();
			GlStateManager.popMatrix();
			GlStateManager.shadeModel(7424);
		}

		GlStateManager.func_179098_w();
		GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
		GlStateManager.pushMatrix();
		f7 = 1.0F - world.getRainStrength(partialTicks);
		f8 = 0.0F;
		f9 = 0.0F;
		f10 = 0.0F;
		GlStateManager.color(1.0F, 1.0F, 1.0F, f7);
		GlStateManager.translate(0.0F, 0.0F, 0.0F);
		GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
		f11 = 30.0F;
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("textures/environment/sun.png"));
		worldrenderer.startDrawingQuads();
		worldrenderer.addVertexWithUV((double) (-f11), 100.0D, (double) (-f11), 0.0D, 0.0D);
		worldrenderer.addVertexWithUV((double) f11, 100.0D, (double) (-f11), 1.0D, 0.0D);
		worldrenderer.addVertexWithUV((double) f11, 100.0D, (double) f11, 1.0D, 1.0D);
		worldrenderer.addVertexWithUV((double) (-f11), 100.0D, (double) f11, 0.0D, 1.0D);
		tessellator.draw();
		f11 = 20.0F;
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("textures/environment/moon_phases.png"));
		int k = world.getMoonPhase();
		int l = k % 4;
		int i1 = k / 4 % 2;
		float f15 = (float) (l + 0) / 4.0F;
		float f16 = (float) (i1 + 0) / 2.0F;
		float f17 = (float) (l + 1) / 4.0F;
		float f18 = (float) (i1 + 1) / 2.0F;
		worldrenderer.startDrawingQuads();
		worldrenderer.addVertexWithUV((double) (-f11), -100.0D, (double) f11, (double) f17, (double) f18);
		worldrenderer.addVertexWithUV((double) f11, -100.0D, (double) f11, (double) f15, (double) f18);
		worldrenderer.addVertexWithUV((double) f11, -100.0D, (double) (-f11), (double) f15, (double) f16);
		worldrenderer.addVertexWithUV((double) (-f11), -100.0D, (double) (-f11), (double) f17, (double) f16);
		tessellator.draw();
		GlStateManager.func_179090_x();
		float f19 = world.getStarBrightness(partialTicks) * f7;

		if (f19 > 0.0F)
		{
			GlStateManager.color(f19, f19, f19, f19);

			if (someBooleanField)
			{
				someVertexBuffer2.func_177359_a();
				GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
				GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0L);
				someVertexBuffer2.func_177358_a(7);
				someVertexBuffer2.func_177361_b();
				GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
			}
			else
			{
				GlStateManager.callList(starGLCallList);
			}
		}

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableFog();
		GlStateManager.popMatrix();
		GlStateManager.func_179090_x();
		GlStateManager.color(0.0F, 0.0F, 0.0F);
		double d0 = mc.thePlayer.getPositionEyes(partialTicks).yCoord - world.getHorizon();

		if (d0 < 0.0D)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0F, 12.0F, 0.0F);

			if (someBooleanField)
			{
				someVertexBuffer3.func_177359_a();
				GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
				GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0L);
				someVertexBuffer3.func_177358_a(7);
				someVertexBuffer3.func_177361_b();
				GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
			}
			else
			{
				GlStateManager.callList(glSkyList2);
			}

			GlStateManager.popMatrix();
			f9 = 1.0F;
			f10 = -((float) (d0 + 65.0D));
			f11 = -1.0F;
			worldrenderer.startDrawingQuads();
			worldrenderer.func_178974_a(0, 255);
			worldrenderer.addVertex(-1.0D, (double) f10, 1.0D);
			worldrenderer.addVertex(1.0D, (double) f10, 1.0D);
			worldrenderer.addVertex(1.0D, -1.0D, 1.0D);
			worldrenderer.addVertex(-1.0D, -1.0D, 1.0D);
			worldrenderer.addVertex(-1.0D, -1.0D, -1.0D);
			worldrenderer.addVertex(1.0D, -1.0D, -1.0D);
			worldrenderer.addVertex(1.0D, (double) f10, -1.0D);
			worldrenderer.addVertex(-1.0D, (double) f10, -1.0D);
			worldrenderer.addVertex(1.0D, -1.0D, -1.0D);
			worldrenderer.addVertex(1.0D, -1.0D, 1.0D);
			worldrenderer.addVertex(1.0D, (double) f10, 1.0D);
			worldrenderer.addVertex(1.0D, (double) f10, -1.0D);
			worldrenderer.addVertex(-1.0D, (double) f10, -1.0D);
			worldrenderer.addVertex(-1.0D, (double) f10, 1.0D);
			worldrenderer.addVertex(-1.0D, -1.0D, 1.0D);
			worldrenderer.addVertex(-1.0D, -1.0D, -1.0D);
			worldrenderer.addVertex(-1.0D, -1.0D, -1.0D);
			worldrenderer.addVertex(-1.0D, -1.0D, 1.0D);
			worldrenderer.addVertex(1.0D, -1.0D, 1.0D);
			worldrenderer.addVertex(1.0D, -1.0D, -1.0D);
			tessellator.draw();
		}

		if (world.provider.isSkyColored())
		{
			GlStateManager.color(f1 * 0.2F + 0.04F, f2 * 0.2F + 0.04F, f3 * 0.6F + 0.1F);
		}
		else
		{
			GlStateManager.color(f1, f2, f3);
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0F, -((float) (d0 - 16.0D)), 0.0F);
		GlStateManager.callList(glSkyList2);
		GlStateManager.popMatrix();
		GlStateManager.func_179098_w();
		GlStateManager.depthMask(true);
	}
}
