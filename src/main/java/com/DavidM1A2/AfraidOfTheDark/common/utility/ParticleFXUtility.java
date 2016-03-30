
package com.DavidM1A2.AfraidOfTheDark.common.utility;

import com.DavidM1A2.AfraidOfTheDark.client.particleFX.AOTDParticleFX;
import com.DavidM1A2.AfraidOfTheDark.client.particleFX.EnariaBasicAttack;
import com.DavidM1A2.AfraidOfTheDark.client.particleFX.EnariaTeleport;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDParticleFXTypes;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

public class ParticleFXUtility
{
	public static void generateParticles(World world, double x, double y, double z, AOTDParticleFXTypes particleType)
	{
		AOTDParticleFX particleFX = null;

		try
		{
			switch (particleType)
			{
				case EnariaBasicAttack:
					particleFX = new EnariaBasicAttack(world, x, y, z, 0, 0, 0);
					break;
				case EnariaTeleport:
					particleFX = new EnariaTeleport(world, x, y, z, 0, 0, 0);
					break;
				default:
					break;
			}

			Minecraft.getMinecraft().effectRenderer.addEffect(particleFX);
		}
		catch (Exception e)
		{
			LogHelper.info("Error loading particle FX.... see ParticleFXUtility.");
			e.printStackTrace();
		}
	}
}
