/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell;

import java.io.Serializable;

public interface ISpellComponent extends Serializable
{
	/**
	 * 
	 * @return The enum constant representing this class
	 */
	public abstract ISpellComponentEnum getType();
}
