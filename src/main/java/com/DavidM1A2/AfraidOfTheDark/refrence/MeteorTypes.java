package com.DavidM1A2.AfraidOfTheDark.refrence;

public enum MeteorTypes
{
	silver,
	starMetal,
	sunstone;
	
	public String formattedString()
	{
		String toReturn = "";

		for (String string : this.toString().split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])"))
		{
			toReturn = toReturn + string + " ";
		}

		return toReturn;
	}
}
