/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.refrence;

public enum MeteorTypes
{
	silver(0), starMetal(1), sunstone(2);

	private final int index;

	private MeteorTypes(final int index)
	{
		this.index = index;
	}

	public int getIndex()
	{
		return this.index;
	}

	public static MeteorTypes typeFromIndex(final int index)
	{
		for (final MeteorTypes type : MeteorTypes.values())
		{
			if (type.index == index)
			{
				return type;
			}
		}
		return null;
	}

	public String formattedString()
	{
		String toReturn = "";

		for (final String string : this.toString().split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])"))
		{
			toReturn = toReturn + string + " ";
		}

		return toReturn;
	}
}
