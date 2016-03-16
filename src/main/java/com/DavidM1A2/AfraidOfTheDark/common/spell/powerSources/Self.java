package com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources;

import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;

public class Self implements IPowerSource
{
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
}
