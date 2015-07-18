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
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class AOTDParticleFX extends EntityFX
{
	private static final ResourceLocation customParticleTextures = new ResourceLocation("afraidofthedark:textures/particles/test.png");
	private static final ResourceLocation defaultParticleTextures = new ResourceLocation("textures/particle/particles.png");

	public AOTDParticleFX(World world, double lastTickPosX, double lastTickPosY, double lastTickPosZ, double motionX, double motionY, double motionZ)
	{
		super(world, lastTickPosX, lastTickPosY, lastTickPosZ, motionX, motionY, motionZ);
		this.setParticleTextureIndex(this.getTextureIndex());
	}

	public abstract int getTextureIndex();

	@Override
	public void func_180434_a(WorldRenderer worldRenderer, Entity entity, float float1, float float2, float float3, float float4, float float5, float float6)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(customParticleTextures);

		float f6 = this.particleTextureIndexX / 16.0F;
		float f7 = f6 + 0.0624375F;
		float f8 = this.particleTextureIndexY / 16.0F;
		float f9 = f8 + 0.0624375F;
		float f10 = 0.1F * this.particleScale;

		if (this.particleIcon != null)
		{
			f6 = this.particleIcon.getMinU();
			f7 = this.particleIcon.getMaxU();
			f8 = this.particleIcon.getMinV();
			f9 = this.particleIcon.getMaxV();
		}

		float f11 = (float) (this.prevPosX + (this.posX - this.prevPosX) * float1 - interpPosX);
		float f12 = (float) (this.prevPosY + (this.posY - this.prevPosY) * float1 - interpPosY);
		float f13 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * float1 - interpPosZ);
		worldRenderer.func_178960_a(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
		worldRenderer.addVertexWithUV(f11 - float2 * f10 - float5 * f10, f12 - float3 * f10, f13 - float4 * f10 - float6 * f10, f7, f9);
		worldRenderer.addVertexWithUV(f11 - float2 * f10 + float5 * f10, f12 + float3 * f10, f13 - float4 * f10 + float6 * f10, f7, f8);
		worldRenderer.addVertexWithUV(f11 + float2 * f10 + float5 * f10, f12 + float3 * f10, f13 + float4 * f10 + float6 * f10, f6, f8);
		worldRenderer.addVertexWithUV(f11 + float2 * f10 - float5 * f10, f12 - float3 * f10, f13 + float4 * f10 - float6 * f10, f6, f9);

		Tessellator.getInstance().draw();

		worldRenderer.startDrawingQuads();
		Minecraft.getMinecraft().renderEngine.bindTexture(defaultParticleTextures);
	}
}
