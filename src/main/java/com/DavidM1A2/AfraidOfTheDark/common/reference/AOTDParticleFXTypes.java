
package com.DavidM1A2.AfraidOfTheDark.common.reference;

import com.DavidM1A2.AfraidOfTheDark.client.particleFX.AOTDParticleFX;
import com.DavidM1A2.AfraidOfTheDark.client.particleFX.EnariaBasicAttack;
import com.DavidM1A2.AfraidOfTheDark.client.particleFX.EnariaSplash;
import com.DavidM1A2.AfraidOfTheDark.client.particleFX.EnariaTeleport;
import com.DavidM1A2.AfraidOfTheDark.client.particleFX.VitaeReleased;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

public enum AOTDParticleFXTypes
{
	EnariaTeleport
	{
		@Override
		protected AOTDParticleFX newInstance(World world, double x, double y, double z, double motionX, double motionY, double motionZ)
		{
			return new EnariaTeleport(world, x, y, z, motionX, motionY, motionZ);
		}
	},
	EnariaBasicAttack
	{
		@Override
		protected AOTDParticleFX newInstance(World world, double x, double y, double z, double motionX, double motionY, double motionZ)
		{
			return new EnariaBasicAttack(world, x, y, z, motionX, motionY, motionZ);
		}
	},
	EnariaSplash
	{
		@Override
		protected AOTDParticleFX newInstance(World world, double x, double y, double z, double motionX, double motionY, double motionZ)
		{
			return new EnariaSplash(world, x, y, z, motionX, motionY, motionZ);
		}
	},
	VitaeReleased
	{
		@Override
		protected AOTDParticleFX newInstance(World world, double x, double y, double z, double motionX, double motionY, double motionZ)
		{
			return new VitaeReleased(world, x, y, z, motionX, motionY, motionZ);
		}
	};

	public int getIndex()
	{
		return this.ordinal();
	}

	public void instantiate(World world, double x, double y, double z, double motionX, double motionY, double motionZ)
	{
		if (world.isRemote)
		{
			try
			{
				AOTDParticleFX particleFX = this.newInstance(world, x, y, z, motionX, motionY, motionZ);
				particleFX.setPosition(x, y, z);
				Minecraft.getMinecraft().effectRenderer.addEffect(particleFX);
			}
			catch (Exception e)
			{
				LogHelper.error("Error loading particle FX.... see ParticleFXTypes.");
				e.printStackTrace();
			}
		}
	}

	protected abstract AOTDParticleFX newInstance(World world, double x, double y, double z, double motionX, double motionY, double motionZ);
}
