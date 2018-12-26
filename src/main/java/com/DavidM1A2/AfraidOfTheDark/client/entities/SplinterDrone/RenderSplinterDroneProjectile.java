package com.DavidM1A2.AfraidOfTheDark.client.entities.SplinterDrone;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.common.entities.SplinterDrone.EntitySplinterDroneProjectile;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderSplinterDroneProjectile<T extends EntitySplinterDroneProjectile> extends Render<T> {
	public static final ResourceLocation SPLINTER_DRONE_PROJECTILE_TEXTURE = new ResourceLocation(
			"afraidofthedark:textures/entity/splinterDroneProjectile.png");
	public static ModelSplinterDroneProjectile modelSplinterDroneProjectile;

	public RenderSplinterDroneProjectile(RenderManager renderManager) {
		super(renderManager);
		this.modelSplinterDroneProjectile = new ModelSplinterDroneProjectile();
	}

	@Override
	public void doRender(T _entity, double posX, double posY, double posZ, float var8, float var9) {
		EntitySplinterDroneProjectile entity = (EntitySplinterDroneProjectile) _entity;

		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glTranslatef((float) posX, (float) posY + 0.2f, (float) posZ);
		this.bindEntityTexture(_entity);
		this.modelSplinterDroneProjectile.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(T var1) {
		return SPLINTER_DRONE_PROJECTILE_TEXTURE;
	}
}