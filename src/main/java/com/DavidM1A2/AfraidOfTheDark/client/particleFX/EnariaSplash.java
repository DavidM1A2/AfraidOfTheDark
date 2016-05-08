/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.particleFX;

import net.minecraft.world.World;

public class EnariaSplash extends AOTDParticleFX
{
	public EnariaSplash(World world, double lastTickPosX, double lastTickPosY, double lastTickPosZ, double motionX, double motionY, double motionZ)
	{
		super(world, lastTickPosX, lastTickPosY, lastTickPosZ, motionX, motionY, motionZ);
		this.particleScale = 3.0f;
		this.motionX = 0.5 - world.rand.nextDouble();
		this.motionY = world.rand.nextDouble() * 0.5 + 0.5;
		this.motionZ = 0.5 - world.rand.nextDouble();
		this.motionX = this.motionX * 0.9;
		this.motionZ = this.motionZ * 0.9;
		this.particleMaxAge = 80;
	}

	@Override
	public int getTextureIndex()
	{
		return 2;
	}

	@Override
	public void updateMotionXYZ()
	{
		this.motionX = this.motionX * 0.95;
		this.motionY = this.motionY - 0.08;
		this.motionZ = this.motionZ * 0.95;
	}
}
