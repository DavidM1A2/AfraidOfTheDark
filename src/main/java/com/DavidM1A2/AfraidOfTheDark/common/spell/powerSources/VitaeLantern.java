/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;
import com.DavidM1A2.AfraidOfTheDark.common.utility.VitaeUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class VitaeLantern extends PowerSource
{
	@Override
	public boolean attemptToCast(Spell toCast)
	{
		double cost = toCast.getCost();
		EntityPlayer entityPlayer = AfraidOfTheDark.proxy.getSpellOwner(toCast);
		return VitaeUtils.consumeVitaeFromLanterns(entityPlayer, cost);
	}

	@Override
	public String notEnoughEnergyMsg()
	{
		return "I do not have enough vitae in my lantern(s) to cast this.";
	}

	@Override
	public PowerSources getType()
	{
		return PowerSources.VitaeLantern;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
	}
}
