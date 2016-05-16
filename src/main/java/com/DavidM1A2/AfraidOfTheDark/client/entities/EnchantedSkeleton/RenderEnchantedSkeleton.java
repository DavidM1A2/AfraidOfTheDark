/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.entities.EnchantedSkeleton;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.common.entities.EnchantedSkeleton.EntityEnchantedSkeleton;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderEnchantedSkeleton<T extends EntityEnchantedSkeleton> extends RenderLiving<T>
{
	public static final ResourceLocation ENCHANTED_SKELETON_TEXTURE = new ResourceLocation("afraidofthedark:textures/entity/enchantedSkeleton.png");
	public static ModelEnchantedSkeleton modelEnchantedSkeleton = new ModelEnchantedSkeleton();
	public static float modelHeight = 2.9F;

	public RenderEnchantedSkeleton(RenderManager renderManager)
	{
		super(renderManager, modelEnchantedSkeleton, 0.5F);
	}

	@Override
	public void doRender(T _entity, double posX, double posY, double posZ, float var8, float var9)
	{
		EntityEnchantedSkeleton entity = (EntityEnchantedSkeleton) _entity;

		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		super.doRender(_entity, posX, posY, posZ, var8, var9);
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
		return ENCHANTED_SKELETON_TEXTURE;
	}
}