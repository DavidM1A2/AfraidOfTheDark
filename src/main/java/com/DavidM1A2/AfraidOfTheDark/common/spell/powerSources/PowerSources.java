/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources;

public enum PowerSources
{
	Self(1, "afraidofthedark:textures/gui/spellCrafting/powerSources/self.png");

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
}
