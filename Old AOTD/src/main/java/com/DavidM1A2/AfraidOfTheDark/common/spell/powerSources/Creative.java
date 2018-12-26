package com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources;

import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;

import net.minecraft.nbt.NBTTagCompound;

public class Creative extends PowerSource
{
	@Override
	public boolean attemptToCast(Spell toCast)
	{
		return true;
	}

	@Override
	public String notEnoughEnergyMsg()
	{
		return "Wtf? How did you use creative with infinite energy and not cast the spell? ";
	}

	@Override
	public PowerSources getType()
	{
		return PowerSources.Creative;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
	}
}
