/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.entities.EnchantedSkeleton;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.common.entities.EnchantedSkeleton.EntityEnchantedSkeleton;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class RenderEnchantedSkeleton extends RenderLiving
{

	public static final ResourceLocation Enchanted_Sekeleton_Texture = new ResourceLocation("afraidofthedark:textures/entity/EnchantedSkeleton.png");
	public static ModelEnchantedSkeleton modelEnchantedSkeleton = new ModelEnchantedSkeleton();
	public static float modelHeight = 2.9F;

	public RenderEnchantedSkeleton()
	{
		super(Minecraft.getMinecraft().getRenderManager(), modelEnchantedSkeleton, 1.0F);
	}

	@Override
	public void doRender(Entity _entity, double posX, double posY, double posZ, float var8, float var9)
	{
		EntityEnchantedSkeleton entity = (EntityEnchantedSkeleton) _entity;

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
		GL11.glTranslatef(0, modelHeight, 0);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity var1)
	{
		return Enchanted_Sekeleton_Texture;
	}
}