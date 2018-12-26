/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.entities.DeeeSyft;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.common.entities.DeeeSyft.EntityDeeeSyft;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderDeeeSyft<T extends EntityDeeeSyft> extends RenderLiving<T>
{
	private static final ResourceLocation DEEESYFT_TEXTURE = new ResourceLocation("afraidofthedark:textures/entity/deee_syft.png");
	private static ModelDeeeSyft modelDeeeSyft = new ModelDeeeSyft();
	private static float modelHeight = -0.5F;

	public RenderDeeeSyft(RenderManager renderManager)
	{
		super(renderManager, modelDeeeSyft, 1F);
	}

	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}

	@Override
	protected void preRenderCallback(T entityliving, float f)
	{
		GL11.glRotatef(180F, 0, 1F, 0F);
		GL11.glRotatef(180F, 0, 0, 1F);
		GL11.glTranslatef(-1, modelHeight, -1);
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity)
	{
		return DEEESYFT_TEXTURE;
	}
}