package com.DavidM1A2.AfraidOfTheDark.client.particleFX;

import net.minecraft.world.World;

public class VitaeReleased extends AOTDParticleFX
{
	public VitaeReleased(World world, double lastTickPosX, double lastTickPosY, double lastTickPosZ, double motionX, double motionY, double motionZ)
	{
		super(world, lastTickPosX, lastTickPosY, lastTickPosZ, motionX, motionY, motionZ);
		this.motionX = (this.rand.nextDouble() - 0.5) / 100D;
		this.motionY = (this.rand.nextDouble() - 0.5) / 100D;
		this.motionZ = (this.rand.nextDouble() - 0.5) / 100D;
	}

	@Override
	public int getTextureIndex()
	{
		return 2;
	}

	@Override
	public void updateMotionXYZ()
	{
		this.motionX *= 0.9800000190734863D;
		this.motionY *= 0.9800000190734863D;
		this.motionZ *= 0.9800000190734863D;
	}
}