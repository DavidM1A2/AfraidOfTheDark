/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell;

public interface ISpellComponentEnum
{
	public int getID();

	public String getIcon();

	public String getName();

	public <T extends ISpellComponent> T newInstance();

	/**
	 * 
	 * @return The name of the spell component but formatted
	 */
	public String getNameFormatted();
}
