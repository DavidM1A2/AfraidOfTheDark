/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.entities.bolts;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntityBolt;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// Mostly arrow source code but with an entity bolt
@SideOnly(Side.CLIENT)
public abstract class BoltRender<T extends EntityBolt> extends Render<T> {
	// The texture the bolt will use to render itself
	private final ResourceLocation BOLT_TEXTURE;

	public BoltRender(final RenderManager renderManager, final String boltTexture) {
		super(renderManager);
		this.BOLT_TEXTURE = new ResourceLocation(boltTexture);
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method,
	 * always casting down its argument and then handing it off to a worker
	 * function which does the actual work. In all probabilty, the class Render
	 * is generic (Render<T extends Entity) and this method has signature public
	 * void func_76986_a(T entity, double d, double d1, double d2, float f,
	 * float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(final T entity, final double x, final double y, final double z,
			final float p_76986_8_, final float p_76986_9_) {
		this.bindEntityTexture(entity);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x, (float) y, (float) z);
		GL11.glRotatef(
				(entity.prevRotationYaw + ((entity.rotationYaw - entity.prevRotationYaw) * p_76986_9_))
						- 90.0F,
				0.0F, 1.0F, 0.0F);
		GL11.glRotatef(
				entity.prevRotationPitch + ((entity.rotationPitch - entity.prevRotationPitch) * p_76986_9_),
				0.0F, 0.0F, 1.0F);
		final Tessellator tessellator = Tessellator.getInstance();
		final VertexBuffer vertexBuffer = tessellator.getBuffer();
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
		// vertexBuffer.startDrawingQuads();
		// vertexBuffer.addVertexWithUV(-7.0D, -2.0D, -2.0D, f6, f8);
		// vertexBuffer.addVertexWithUV(-7.0D, -2.0D, 2.0D, f7, f8);
		// vertexBuffer.addVertexWithUV(-7.0D, 2.0D, 2.0D, f7, f9);
		// vertexBuffer.addVertexWithUV(-7.0D, 2.0D, -2.0D, f6, f9);
		vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		vertexBuffer.pos(-7.0D, -2.0D, -2.0D).tex(f6, f8).endVertex();
		vertexBuffer.pos(-7.0D, -2.0D, 2.0D).tex(f7, f8).endVertex();
		vertexBuffer.pos(-7.0D, 2.0D, 2.0D).tex(f7, f9).endVertex();
		vertexBuffer.pos(-7.0D, 2.0D, -2.0D).tex(f6, f9).endVertex();
		tessellator.draw();
		GL11.glNormal3f(-f10, 0.0F, 0.0F);
		// vertexBuffer.startDrawingQuads();
		// vertexBuffer.addVertexWithUV(-7.0D, 2.0D, -2.0D, f6, f8);
		// vertexBuffer.addVertexWithUV(-7.0D, 2.0D, 2.0D, f7, f8);
		// vertexBuffer.addVertexWithUV(-7.0D, -2.0D, 2.0D, f7, f9);
		// vertexBuffer.addVertexWithUV(-7.0D, -2.0D, -2.0D, f6, f9);
		vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		vertexBuffer.pos(-7.0D, 2.0D, -2.0D).tex(f6, f8).endVertex();
		vertexBuffer.pos(-7.0D, 2.0D, 2.0D).tex(f7, f8).endVertex();
		vertexBuffer.pos(-7.0D, -2.0D, 2.0D).tex(f7, f9).endVertex();
		vertexBuffer.pos(-7.0D, -2.0D, -2.0D).tex(f6, f9).endVertex();
		tessellator.draw();

		for (int i = 0; i < 4; ++i) {
			GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			GL11.glNormal3f(0.0F, 0.0F, f10);
			// vertexBuffer.startDrawingQuads();
			// vertexBuffer.addVertexWithUV(-8.0D, -2.0D, 0.0D, f2, f4);
			// vertexBuffer.addVertexWithUV(8.0D, -2.0D, 0.0D, f3, f4);
			// vertexBuffer.addVertexWithUV(8.0D, 2.0D, 0.0D, f3, f5);
			// vertexBuffer.addVertexWithUV(-8.0D, 2.0D, 0.0D, f2, f5);
			vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
			vertexBuffer.pos(-8.0D, -2.0D, 0.0D).tex(f2, f4).endVertex();
			vertexBuffer.pos(8.0D, -2.0D, 0.0D).tex(f3, f4).endVertex();
			vertexBuffer.pos(8.0D, 2.0D, 0.0D).tex(f3, f5).endVertex();
			vertexBuffer.pos(-8.0D, 2.0D, 0.0D).tex(f2, f5).endVertex();
			tessellator.draw();
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(final T entity) {
		return BOLT_TEXTURE;
	}
}