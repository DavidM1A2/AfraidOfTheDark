package com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class Self extends PowerSource
{
	@Override
	public boolean attemptToCast(Spell toCast)
	{
		double cost = toCast.getCost();
		EntityPlayer caster = AfraidOfTheDark.proxy.getSpellOwner(toCast);
		int storedVitae = caster.getCapability(ModCapabilities.ENTITY_DATA, null).getVitaeLevel();
		if (storedVitae >= cost)
			return true;
		else
			return false;
	}

	@Override
	public String notEnoughEnergyMsg()
	{
		return "I do not have enough vitae to cast this spell.";
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
	}

	@Override
	public PowerSources getType()
	{
		return PowerSources.Self;
	}
}
