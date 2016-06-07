/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources;

import com.DavidM1A2.AfraidOfTheDark.common.spell.ISpellComponentEnum;

public enum PowerSources implements ISpellComponentEnum
{
	Self(1, "self.png")
	{
		@Override
		public PowerSource newInstance()
		{
			return new Self();
		}
	},
	VitaeLantern(2, "vitaeLantern.png")
	{
		@Override
		public PowerSource newInstance()
		{
			return new VitaeLantern();
		}
	},
	Creative(3, "creative.png")
	{
		@Override
		public PowerSource newInstance()
		{
			return new Creative();
		}
	},
	Experience(4, "experience.png")
	{
		@Override
		public PowerSource newInstance()
		{
			return new Experience();
		}
	};

	private int id;
	private String textureLocation;

	private PowerSources(int id, String textureLocation)
	{
		this.id = id;
		this.textureLocation = "afraidofthedark:textures/gui/spellCrafting/powerSources/" + textureLocation;
	}

	public int getID()
	{
		return this.id;
	}

	public String getIcon()
	{
		return this.textureLocation;
	}

	@Override
	public String getName()
	{
		return this.toString();
	}

	@Override
	public abstract PowerSource newInstance();

	@Override
	public String getNameFormatted()
	{
		String toReturn = "";

		for (final String string : this.getName().split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])"))
		{
			toReturn = toReturn + string + " ";
		}

		if (toReturn.charAt(toReturn.length() - 1) == ' ')
			toReturn = toReturn.substring(0, toReturn.length() - 1);

		return toReturn;
	}
}
