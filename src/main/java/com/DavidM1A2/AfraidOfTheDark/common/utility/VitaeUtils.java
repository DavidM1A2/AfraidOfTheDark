/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.utility;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemVitaeLantern;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDParticleFXTypes;
import com.DavidM1A2.AfraidOfTheDark.proxy.ClientProxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class VitaeUtils
{
	public static void vitaeReleasedFX(World world, BlockPos location, double radius, int numberOfParticles)
	{
		for (int i = 0; i < numberOfParticles; i++)
		{
			BlockPos particleLocation = location.add((world.rand.nextDouble() - 0.5) * radius * 2, (world.rand.nextDouble() - 0.5) * radius * 2, (world.rand.nextDouble() - 0.5) * radius * 2);
			if (world.isRemote || AfraidOfTheDark.proxy instanceof ClientProxy)
				AOTDParticleFXTypes.VitaeReleased.instantiateClient(world, particleLocation.getX() + world.rand.nextDouble(), particleLocation.getY() + world.rand.nextDouble(), particleLocation.getZ() + world.rand.nextDouble(), 0, 0, 0);
			else
				AOTDParticleFXTypes.VitaeReleased.instantiateServer(world, particleLocation.getX() + world.rand.nextDouble(), particleLocation.getY() + world.rand.nextDouble(), particleLocation.getZ() + world.rand.nextDouble(), 150);
		}
	}

	public static double getVitaeSumOfAllLanterns(EntityPlayer entityPlayer)
	{
		double totalLanternVitae = 0;
		for (int i = 0; i < entityPlayer.inventory.mainInventory.size(); i++)
		{
			ItemStack current = entityPlayer.inventory.mainInventory.get(i);
			if (current != null && current.getItem() instanceof ItemVitaeLantern)
			{
				ItemVitaeLantern lantern = (ItemVitaeLantern) current.getItem();
				totalLanternVitae = totalLanternVitae + lantern.getStoredVitae(current);
			}
		}
		return totalLanternVitae;
	}

	public static boolean canConsumeVitaeFromLanterns(EntityPlayer entityPlayer, double vitaeAmount)
	{
		return VitaeUtils.getVitaeSumOfAllLanterns(entityPlayer) >= vitaeAmount;
	}

	public static boolean consumeVitaeFromLanterns(EntityPlayer entityPlayer, double vitaeAmount)
	{
		if (!VitaeUtils.canConsumeVitaeFromLanterns(entityPlayer, vitaeAmount))
			return false;

		for (int i = 0; i < entityPlayer.inventory.mainInventory.size(); i++)
		{
			ItemStack current = entityPlayer.inventory.mainInventory.get(i);
			if (current != null && current.getItem() instanceof ItemVitaeLantern)
			{
				ItemVitaeLantern lantern = (ItemVitaeLantern) current.getItem();
				if (vitaeAmount - lantern.getStoredVitae(current) > 0)
				{
					vitaeAmount = vitaeAmount - lantern.getStoredVitae(current);
					lantern.addVitae(current, -lantern.getStoredVitae(current));
				}
				else
				{
					lantern.addVitae(current, (int) -vitaeAmount);
					return true;
				}
			}
		}

		return false;
	}
}
