/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */

package com.DavidM1A2.AfraidOfTheDark.client.particleFX;

import net.minecraft.world.World;

public class EnariaTeleport extends AOTDParticleFX
{
	public EnariaTeleport(World world, double lastTickPosX, double lastTickPosY, double lastTickPosZ, double motionX, double motionY, double motionZ)
	{
		super(world, lastTickPosX, lastTickPosY, lastTickPosZ, motionX, motionY, motionZ);
		this.motionX = 0;
		this.motionY = 0;
		this.motionZ = 0;
	}

	@Override
	public int getTextureIndex()
	{
		return 0;
	}

	@Override
	public void updateMotionXYZ()
	{
		this.motionY = this.motionY - 0.03f;
		this.motionY = this.motionY * 0.9800000190734863D;
	}
}