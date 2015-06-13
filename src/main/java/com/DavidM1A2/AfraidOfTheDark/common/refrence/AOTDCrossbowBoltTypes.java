package com.DavidM1A2.AfraidOfTheDark.common.refrence;

public enum AOTDCrossbowBoltTypes
{
	wooden(0), iron(1), silver(2);

	private int id = 0;

	private AOTDCrossbowBoltTypes(int id)
	{
		this.id = id;
	}

	public static int getIDFromType(AOTDCrossbowBoltTypes crossbowType)
	{
		return crossbowType.id;
	}

	public static AOTDCrossbowBoltTypes getTypeFromID(int id)
	{
		for (AOTDCrossbowBoltTypes crossbowTypes : AOTDCrossbowBoltTypes.values())
		{
			if (crossbowTypes.id == id)
			{
				return crossbowTypes;
			}
		}
		return null;
	}

	public AOTDCrossbowBoltTypes next()
	{
		if (this == silver)
		{
			return wooden;
		}
		else
		{
			return getTypeFromID(this.id + 1);
		}
	}
}
