/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

import java.io.Serializable;

public abstract class Effect implements Serializable
{
	public abstract double getCost();

	public abstract void performEffect();
}
