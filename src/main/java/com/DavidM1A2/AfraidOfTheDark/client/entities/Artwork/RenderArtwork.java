/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.entities.Artwork;

import com.DavidM1A2.AfraidOfTheDark.common.entities.EntityArtwork.EntityArtwork;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class RenderArtwork extends Render<EntityArtwork> {
	public RenderArtwork(RenderManager renderManagerIn) {
		super(renderManagerIn);
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method,
	 * always casting down its argument and then handing it off to a worker
	 * function which does the actual work. In all probabilty, the class Render
	 * is generic (Render<T extends Entity>) and this method has signature
	 * public void func_76986_a(T entity, double d, double d1, double d2, float
	 * f, float f1). But JAD is pre 1.5 so doe
	 */
	public void doRender(EntityArtwork entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		EnumFacing facing = entity.getHorizontalFacing();
		int var = entity.blocksToTakeUp() % 2 == 0 ? entity.blocksToTakeUp() : entity.blocksToTakeUp() + 1;
		var = var / 2 + 8 - entity.blocksToTakeUp();
		float transX = var;
		float transY = var;
		float transZ = var;
		if (facing == EnumFacing.WEST) {
			transZ = -transZ;
			transX = 0;
			if (entity.blocksToTakeUp() % 2 != 0)
				transY = transY - 0.5f;
			if (entity.blocksToTakeUp() % 2 != 0)
				transZ = transZ + 0.5f;
		} else if (facing == EnumFacing.EAST) {
			transX = 0;
			if (entity.blocksToTakeUp() % 2 != 0)
				transY = transY - 0.5f;
			if (entity.blocksToTakeUp() % 2 != 0)
				transZ = transZ - 0.5f;
		} else if (facing == EnumFacing.NORTH) {
			transZ = 0;
			if (entity.blocksToTakeUp() % 2 != 0)
				transX = transX - 0.5f;
			if (entity.blocksToTakeUp() % 2 != 0)
				transY = transY - 0.5f;
		} else if (facing == EnumFacing.SOUTH) {
			transX = -transX;
			transZ = 0;
			if (entity.blocksToTakeUp() % 2 != 0)
				transX = transX + 0.5f;
			if (entity.blocksToTakeUp() % 2 != 0)
				transY = transY - 0.5f;
		}

		GlStateManager.translate(transX, transY, transZ);
		GlStateManager.rotate(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.enableRescaleNormal();
		this.bindEntityTexture(entity);
		float f = 0.0625F;
		GlStateManager.scale(f, f, f);
		this.renderPainting(entity, entity.getWidthPixels(), entity.getHeightPixels(), 0, 0);
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityArtwork entity) {
		return entity.getArt().getTexture();
	}

	private void renderPainting(EntityArtwork painting, int width, int height, int textureU, int textureV) {
		float f = (float) (-width) / 2.0F;
		float f1 = (float) (-height) / 2.0F;
		float f2 = 0.5F;
		float f3 = 0.75F;
		float f4 = 0.8125F;
		float f5 = 0.0F;
		float f6 = 0.0625F;
		float f7 = 0.75F;
		float f8 = 0.8125F;
		float f9 = 0.001953125F;
		float f10 = 0.001953125F;
		float f11 = 0.7519531F;
		float f12 = 0.7519531F;
		float f13 = 0.0F;
		float f14 = 0.0625F;

		for (int i = 0; i < width / 16; ++i) {
			for (int j = 0; j < height / 16; ++j) {
				float f15 = f + (float) ((i + 1) * painting.blocksToTakeUp());
				float f16 = f + (float) (i * painting.blocksToTakeUp());
				float f17 = f1 + (float) ((j + 1) * painting.blocksToTakeUp());
				float f18 = f1 + (float) (j * painting.blocksToTakeUp());
				this.setLightmap(painting, (f15 + f16) / 2.0F, (f17 + f18) / 2.0F);
				float f19 = (float) (textureU + width - i * 16) / 256.0F;
				float f20 = (float) (textureU + width - (i + 1) * 16) / 256.0F;
				float f21 = (float) (textureV + height - j * 16) / 256.0F;
				float f22 = (float) (textureV + height - (j + 1) * 16) / 256.0F;
				Tessellator tessellator = Tessellator.getInstance();
				VertexBuffer vertexBuffer = tessellator.getBuffer();
				vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
				vertexBuffer.pos((double) f15, (double) f18, (double) (-f2)).tex((double) f20, (double) f21)
						.normal(0.0F, 0.0F, -1.0F).endVertex();
				vertexBuffer.pos((double) f16, (double) f18, (double) (-f2)).tex((double) f19, (double) f21)
						.normal(0.0F, 0.0F, -1.0F).endVertex();
				vertexBuffer.pos((double) f16, (double) f17, (double) (-f2)).tex((double) f19, (double) f22)
						.normal(0.0F, 0.0F, -1.0F).endVertex();
				vertexBuffer.pos((double) f15, (double) f17, (double) (-f2)).tex((double) f20, (double) f22)
						.normal(0.0F, 0.0F, -1.0F).endVertex();
				vertexBuffer.pos((double) f15, (double) f17, (double) f2).tex((double) f3, (double) f5)
						.normal(0.0F, 0.0F, 1.0F).endVertex();
				vertexBuffer.pos((double) f16, (double) f17, (double) f2).tex((double) f4, (double) f5)
						.normal(0.0F, 0.0F, 1.0F).endVertex();
				vertexBuffer.pos((double) f16, (double) f18, (double) f2).tex((double) f4, (double) f6)
						.normal(0.0F, 0.0F, 1.0F).endVertex();
				vertexBuffer.pos((double) f15, (double) f18, (double) f2).tex((double) f3, (double) f6)
						.normal(0.0F, 0.0F, 1.0F).endVertex();
				vertexBuffer.pos((double) f15, (double) f17, (double) (-f2)).tex((double) f7, (double) f9)
						.normal(0.0F, 1.0F, 0.0F).endVertex();
				vertexBuffer.pos((double) f16, (double) f17, (double) (-f2)).tex((double) f8, (double) f9)
						.normal(0.0F, 1.0F, 0.0F).endVertex();
				vertexBuffer.pos((double) f16, (double) f17, (double) f2).tex((double) f8, (double) f10)
						.normal(0.0F, 1.0F, 0.0F).endVertex();
				vertexBuffer.pos((double) f15, (double) f17, (double) f2).tex((double) f7, (double) f10)
						.normal(0.0F, 1.0F, 0.0F).endVertex();
				vertexBuffer.pos((double) f15, (double) f18, (double) f2).tex((double) f7, (double) f9)
						.normal(0.0F, -1.0F, 0.0F).endVertex();
				vertexBuffer.pos((double) f16, (double) f18, (double) f2).tex((double) f8, (double) f9)
						.normal(0.0F, -1.0F, 0.0F).endVertex();
				vertexBuffer.pos((double) f16, (double) f18, (double) (-f2)).tex((double) f8, (double) f10)
						.normal(0.0F, -1.0F, 0.0F).endVertex();
				vertexBuffer.pos((double) f15, (double) f18, (double) (-f2)).tex((double) f7, (double) f10)
						.normal(0.0F, -1.0F, 0.0F).endVertex();
				vertexBuffer.pos((double) f15, (double) f17, (double) f2).tex((double) f12, (double) f13)
						.normal(-1.0F, 0.0F, 0.0F).endVertex();
				vertexBuffer.pos((double) f15, (double) f18, (double) f2).tex((double) f12, (double) f14)
						.normal(-1.0F, 0.0F, 0.0F).endVertex();
				vertexBuffer.pos((double) f15, (double) f18, (double) (-f2)).tex((double) f11, (double) f14)
						.normal(-1.0F, 0.0F, 0.0F).endVertex();
				vertexBuffer.pos((double) f15, (double) f17, (double) (-f2)).tex((double) f11, (double) f13)
						.normal(-1.0F, 0.0F, 0.0F).endVertex();
				vertexBuffer.pos((double) f16, (double) f17, (double) (-f2)).tex((double) f12, (double) f13)
						.normal(1.0F, 0.0F, 0.0F).endVertex();
				vertexBuffer.pos((double) f16, (double) f18, (double) (-f2)).tex((double) f12, (double) f14)
						.normal(1.0F, 0.0F, 0.0F).endVertex();
				vertexBuffer.pos((double) f16, (double) f18, (double) f2).tex((double) f11, (double) f14)
						.normal(1.0F, 0.0F, 0.0F).endVertex();
				vertexBuffer.pos((double) f16, (double) f17, (double) f2).tex((double) f11, (double) f13)
						.normal(1.0F, 0.0F, 0.0F).endVertex();
				tessellator.draw();
			}
		}
	}

	private void setLightmap(EntityArtwork painting, float p_77008_2_, float p_77008_3_) {
		int i = MathHelper.floor_double(painting.posX);
		int j = MathHelper.floor_double(painting.posY + (double) (p_77008_3_ / 16.0F));
		int k = MathHelper.floor_double(painting.posZ);
		EnumFacing enumfacing = painting.facingDirection;

		if (enumfacing == EnumFacing.NORTH) {
			i = MathHelper.floor_double(painting.posX + (double) (p_77008_2_ / 16.0F));
		}

		if (enumfacing == EnumFacing.WEST) {
			k = MathHelper.floor_double(painting.posZ - (double) (p_77008_2_ / 16.0F));
		}

		if (enumfacing == EnumFacing.SOUTH) {
			i = MathHelper.floor_double(painting.posX - (double) (p_77008_2_ / 16.0F));
		}

		if (enumfacing == EnumFacing.EAST) {
			k = MathHelper.floor_double(painting.posZ + (double) (p_77008_2_ / 16.0F));
		}

		int l = this.renderManager.worldObj.getCombinedLight(new BlockPos(i, j, k), 0);
		int i1 = l % 65536;
		int j1 = l / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) i1, (float) j1);
		GlStateManager.color(1.0F, 1.0F, 1.0F);
	}
}
