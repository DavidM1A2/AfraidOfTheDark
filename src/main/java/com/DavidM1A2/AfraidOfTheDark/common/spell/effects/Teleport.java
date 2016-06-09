/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.spell.SpellHitInfo;
import com.DavidM1A2.AfraidOfTheDark.common.utility.VitaeUtils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class Teleport extends Effect
{
	@Override
	public int getCost()
	{
		return 8;
	}

	@Override
	public void performEffect(SpellHitInfo hitInfo)
	{
		Entity hitEntity = hitInfo.getEntityHit();
		if (hitEntity != null)
			this.teleport(hitInfo.getWorld(), hitInfo.getSpellCaster(), hitEntity.getPosition());
		else
		{
			Random RNG = hitInfo.getWorld().rand;
			int radius = hitInfo.getRadius();
			if (radius < 1)
				radius = 1;
			int xToAdd = radius - RNG.nextInt(2 * radius);
			int yToAdd = radius - RNG.nextInt(2 * radius);
			int zToAdd = radius - RNG.nextInt(2 * radius);
			this.teleport(hitInfo.getWorld(), hitInfo.getSpellCaster(), hitInfo.getLocation().add(xToAdd, yToAdd, zToAdd));
		}
	}

	private void teleport(World world, EntityPlayer toTeleport, BlockPos locationTo)
	{
		if (!world.isRemote)
		{
			VitaeUtils.vitaeReleasedFX(world, toTeleport.getPosition(), 1, 10);
			((EntityPlayerMP) toTeleport).playerNetServerHandler.setPlayerLocation(locationTo.getX(), locationTo.getY(), locationTo.getZ(), toTeleport.rotationYaw, toTeleport.rotationPitch);
			VitaeUtils.vitaeReleasedFX(world, locationTo, 1, 10);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
	}

	@Override
	public Effects getType()
	{
		return Effects.Teleport;
	}
}
