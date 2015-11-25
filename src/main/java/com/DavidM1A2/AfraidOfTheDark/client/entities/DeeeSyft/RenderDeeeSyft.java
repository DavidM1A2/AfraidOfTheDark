/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.entities.DeeeSyft;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.common.entities.DeeeSyft.EntityDeeeSyft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class RenderDeeeSyft extends RenderLiving
{

	public static final ResourceLocation DEEESYFT_TEXTURE = new ResourceLocation("afraidofthedark:textures/entity/deeeSyft.png");
	public static ModelDeeeSyft modelDeeeSyft = new ModelDeeeSyft();
	public static float modelHeight = -0.5F;

	public RenderDeeeSyft()
	{
		super(Minecraft.getMinecraft().getRenderManager(), modelDeeeSyft, 1F);
	}

	@Override
	public void doRender(Entity _entity, double posX, double posY, double posZ, float var8, float var9)
	{
		EntityDeeeSyft entity = (EntityDeeeSyft) _entity;

		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		super.doRender(_entity, posX, posY, posZ, var8, var9);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}

	@Override
	protected void preRenderCallback(EntityLivingBase entityliving, float f)
	{
		GL11.glRotatef(180F, 0, 1F, 0F);
		GL11.glRotatef(180F, 0, 0, 1F);
		GL11.glTranslatef(-1, modelHeight, -1);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity var1)
	{
		return DEEESYFT_TEXTURE;
	}
}