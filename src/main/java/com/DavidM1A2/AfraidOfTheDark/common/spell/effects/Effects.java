/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

public enum Effects
{
	Explosion(1);

	private int id;

	private Effects(int id)
	{
		this.id = id;
	}

	public int getID()
	{
		return this.id;
	}
}
