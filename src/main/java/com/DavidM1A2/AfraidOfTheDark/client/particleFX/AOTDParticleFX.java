/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.particleFX;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class AOTDParticleFX extends EntityFX
{
	private static final ResourceLocation customParticleTextures = new ResourceLocation("afraidofthedark:textures/particles/AOTDParticles.png");
	private static final ResourceLocation defaultParticleTextures = new ResourceLocation("textures/particle/particles.png");

	public AOTDParticleFX(World world, double lastTickPosX, double lastTickPosY, double lastTickPosZ, double motionX, double motionY, double motionZ)
	{
		super(world, lastTickPosX, lastTickPosY, lastTickPosZ, motionX, motionY, motionZ);
		this.setParticleTextureIndex(this.getTextureIndex());
	}

	public abstract int getTextureIndex();

	@Override
	public void renderParticle(WorldRenderer worldRenderer, Entity entity, float partialTicks, float float2, float float3, float float4, float float5, float float6)
	{
		// Draw whatever we have currently loaded into memory
		Tessellator.getInstance().draw();
		Minecraft.getMinecraft().renderEngine.bindTexture(customParticleTextures);
		// Begin drawing our custom particle
		worldRenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);

		super.renderParticle(worldRenderer, entity, partialTicks, float2, float3, float4, float5, float6);

		// Draw our custom particle
		Tessellator.getInstance().draw();
		Minecraft.getMinecraft().renderEngine.bindTexture(defaultParticleTextures);
		// Begin drawing remaining particles
		worldRenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.particleAge++ >= this.particleMaxAge)
		{
			this.setDead();
		}

		this.updateMotionXYZ();
		this.moveEntity(this.motionX, this.motionY, this.motionZ);

		if (this.onGround)
		{
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
		}
	}

	public abstract void updateMotionXYZ();
}
