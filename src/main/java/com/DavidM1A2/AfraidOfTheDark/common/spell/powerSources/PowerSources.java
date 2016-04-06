/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources;

import com.DavidM1A2.AfraidOfTheDark.common.spell.ISpellComponentEnum;

public enum PowerSources implements ISpellComponentEnum
{
	Self(1, "afraidofthedark:textures/gui/spellCrafting/powerSources/self.png")
	{
		@Override
		public PowerSource newInstance()
		{
			return new Self();
		}
	},
	VitaeLantern(2, "afraidofthedark:textures/gui/spellCrafting/powerSources/vitaeLantern.png")
	{
		@Override
		public PowerSource newInstance()
		{
			return new VitaeLantern();
		}
	};

	private int id;
	private String textureLocation;

	private PowerSources(int id, String textureLocation)
	{
		this.id = id;
		this.textureLocation = textureLocation;
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
}
