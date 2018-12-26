/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.entities.Enaria;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria.ghastly.EntityGhastlyEnaria;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderGhastlyEnaria<T extends EntityGhastlyEnaria> extends RenderLiving<T>
{
	public static final ResourceLocation ENARIA_TEXTURE = new ResourceLocation("afraidofthedark:textures/entity/enaria.png");
	public static ModelGhastlyEnaria modelEnaria = new ModelGhastlyEnaria();
	public static float modelHeight = 2.8F;

	public RenderGhastlyEnaria(RenderManager renderManager)
	{
		super(renderManager, modelEnaria, 0F);
	}

	@Override
	public void doRender(T _entity, double posX, double posY, double posZ, float var8, float var9)
	{
		EntityGhastlyEnaria entity = (EntityGhastlyEnaria) _entity;

		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.3f);
		super.doRender(_entity, posX, posY, posZ, var8, var9);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}

	@Override
	protected void preRenderCallback(T entityliving, float f)
	{
		GL11.glRotatef(180F, 0, 1F, 0F);
		GL11.glRotatef(180F, 0, 0, 1F);
		GL11.glTranslatef(0, modelHeight, 0);
	}

	@Override
	protected ResourceLocation getEntityTexture(T var1)
	{
		return ENARIA_TEXTURE;
	}
}