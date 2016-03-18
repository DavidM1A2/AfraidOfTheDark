/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources;

public enum PowerSources
{
	Self(1);

	private int id;

	private PowerSources(int id)
	{
		this.id = id;
	}

	public int getID()
	{
		return this.id;
	}
}
