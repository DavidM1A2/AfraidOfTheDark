package com.DavidM1A2.AfraidOfTheDark.client.entities.SplinterDrone;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.common.entities.SplinterDrone.EntitySplinterDrone;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderSplinterDrone<T extends EntitySplinterDrone> extends RenderLiving<T>
{
	private static final ResourceLocation SPLINTER_DRONE_TEXTURE = new ResourceLocation("afraidofthedark:textures/entity/splinter_drone.png");
	private static ModelSplinterDrone modelSplinterDrone = new ModelSplinterDrone();
	private static float modelHeight = 3.1F;

	public RenderSplinterDrone(RenderManager renderManager)
	{
		super(renderManager, modelSplinterDrone, 0.3F);
	}

	@Override
	public void doRender(T entity, double posX, double posY, double posZ, float var8, float var9)
	{
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		super.doRender(entity, posX, posY, posZ, var8, var9);
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
		return SPLINTER_DRONE_TEXTURE;
	}
}