/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

import com.DavidM1A2.AfraidOfTheDark.common.spell.EffectAffinity;
import com.DavidM1A2.AfraidOfTheDark.common.spell.ISpellComponentEnum;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

public enum Effects implements ISpellComponentEnum
{
	Explosion(1, Explosion.class, new EffectAffinity(0, .2, .1, .5), "afraidofthedark:textures/gui/spellCrafting/effects/explosion.png"),
	Grow(2, Grow.class, new EffectAffinity(0, 0, 1, 0), "afraidofthedark:textures/gui/spellCrafting/effects/grow.png"),
	Heal(3, Heal.class, new EffectAffinity(.3, 0, .5, 0), "afraidofthedark:textures/gui/spellCrafting/effects/heal.png");

	private int id;
	private String iconTexture;
	private Class<? extends Effect> effectClass;
	private EffectAffinity effectAffinity;

	private Effects(int id, Class<? extends Effect> effectClass, EffectAffinity effectAffinity, String iconTexture)
	{
		this.id = id;
		this.iconTexture = iconTexture;
		this.effectClass = effectClass;
		this.effectAffinity = effectAffinity;
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

	public EffectAffinity getAffinity()
	{
		return this.effectAffinity;
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
