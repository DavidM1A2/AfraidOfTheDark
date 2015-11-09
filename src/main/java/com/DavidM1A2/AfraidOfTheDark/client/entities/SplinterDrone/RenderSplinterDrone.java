package com.DavidM1A2.AfraidOfTheDark.client.entities.SplinterDrone;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.common.entities.SplinterDrone.EntitySplinterDrone;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class RenderSplinterDrone extends RenderLiving
{

	public static final ResourceLocation SplinterDrone_texture = new ResourceLocation("afraidofthedark:textures/entity/SplinterDrone.png");
	public static ModelSplinterDrone modelSplinterDrone = new ModelSplinterDrone();
	public static float modelHeight = 3.1F;

	public RenderSplinterDrone()
	{
		super(Minecraft.getMinecraft().getRenderManager(), modelSplinterDrone, 1F);
	}

	@Override
	public void doRender(Entity _entity, double posX, double posY, double posZ, float var8, float var9)
	{
		EntitySplinterDrone entity = (EntitySplinterDrone) _entity;

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
		return SplinterDrone_texture;
	}
}