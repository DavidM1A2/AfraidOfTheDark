package com.DavidM1A2.AfraidOfTheDark.client.particleFX;

import net.minecraft.world.World;

public class Test extends AOTDParticleFX
{
	public Test(World world, double lastTickPosX, double lastTickPosY, double lastTickPosZ, double motionX, double motionY, double motionZ)
	{
		super(world, lastTickPosX, lastTickPosY, lastTickPosZ, motionX, motionY, motionZ);
		this.particleScale = 2.0F;
		this.setRBGColorF(0x88, 0x00, 0x88);
	}

	@Override
	public int getTextureIndex()
	{
		return 0;
	}
}
