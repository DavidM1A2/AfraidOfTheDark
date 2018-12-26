
package com.DavidM1A2.AfraidOfTheDark.common.reference;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.particleFX.AOTDParticleFX;
import com.DavidM1A2.AfraidOfTheDark.client.particleFX.EnariaBasicAttack;
import com.DavidM1A2.AfraidOfTheDark.client.particleFX.EnariaSplash;
import com.DavidM1A2.AfraidOfTheDark.client.particleFX.EnariaTeleport;
import com.DavidM1A2.AfraidOfTheDark.client.particleFX.VitaeReleased;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncParticleFX;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum AOTDParticleFXTypes
{
	EnariaTeleport
	{
		@Override
		@SideOnly(Side.CLIENT)
		protected AOTDParticleFX newInstance(World world, double x, double y, double z, double motionX, double motionY, double motionZ)
		{
			return new EnariaTeleport(world, x, y, z, motionX, motionY, motionZ);
		}
	},
	EnariaBasicAttack
	{
		@Override
		@SideOnly(Side.CLIENT)
		protected AOTDParticleFX newInstance(World world, double x, double y, double z, double motionX, double motionY, double motionZ)
		{
			return new EnariaBasicAttack(world, x, y, z, motionX, motionY, motionZ);
		}
	},
	EnariaSplash
	{
		@Override
		@SideOnly(Side.CLIENT)
		protected AOTDParticleFX newInstance(World world, double x, double y, double z, double motionX, double motionY, double motionZ)
		{
			return new EnariaSplash(world, x, y, z, motionX, motionY, motionZ);
		}
	},
	VitaeReleased
	{
		@Override
		@SideOnly(Side.CLIENT)
		protected AOTDParticleFX newInstance(World world, double x, double y, double z, double motionX, double motionY, double motionZ)
		{
			return new VitaeReleased(world, x, y, z, motionX, motionY, motionZ);
		}
	};

	public int getIndex()
	{
		return this.ordinal();
	}

	@SideOnly(Side.CLIENT)
	public void instantiateClient(World world, double x, double y, double z, double motionX, double motionY, double motionZ)
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

	@SideOnly(Side.SERVER)
	public void instantiateServer(World world, double x, double y, double z, int range)
	{
		AfraidOfTheDark.instance.getPacketHandler().sendToAllAround(new SyncParticleFX(this, x, y, z), world.provider.getDimensionId(), x, y, z, range);
	}

	@SideOnly(Side.CLIENT)
	protected abstract AOTDParticleFX newInstance(World world, double x, double y, double z, double motionX, double motionY, double motionZ);
}
