/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.entities.Bolts;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.EntityBolt;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// Mostly arrow source code but with an entity bolt
@SideOnly(Side.CLIENT)
public class BoltRender extends Render
{
	// The texture the bolt will use to render itself
	private final ResourceLocation BOLT_TEXTURE;

	public BoltRender(final RenderManager renderManager, final String boltTexture)
	{
		super(renderManager);
		this.BOLT_TEXTURE = new ResourceLocation(boltTexture);
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then handing it off to a worker
	 * function which does the actual work. In all probabilty, the class Render is generic (Render<T extends Entity) and this method has signature
	 * public void func_76986_a(T entity, double d, double d1, double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(final EntityBolt p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_)
	{
		this.bindEntityTexture(p_76986_1_);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) p_76986_2_, (float) p_76986_4_, (float) p_76986_6_);
		GL11.glRotatef((p_76986_1_.prevRotationYaw + ((p_76986_1_.rotationYaw - p_76986_1_.prevRotationYaw) * p_76986_9_)) - 90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(p_76986_1_.prevRotationPitch + ((p_76986_1_.rotationPitch - p_76986_1_.prevRotationPitch) * p_76986_9_), 0.0F, 0.0F, 1.0F);
		final Tessellator tessellator = Tessellator.getInstance();
		final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		final byte b0 = 0;
		final float f2 = 0.0F;
		final float f3 = 0.5F;
		final float f4 = (0 + (b0 * 10)) / 32.0F;
		final float f5 = (5 + (b0 * 10)) / 32.0F;
		final float f6 = 0.0F;
		final float f7 = 0.15625F;
		final float f8 = (5 + (b0 * 10)) / 32.0F;
		final float f9 = (10 + (b0 * 10)) / 32.0F;
		final float f10 = 0.05625F;
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);

		GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(f10, f10, f10);
		GL11.glTranslatef(-4.0F, 0.0F, 0.0F);
		GL11.glNormal3f(f10, 0.0F, 0.0F);
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertexWithUV(-7.0D, -2.0D, -2.0D, f6, f8);
		worldRenderer.addVertexWithUV(-7.0D, -2.0D, 2.0D, f7, f8);
		worldRenderer.addVertexWithUV(-7.0D, 2.0D, 2.0D, f7, f9);
		worldRenderer.addVertexWithUV(-7.0D, 2.0D, -2.0D, f6, f9);
		tessellator.draw();
		GL11.glNormal3f(-f10, 0.0F, 0.0F);
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertexWithUV(-7.0D, 2.0D, -2.0D, f6, f8);
		worldRenderer.addVertexWithUV(-7.0D, 2.0D, 2.0D, f7, f8);
		worldRenderer.addVertexWithUV(-7.0D, -2.0D, 2.0D, f7, f9);
		worldRenderer.addVertexWithUV(-7.0D, -2.0D, -2.0D, f6, f9);
		tessellator.draw();

		for (int i = 0; i < 4; ++i)
		{
			GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			GL11.glNormal3f(0.0F, 0.0F, f10);
			worldRenderer.startDrawingQuads();
			worldRenderer.addVertexWithUV(-8.0D, -2.0D, 0.0D, f2, f4);
			worldRenderer.addVertexWithUV(8.0D, -2.0D, 0.0D, f3, f4);
			worldRenderer.addVertexWithUV(8.0D, 2.0D, 0.0D, f3, f5);
			worldRenderer.addVertexWithUV(-8.0D, 2.0D, 0.0D, f2, f5);
			tessellator.draw();
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(final EntityBolt p_110775_1_)
	{
		return BOLT_TEXTURE;
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(final Entity entity)
	{
		return this.getEntityTexture((EntityBolt) entity);
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then handing it off to a worker
	 * function which does the actual work. In all probabilty, the class Render is generic (Render<T extends Entity) and this method has signature
	 * public void func_76986_a(T entity, double d, double d1, double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	@Override
	public void doRender(final Entity p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_)
	{
		this.doRender((EntityBolt) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}

}