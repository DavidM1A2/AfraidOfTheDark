/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

import com.DavidM1A2.AfraidOfTheDark.common.spell.ISpellComponentEnum;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

public enum Effects implements ISpellComponentEnum
{
	Explosion(1, Explosion.class, "afraidofthedark:textures/gui/spellCrafting/effects/explosion.png"),
	Grow(2, Grow.class, "afraidofthedark:textures/gui/spellCrafting/effects/grow.png"),
	Heal(3, Heal.class, "afraidofthedark:textures/gui/spellCrafting/effects/heal.png");

	private int id;
	private String iconTexture;
	private Class<? extends Effect> effectClass;

	private Effects(int id, Class<? extends Effect> effectClass, String iconTexture)
	{
		this.id = id;
		this.iconTexture = iconTexture;
		this.effectClass = effectClass;
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

	@Override
	public Effect newInstance()
	{
		try
		{
			return this.effectClass.newInstance();
		}
		catch (InstantiationException e)
		{
			LogHelper.error("Failed to create a new instance of a effect...");
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			LogHelper.error("Failed to create a new instance of a effect...");
			e.printStackTrace();
		}
		return null;
	}
}
