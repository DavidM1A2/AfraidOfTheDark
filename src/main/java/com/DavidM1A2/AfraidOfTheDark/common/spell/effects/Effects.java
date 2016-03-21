/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

import com.DavidM1A2.AfraidOfTheDark.common.spell.ISpellComponentEnum;

public enum Effects implements ISpellComponentEnum
{
	Explosion(1, "afraidofthedark:textures/gui/spellCrafting/effects/explosion.png");

	private int id;
	private String iconTexture;

	private Effects(int id, String iconTexture)
	{
		this.id = id;
		this.iconTexture = iconTexture;
	}

	public int getID()
	{
		return this.id;
	}

	@Override
	public String getIcon()
	{
		return this.iconTexture;
	}

	@Override
	public String getName()
	{
		return this.toString();
	}
}
