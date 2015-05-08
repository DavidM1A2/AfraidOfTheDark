package com.DavidM1A2.AfraidOfTheDark.refrence;

import net.minecraft.util.IStringSerializable;

public enum AOTDTreeTypes implements IStringSerializable
{
	GRAVEWOOD;

	public Integer getMetadata()
	{
		if (this == GRAVEWOOD)
		{
			return 0;
		}
		return null;
	}

	@Override
	public String toString()
	{
		if (this == GRAVEWOOD)
		{
			return "gravewood";
		}
		return null;
	}

	@Override
	public String getName()
	{
		return this.toString();
	}
}
