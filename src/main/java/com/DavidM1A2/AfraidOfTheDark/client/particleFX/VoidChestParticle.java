package com.DavidM1A2.AfraidOfTheDark.client.particleFX;

import net.minecraft.world.World;

public class VoidChestParticle extends AOTDParticleFX
{
	public VoidChestParticle(World world, double lastTickPosX, double lastTickPosY, double lastTickPosZ, double motionX, double motionY, double motionZ)
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

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate()
	{
		super.onUpdate();
		this.motionX = 0;
		this.motionY = 0;
		this.motionZ = 0;
	}
}
