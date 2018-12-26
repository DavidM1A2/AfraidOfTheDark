
package com.DavidM1A2.AfraidOfTheDark.common.utility;

public class UnitConverterUtility
{
	public static int ticksToMilliseconds(int ticks)
	{
		return ticks * 50;
	}

	public static int millisecondsToTicks(int milliseconds)
	{
		return milliseconds / 50;
	}
}
