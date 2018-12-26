/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class Experience extends PowerSource
{
	private static final double LEVEL_PER_UNIT_COST = 0.5;

	@Override
	public boolean attemptToCast(Spell toCast)
	{
		EntityPlayer entityPlayer = AfraidOfTheDark.proxy.getSpellOwner(toCast);
		int xpLevelCost = (int) Math.ceil(toCast.getCost() * LEVEL_PER_UNIT_COST);
		if (xpLevelCost <= entityPlayer.experienceLevel)
		{
			entityPlayer.addExperienceLevel(-xpLevelCost);
			return true;
		}
		return false;
	}

	@Override
	public String notEnoughEnergyMsg()
	{
		return "You do not have enough XP to perform this spell";
	}

	@Override
	public PowerSources getType()
	{
		return PowerSources.Experience;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{

	}
}
