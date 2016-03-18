package com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources;

import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;

import net.minecraft.nbt.NBTTagCompound;

public class Self extends PowerSource
{
	public static final int id = 1;

	@Override
	public boolean attemptToCast(Spell toCast)
	{
		return true;
	}

	@Override
	public String notEnoughEnergyMsg()
	{
		return "I do not have enough vitae to cast this spell.";
	}

	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setInteger("id", PowerSources.Self.getID());
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		// TODO Auto-generated method stub

	}
}
